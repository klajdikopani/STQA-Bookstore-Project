package com.example.bookstorehenrihatija.controllers;

import com.example.bookstorehenrihatija.auxiliaries.FileHandler;
import com.example.bookstorehenrihatija.models.Role;
import com.example.bookstorehenrihatija.models.User;
import com.example.bookstorehenrihatija.views.AdminView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.ArrayList;

public class AdminController {
    private final AdminView adminView;
    public AdminController(AdminView view){
        this.adminView = view;
        setSaveListener();
        setDeleteListener();
        setSearchListener();
        setEditListener();
    }
    private void setEditListener(){
        adminView.getUsernameCol().setOnEditCommit(e -> {
            User userToEdit = e.getRowValue();
            int index = User.getUsers().indexOf(userToEdit);
            User.getUsers().get(index).setUsername(e.getNewValue());
        });
        adminView.getFirstnameCol().setOnEditCommit(e -> {
            User userToEdit = e.getRowValue();
            int index = User.getUsers().indexOf(userToEdit);
            User.getUsers().get(index).setFirstName(e.getNewValue());
        });
        adminView.getLastnameCol().setOnEditCommit(e -> {
            User userToEdit = e.getRowValue();
            int index = User.getUsers().indexOf(userToEdit);
            User.getUsers().get(index).setLastName(e.getNewValue());
        });
        adminView.getEmailCol().setOnEditCommit(e -> {
            User userToEdit = e.getRowValue();
            int index = User.getUsers().indexOf(userToEdit);
            User.getUsers().get(index).setEmail(e.getNewValue());
        });
        adminView.getSalaryCol().setOnEditCommit(e -> {
            User userToEdit = e.getRowValue();
            int index = User.getUsers().indexOf(userToEdit);
            User.getUsers().get(index).setSalary(e.getNewValue());
        });
        adminView.getRoleCol().setOnEditCommit(e -> {
            User userToEdit = e.getRowValue();
            int index = User.getUsers().indexOf(userToEdit);
            User.getUsers().get(index).setRole(Role.valueOf(e.getNewValue()));
        });
        adminView.getEditBtn().setOnAction(e -> {
            try{
                FileHandler.overwriteCurrentListToFile(User.DATA_FILE, User.getUsers());
                adminView.getResultLabel().setText("Users were successfully edited");
            }catch(IOException ex){
                adminView.getResultLabel().setText("Writing users failed!");
                ex.printStackTrace();
            }
        });
    }
    private void setSearchListener(){
        adminView.getSearchView().getClearBtn().setOnAction(e -> {
            adminView.getSearchView().getSearchField().setText("");
            adminView.getTableView().setItems(FXCollections.observableArrayList(User.getUsers()));
        });
        adminView.getSearchView().getSearchBtn().setOnAction(e -> {
            String searchText = adminView.getSearchView().getSearchField().getText();
            ArrayList<User> searchResults = User.getSearchResults(searchText);
            adminView.getTableView().setItems(FXCollections.observableArrayList(searchResults));
        });
    }
    private void setDeleteListener(){
        adminView.getDeleteBtn().setOnAction(e -> {
            ObservableList<User> usersInTable = adminView.getTableView().getItems();
            ObservableList<Integer> indices = adminView.getTableView().getSelectionModel().getSelectedIndices();
            for(int index : indices)
                usersInTable.get(index).deleteFromFile();
            adminView.getTableView().setItems(FXCollections.observableArrayList(User.getUsers()));
            adminView.getResultLabel().setText("Users deleted successfully!");
        });
    }
    private void setSaveListener(){
        adminView.getSaveBtn().setOnAction(e -> {
            String username = adminView.getUsernameField().getText();
            String password = adminView.getPasswordField().getText();
            String firstName = adminView.getFirstNameField().getText();
            String lastName = adminView.getLastNameField().getText();
            String email = adminView.getEmailField().getText();
            Double salary = Double.parseDouble(adminView.getSalaryField().getText());
            Role role = adminView.getRoleComboBox().getValue();
            User user = new User(username, password, firstName,
                            lastName, email, salary, role);
            if(user.SaveInFile()){
                adminView.getTableView().getItems().add(user);
                adminView.getResultLabel().setText("User created successfully!");
                resetFields();
            }else{
                adminView.getResultLabel().setText("User creation failed!");
            }
        });
    }
    private void resetFields(){
        adminView.getUsernameField().setText("");
        adminView.getFirstNameField().setText("");
        adminView.getLastNameField().setText("");
        adminView.getEmailField().setText("");
        adminView.getSalaryField().setText("");
        adminView.getPasswordField().setText("");
    }
}
