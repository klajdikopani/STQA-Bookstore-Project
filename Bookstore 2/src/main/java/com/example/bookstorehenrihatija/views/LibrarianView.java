package com.example.bookstorehenrihatija.views;

import com.example.bookstorehenrihatija.controllers.LibrarianController;
import com.example.bookstorehenrihatija.models.Book;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class LibrarianView extends View {
    private final BorderPane borderPane = new BorderPane();
    private final TextField isbnField = new TextField();
    private final TextField countField = new TextField();
    private final Button sellBtn = new Button("Sell");
    private final Label resultLabel = new Label("");
    private final Label amountLabel = new Label("");
    private final VBox formPane = new VBox();
    public TextField getIsbnField(){ return isbnField; }

    public TextField getCountField() {
        return countField;
    }

    public Button getSellBtn() {
        return sellBtn;
    }

    public Label getResultLabel() {
        return resultLabel;
    }

    public Label getAmountLabel() {
        return amountLabel;
    }

    public LibrarianView(BookView bookView){
        isbnField.setId("isbnField");
        countField.setId("countField");
        sellBtn.setId("sellBtn");
        amountLabel.setId("libAmountLabel");
        setFormPane();
        new LibrarianController(this, bookView);
    }
    @Override
    public Parent getView() {
        borderPane.setCenter(formPane);
        BorderPane temp = new BorderPane();
        temp.setCenter(resultLabel);
        borderPane.setBottom(temp);
        return borderPane;
    }
    private void setFormPane(){
        formPane.setAlignment(Pos.CENTER);
        formPane.setSpacing(20);
        formPane.setPadding(new Insets(20));
        Label isbnLabel = new Label("ISBN: ", isbnField);
        isbnLabel.setContentDisplay(ContentDisplay.BOTTOM);
        Label countLabel = new Label("Number of Copies: ", countField);
        countLabel.setContentDisplay(ContentDisplay.BOTTOM);
        formPane.getChildren().addAll(isbnLabel, countLabel, sellBtn, amountLabel);
    }
}
