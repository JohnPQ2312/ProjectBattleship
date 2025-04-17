module com.mycompany.projectbattleship {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.desktop;

    opens com.mycompany.projectbattleship to javafx.fxml;
    exports com.mycompany.projectbattleship;
}
