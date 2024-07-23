module com.example.recipe {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;
    requires org.postgresql.jdbc;
    requires org.slf4j;
    requires jbcrypt;
    requires java.desktop;

    opens com.example.recipe to javafx.fxml;
    exports com.example.recipe;
    opens com.example.recipe.ui.common.login to javafx.fxml;
    exports com.example.recipe.ui.common.login;
}