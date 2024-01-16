module com.example.bookstorehenrihatija {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.bookstorehenrihatija to javafx.fxml;
    exports com.example.bookstorehenrihatija;
    opens com.example.bookstorehenrihatija.models to javafx.base;
}