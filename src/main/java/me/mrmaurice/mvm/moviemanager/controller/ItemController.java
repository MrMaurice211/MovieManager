package me.mrmaurice.mvm.moviemanager.controller;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import me.mrmaurice.mvm.moviemanager.model.search.Media;

import java.util.function.Consumer;

public class ItemController {

    @FXML
    private ImageView itemView;
    private Consumer<Media> onClick;
    private Media item;

    public void setMedia(Media media) {
        this.item = media;
        itemView.setImage(new Image(media.image()));
    }

    public void setOnClick(Consumer<Media> onClick) {
        this.onClick = onClick;
    }

    @FXML
    void selectMedia(MouseEvent event) {
        if (onClick != null) {
            onClick.accept(item);
        }
    }

}

