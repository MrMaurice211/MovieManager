package me.mrmaurice.mvm.moviemanager.model.sub;

import java.util.List;

public class SubResponse {

    // imdb_id, episode, season
    public static final String ENDPOINT = "https://api.opensubtitles.com/api/v1/subtitles?trusted_sources=only" +
            "&order_direction=desc" +
            "&order_by=download_count" +
            "&languages=es" +
            "&imdb_id=%d" +
            "&season_number=%d" +
            "&episode_number=%d";

    private List<Sub> data;

    public List<Sub> data() {
        return data;
    }

}
