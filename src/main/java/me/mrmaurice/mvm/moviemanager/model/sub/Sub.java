package me.mrmaurice.mvm.moviemanager.model.sub;

import me.mrmaurice.mvm.moviemanager.model.download.DownloadRequest;

import java.util.List;

public class Sub {

    private String id;
    private SubAttribute attributes;

    public String id() {
        return id;
    }

    public SubAttribute attributes() {
        return attributes;
    }

    public DownloadRequest asRequest(String title) {

        if (attributes.files().isEmpty()) {
            System.out.println("No files found");
            return null;
        }

        SubAttribute.SubAttributeFile file = attributes.files().get(0);
        int fileId = file.file_id();

        String fileNameFormat = "%s - %s.es";
        String seasonFormat = "s%02de%02d";
        String seasonInfo = String.format(seasonFormat, attributes.feature_details().season_number(),
                attributes.feature_details().episode_number());

        String fileName = String.format(fileNameFormat, title, seasonInfo);
        return new DownloadRequest(fileId, fileName);
    }

    public static class SubAttribute {

        private String language;
        private SubAttributeFeature feature_details;
        private List<SubAttributeFile> files;

        public String language() {
            return language;
        }

        public SubAttributeFeature feature_details() {
            return feature_details;
        }

        public List<SubAttributeFile> files() {
            return files;
        }

        public static class SubAttributeFeature {

            private int season_number;
            private int episode_number;
            private String parent_title;

            public int season_number() {
                return season_number;
            }

            public int episode_number() {
                return episode_number;
            }

            public String parent_title() {
                return parent_title;
            }

        }

        public static class SubAttributeFile {

            private int file_id;

            public int file_id() {
                return file_id;
            }

        }

    }

}
