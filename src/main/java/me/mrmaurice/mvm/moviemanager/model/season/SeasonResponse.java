package me.mrmaurice.mvm.moviemanager.model.season;

public class SeasonResponse {

    // apiKey/imdbId
    public static final String ENDPOINT = "https://imdb-api.com/en/API/Title/%s/%s";

    private String id;
    private String title;
    private TvSeriesInfo tvSeriesInfo;

    public String id() {
        return id;
    }

    public String title() {
        return title;
    }

    public TvSeriesInfo tvSeriesInfo() {
        return tvSeriesInfo;
    }

}
