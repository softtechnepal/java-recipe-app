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

    opens com.example.recipe to javafx.fxml;
    exports com.example.recipe;
    opens com.example.recipe.uicontrollers.common to javafx.fxml;
    exports com.example.recipe.uicontrollers.common;
}