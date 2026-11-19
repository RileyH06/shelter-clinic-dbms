module com.shelterclinic {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.shelterclinic to javafx.fxml;
    exports com.shelterclinic;
}
