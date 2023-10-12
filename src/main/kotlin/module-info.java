module com.unitech.schoolsystem {
    requires javafx.controls;
    requires nv.i18n;
    requires org.apache.poi.poi;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires java.sql;
    requires org.postgresql.jdbc;
    requires org.kordamp.ikonli.bootstrapicons;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.materialdesign2;
    requires org.kordamp.ikonli.fontawesome5;
    requires org.kordamp.ikonli.javafx;
    requires jbcrypt;


    opens com.unitech.schoolsystem to javafx.fxml;
    opens com.unitech.schoolsystem.model to javafx.fxml;
    exports com.unitech.schoolsystem;
    exports com.unitech.schoolsystem.model;


}