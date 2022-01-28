module _3d._3d {
    requires javafx.controls;
    requires javafx.fxml;
    requires commons.math3;


    opens app to javafx.fxml;
    exports app;
    exports app.AffineOperations;
    opens app.AffineOperations to javafx.fxml;
}