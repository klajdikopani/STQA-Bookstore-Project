package com.example.bookstorehenrihatija.views;

import com.example.bookstorehenrihatija.controllers.AdminController;
import com.example.bookstorehenrihatija.models.Role;
import com.example.bookstorehenrihatija.models.User;
import com.example.bookstorehenrihatija.ui.CreateButton;
import com.example.bookstorehenrihatija.ui.DeleteButton;
import com.example.bookstorehenrihatija.ui.EditButton;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.converter.DoubleStringConverter;

import java.util.ArrayList;

public class AdminView extends View{
    private final BorderPane borderPane = new BorderPane();
    private final TableView<User> tableView = new TableView<>();
    private final HBox formPane = new HBox();
    private final TextField usernameField = new TextField();
    private final TextField passwordField = new TextField();
    private final TextField firstNameField = new TextField();
    private final TextField lastNameField = new TextField();
    private final TextField emailField = new TextField();
    private final TextField salaryField = new TextField();
    private final ComboBox<Role> roleComboBox = new ComboBox<>();
    private final Button saveBtn = new CreateButton();
    private final Button deleteBtn = new DeleteButton();
    private final Button editBtn = new EditButton();
    private final TableColumn<User, String> usernameCol = new TableColumn<>("Username");
    private final TableColumn<User, String> firstnameCol = new TableColumn<>("First name");
    private final TableColumn<User, String> lastnameCol = new TableColumn<>("Last name");
    private final TableColumn<User, String> emailCol = new TableColumn<>("Email");
    private final TableColumn<User, Double> salaryCol = new TableColumn<>("Salary");
    private final TableColumn<User, String> roleCol = new TableColumn<>("Role");
    private final Label resultLabel = new Label("");
    private final SearchView searchView = new SearchView("Search for a user");
    public TableView<User> getTableView(){ return tableView; }
    public TextField getUsernameField(){ return usernameField; }
    public TextField getPasswordField(){ return  passwordField; }
    public TextField getFirstNameField(){ return firstNameField; }
    public TextField getLastNameField(){ return lastNameField; }
    public TextField getEmailField(){ return emailField; }
    public TextField getSalaryField(){ return salaryField; }

    public ComboBox<Role> getRoleComboBox() {
        return roleComboBox;
    }
    public Button getSaveBtn(){ return saveBtn; }
    public Button getDeleteBtn(){ return deleteBtn; }
    public Button getEditBtn(){ return editBtn; }
    public TableColumn<User, String> getUsernameCol(){ return usernameCol; }
    public TableColumn<User, String> getFirstnameCol(){ return firstnameCol;}
    public TableColumn<User, String> getLastnameCol(){ return lastnameCol; }
    public TableColumn<User, String> getEmailCol(){ return emailCol; }
    public TableColumn<User, Double> getSalaryCol(){ return salaryCol; }
    public TableColumn<User, String> getRoleCol(){ return roleCol; }
    public Label getResultLabel(){ return resultLabel; }
    public SearchView getSearchView(){ return searchView; }
    public AdminView(){
        setTableView();
        setForm();
        new AdminController(this);
    }

    private void setForm(){
        formPane.setPadding(new Insets(20));
        formPane.setSpacing(20);
        formPane.setAlignment(Pos.CENTER);
        Label usernameLabel = new Label("Username:", usernameField);
        usernameLabel.setContentDisplay(ContentDisplay.TOP);
        Label firstnameLabel = new Label("First name:", firstNameField);
        firstnameLabel.setContentDisplay(ContentDisplay.TOP);
        Label lastnameLabel = new Label("Last name:", lastNameField);
        lastnameLabel.setContentDisplay(ContentDisplay.TOP);
        Label passwordLabel = new Label("Password:", passwordField);
        passwordLabel.setContentDisplay(ContentDisplay.TOP);
        Label emailLabel = new Label("Email:", emailField);
        emailLabel.setContentDisplay(ContentDisplay.TOP);
        Label salaryLabel = new Label("Salary:", salaryField);
        salaryLabel.setContentDisplay(ContentDisplay.TOP);
        Label roleLabel = new Label("Role", roleComboBox);
        roleComboBox.getItems().setAll(new Role[]{ Role.ADMIN, Role.MANAGER, Role.LIBRARIAN});
        roleComboBox.setValue(Role.LIBRARIAN);
        roleLabel.setContentDisplay(ContentDisplay.TOP);
        formPane.getChildren().addAll(usernameLabel, passwordLabel, firstnameLabel, lastnameLabel,
                        emailLabel, salaryLabel, roleLabel, saveBtn, deleteBtn, editBtn);
    }

    private void setTableView(){
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.setEditable(true);
        tableView.setItems(FXCollections.observableArrayList(User.getUsers()));
        usernameCol.setCellValueFactory(
                new PropertyValueFactory<>("username")
        );
        usernameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        firstnameCol.setCellValueFactory(
                new PropertyValueFactory<>("firstName")
        );
        firstnameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        lastnameCol.setCellValueFactory(
                new PropertyValueFactory<>("lastName")
        );
        lastnameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        emailCol.setCellValueFactory(
                new PropertyValueFactory<>("email")
        );
        emailCol.setCellFactory(TextFieldTableCell.forTableColumn());
        salaryCol.setCellValueFactory(
                new PropertyValueFactory<>("salary")
        );
        salaryCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        roleCol.setCellValueFactory(celldata -> new ReadOnlyStringWrapper(celldata.getValue().getRole().toString()));
        roleCol.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(getRoleStrings())));
        tableView.getColumns().addAll(usernameCol, firstnameCol, lastnameCol, emailCol, salaryCol, roleCol);
    }
    private ArrayList<String> getRoleStrings(){
        ArrayList<String> roleStrings = new ArrayList<>();
        roleStrings.add("MANAGER");
        roleStrings.add("LIBRARIAN");
        roleStrings.add("ADMIN");
        return roleStrings;
    }
    @Override
    public Parent getView() {
        borderPane.setCenter(tableView);
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(5);
        vBox.getChildren().addAll(formPane, resultLabel);
        borderPane.setTop(searchView.getSearchPane());
        borderPane.setBottom(vBox);
        return borderPane;
    }
}
