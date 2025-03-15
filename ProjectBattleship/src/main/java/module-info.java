module com.mycompany.projectbattleship {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.projectbattleship to javafx.fxml;
    exports com.mycompany.projectbattleship;
}
