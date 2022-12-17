module me.mrmaurice.mvm.moviemanager {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;
    requires java.net.http;
    requires com.google.gson;
    requires java.desktop;

    exports me.mrmaurice.mvm.moviemanager;
    exports me.mrmaurice.mvm.moviemanager.controller;
    opens me.mrmaurice.mvm.moviemanager.controller to javafx.fxml;

    exports me.mrmaurice.mvm.moviemanager.model.download;
    exports me.mrmaurice.mvm.moviemanager.model.episode;
    exports me.mrmaurice.mvm.moviemanager.model.search;
    exports me.mrmaurice.mvm.moviemanager.model.season;
    exports me.mrmaurice.mvm.moviemanager.model.sub;

    opens me.mrmaurice.mvm.moviemanager.model.download to com.google.gson;
    opens me.mrmaurice.mvm.moviemanager.model.episode to com.google.gson;
    opens me.mrmaurice.mvm.moviemanager.model.search to com.google.gson;
    opens me.mrmaurice.mvm.moviemanager.model.season to com.google.gson;
    opens me.mrmaurice.mvm.moviemanager.model.sub to com.google.gson;

}