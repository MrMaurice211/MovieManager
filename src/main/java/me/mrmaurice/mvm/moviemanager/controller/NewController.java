package me.mrmaurice.mvm.moviemanager.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.DirectoryChooser;
import me.mrmaurice.mvm.moviemanager.MovieManager;
import me.mrmaurice.mvm.moviemanager.PostRequest;
import me.mrmaurice.mvm.moviemanager.UrlRequest;
import me.mrmaurice.mvm.moviemanager.model.download.DownloadRequest;
import me.mrmaurice.mvm.moviemanager.model.download.DownloadResponse;
import me.mrmaurice.mvm.moviemanager.model.episode.EpisodeResponse;
import me.mrmaurice.mvm.moviemanager.model.search.Media;
import me.mrmaurice.mvm.moviemanager.model.search.SearchResponse;
import me.mrmaurice.mvm.moviemanager.model.season.SeasonResponse;
import me.mrmaurice.mvm.moviemanager.model.sub.Sub;
import me.mrmaurice.mvm.moviemanager.model.sub.SubResponse;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.ResourceBundle;

public class NewController implements Initializable {

    private static final String SUB_API_KEY = System.getProperty("subsApiKey");
    private static final String API_KEY = System.getProperty("apiKey");

    @FXML
    private GridPane gridPane;

    @FXML
    private RadioButton movieButton;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchText;

    @FXML
    private RadioButton serieButton;

    @FXML
    void searchInput(MouseEvent event) {
        String input = searchText.getText();

        if (input == null || input.isEmpty()) {
            System.out.println("Invalid input");
            return;
        }

        input = input.replace(" ", "%20");

        gridPane.getChildren().removeIf(o -> true);
        searchButton.setDisable(true);

        boolean movie = !serieButton.isSelected();
        String type = movie ? "SearchMovie" : "SearchSeries";
        String requestUrl = "https://imdb-api.com/en/API/%s/%s/%s";

        System.out.println("Using key: " + API_KEY);
        UrlRequest request = new UrlRequest(requestUrl, type, API_KEY, input);
        SearchResponse result = request.doCall(SearchResponse.class);
        List<Media> mediaList = result.results();

        int column = 0;
        int row = 1;
        try {
            for (Media media : mediaList) {

                if (media.image().isBlank())
                    continue;

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(MovieManager.class.getResource("ItemView.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                ItemController itemController = fxmlLoader.getController();
                itemController.setMedia(media);
                itemController.setOnClick(this::selectMedia);

                if (column == 2) {
                    column = 0;
                    row++;
                }

                gridPane.add(anchorPane, column++, row); //(child,column,row)
                //set grid width
                gridPane.setMinWidth(Region.USE_COMPUTED_SIZE);
                gridPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
                gridPane.setMaxWidth(Region.USE_PREF_SIZE);

                //set grid height
                gridPane.setMinHeight(Region.USE_COMPUTED_SIZE);
                gridPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
                gridPane.setMaxHeight(Region.USE_PREF_SIZE);

                GridPane.setMargin(anchorPane, new Insets(10));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        searchButton.setDisable(false);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ToggleGroup group = new ToggleGroup();
        movieButton.setToggleGroup(group);
        serieButton.setToggleGroup(group);
    }

    private void selectMedia(Media media) {

        DirectoryChooser fileChooser = new DirectoryChooser();
        fileChooser.setTitle("Select output folder");
        File targetFolder = fileChooser.showDialog(gridPane.getScene().getWindow());

        if (targetFolder == null)
            return;
        System.out.println("Selected folder: " + targetFolder.getAbsolutePath());

        SeasonResponse seasonResponse = new UrlRequest(SeasonResponse.ENDPOINT, API_KEY, media.id())
                .doCall(SeasonResponse.class);
        int seasons = seasonResponse.tvSeriesInfo().seasons().length;
        System.out.println(media.title() + " has " + seasons + " seasons");

        for (int s = 1; s <= seasons; s++) {
            EpisodeResponse episodeResponse = new UrlRequest(EpisodeResponse.ENDPOINT, API_KEY, media.id(), s)
                    .doCall(EpisodeResponse.class);

            int episodes = episodeResponse.episodes().size();

            System.out.println(media.title() + " season " + s + " has " + episodes + " episodes");

            for (int e = 1; e <= episodes; e++) {

                SubResponse subResponse = new UrlRequest(SubResponse.ENDPOINT, media.numericId(), s, e)
                        .header("Api-Key", SUB_API_KEY)
                        .header("Content-Type", "application/json")
                        .doCall(SubResponse.class);

                if (subResponse.data().isEmpty()) {
                    System.out.println("No subtitles found for " + media.title() + " season " + s + " episode " + e);
                    continue;
                }

                Sub sub = subResponse.data().get(0);
                DownloadRequest downloadRequest = sub.asRequest(media.title());
                DownloadResponse downloadResponse = new PostRequest(DownloadResponse.ENDPOINT)
                        .jsonBody(downloadRequest)
                        .header("Api-Key", SUB_API_KEY)
                        .header("Content-Type", "application/json")
                        .header("Accept", "*/*")
                        .execute(DownloadResponse.class);

                System.out.println("Downloading " + media.title() + " season " + s + " episode " + e);

                try (InputStream is = new URL(downloadResponse.link()).openStream()) {
                    File mediaFolder = new File(targetFolder, media.title());
                    File seasonFolder = new File(mediaFolder, "Season " + s);

                    if (!seasonFolder.exists())
                        seasonFolder.mkdirs();

                    File subFile = new File(seasonFolder, downloadResponse.file_name());
                    Files.copy(is, subFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

            }

        }

    }

}

