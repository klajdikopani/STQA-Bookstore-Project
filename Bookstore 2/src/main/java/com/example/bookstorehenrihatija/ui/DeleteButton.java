package com.example.bookstorehenrihatija.ui;

import javafx.scene.control.Button;
import javafx.scene.paint.Color;

public class DeleteButton extends Button {
    public DeleteButton() {
        super.setText("Delete");
        super.setTextFill(Color.WHITE);
        super.setStyle("-fx-background-color: darkred");
    }
}
