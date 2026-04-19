module scratch {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires javafx.graphics;
    requires javafx.base;
    requires jdk.compiler;
    requires scratch;

    opens scratch to javafx.fxml;
    exports scratch;
}