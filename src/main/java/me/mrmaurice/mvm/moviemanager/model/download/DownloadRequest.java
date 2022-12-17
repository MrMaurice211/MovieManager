package me.mrmaurice.mvm.moviemanager.model.download;

public class DownloadRequest {

    private int file_id;
    private String sub_format = "srt";
    private String file_name;

    public DownloadRequest(int file_id, String file_name) {
        this.file_id = file_id;
        this.file_name = file_name;
    }

}
