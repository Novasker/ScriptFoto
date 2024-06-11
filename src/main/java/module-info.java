module com.script.scriptfoto {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.script.scriptfoto to javafx.fxml;
    exports com.script.scriptfoto;
}