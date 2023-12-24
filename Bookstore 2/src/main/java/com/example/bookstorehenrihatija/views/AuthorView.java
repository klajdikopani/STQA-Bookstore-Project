package com.example.bookstorehenrihatija.views;

import com.example.bookstorehenrihatija.controllers.AuthorController;
import com.example.bookstorehenrihatija.models.Author;
import com.example.bookstorehenrihatija.ui.CreateButton;
import com.example.bookstorehenrihatija.ui.DeleteButton;
import com.example.bookstorehenrihatija.ui.EditButton;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class AuthorView extends View{
    private final BorderPane borderPane = new BorderPane();
    private final TableView<Author> tableView = new TableView<>();
    private final HBox formPane = new HBox();
    private final TextField firstNameField = new TextField();
    private final TextField lastNameField = new TextField();
    private final Button saveBtn = new CreateButton();
    private final Button deleteBtn = new DeleteButton();
    private final Button editBtn = new EditButton();
    private final TableColumn<Author, String> firstNameCol = new TableColumn<>("First name");
    private final TableColumn<Author, String> lastNameCol = new TableColumn<>("Last name");
    private final Label resultLabel = new Label("");
    private final SearchView searchView = new SearchView("Search for an author");
    public SearchView getSearchView(){ return searchView; }
    public TableColumn<Author, String> getFirstNameCol(){ return firstNameCol; }
    public TableColumn<Author, String> getLastNameCol(){ return lastNameCol; }
    public Button getEditBtn(){ return editBtn; }
    public Button getDeleteBtn(){ return deleteBtn; }
    public Button getSaveBtn(){ return saveBtn; }
    public Label getResultLabel(){ return resultLabel; }
    public TableView<Author> getTableView(){ return tableView; }
    public TextField getFirstNameField(){ return firstNameField; }
    public TextField getLastNameField(){ return lastNameField; }
    public AuthorView(){
        setTableView();
        setForm();
        new AuthorController(this);
    }
    @Override
    public Parent getView() {
        borderPane.setCenter(tableView);
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(5);
        vbox.getChildren().addAll(formPane, resultLabel);
        borderPane.setBottom(vbox);
        borderPane.setTop(searchView.getSearchPane());
        return borderPane;
    }
    private void setForm(){
        formPane.setPadding(new Insets(20));
        formPane.setSpacing(20);
        formPane.setAlignment(Pos.CENTER);
        Label firstNameLabel = new Label("First name: ", firstNameField);
        firstNameLabel.setContentDisplay(ContentDisplay.TOP);
        Label lastNameLabel = new Label("Last name: ", lastNameField);
        lastNameLabel.setContentDisplay(ContentDisplay.TOP);
        formPane.getChildren().addAll(firstNameLabel, lastNameLabel, saveBtn, deleteBtn, editBtn);
    }
    private void setTableView(){
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.setEditable(true);
        tableView.setItems(FXCollections.observableArrayList(Author.getAuthors()));
        firstNameCol.setCellValueFactory(
                new PropertyValueFactory<>("firstName")
        );
        firstNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        lastNameCol.setCellValueFactory(
                new PropertyValueFactory<>("lastName")
        );
        lastNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        tableView.getColumns().addAll(firstNameCol, lastNameCol);
    }
}
