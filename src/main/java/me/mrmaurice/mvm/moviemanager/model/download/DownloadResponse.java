package me.mrmaurice.mvm.moviemanager.model.download;

public class DownloadResponse {

    public static final String ENDPOINT = "https://api.opensubtitles.com/api/v1/download";

    private String link;
    private String file_name;
    private int remaining;

    public String link() {
        return link;
    }

    public String file_name() {
        return file_name;
    }

    public int remaining() {
        return remaining;
    }

}
