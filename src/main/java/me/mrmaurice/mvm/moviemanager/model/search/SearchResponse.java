package me.mrmaurice.mvm.moviemanager.model.search;

import java.util.List;

public class SearchResponse {

    private List<Media> results;
    private String expression;

    public List<Media> results() {
        return results;
    }

    public String expression() {
        return expression;
    }

}
