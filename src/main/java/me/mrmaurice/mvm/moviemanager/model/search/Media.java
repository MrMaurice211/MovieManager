package me.mrmaurice.mvm.moviemanager.model.search;

public class Media {

    private String image;
    private String title;
    private String id;

    public String image() {
        return image;
    }

    public String title() {
        return title;
    }

    public String id() {
        return id;
    }

    public int numericId() {
        return Integer.parseInt(id.replace("tt", ""));
    }

}
