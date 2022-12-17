package me.mrmaurice.mvm.moviemanager.model.episode;

import java.util.List;

public class EpisodeResponse {

    // apiKey/imdbId/seasonNumber
    public static final String ENDPOINT = "https://imdb-api.com/en/API/SeasonEpisodes/%s/%s/%d";

    private String imDbId;
    private String title;
    private List<Episode> episodes;

    public String imDbId() {
        return imDbId;
    }

    public String title() {
        return title;
    }

    public List<Episode> episodes() {
        return episodes;
    }

}
