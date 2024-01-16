package com.example.Test.System;

import com.example.bookstorehenrihatija.controllers.AdminController;
import com.example.bookstorehenrihatija.models.User;
import com.example.bookstorehenrihatija.views.AdminView;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.w3c.dom.Text;
import static org.testfx.assertions.api.Assertions.assertThat;

public class TestAdminSystem extends ApplicationTest {
    Button createButton;
    TextField usernameField;
    TextField passwordField;
    TextField firstnameField;
    TextField lastnameField;
    TextField emailField;
    TextField salaryField;
    TextField adminSearchField;
    Button adminSearchBtn;
    Button adminClearBtn;
    Label resultLabel;
    TableView<User> table;
    @Override
    public void start(Stage stage){
        AdminView adminView = new AdminView();
        adminView.getSaveBtn().setId("createBtn");
        adminView.getUsernameField().setId("usernameField");
        adminView.getPasswordField().setId("passwordField");
        adminView.getFirstNameField().setId("firstnameField");
        adminView.getLastNameField().setId("lastnameField");
        adminView.getEmailField().setId("emailField");
        adminView.getSalaryField().setId("salaryField");
        adminView.getResultLabel().setId("resultLabel");
        adminView.getTableView().setId("table");
        adminView.getSearchView().getSearchField().setId("adminSearchField");
        adminView.getSearchView().getSearchBtn().setId("adminSearchBtn");
        adminView.getSearchView().getClearBtn().setId("adminClearBtn");
        AdminController controller = new AdminController(adminView);
        Scene scene = new Scene(adminView.getView());
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void test_saveValidUser(){
        createButton = lookup("#createBtn").queryAs(Button.class);
        usernameField = lookup("#usernameField").queryAs(TextField.class);
        passwordField = lookup("#passwordField").queryAs(TextField.class);
        firstnameField = lookup("#firstnameField").queryAs(TextField.class);
        lastnameField = lookup("#lastnameField").queryAs(TextField.class);
        emailField = lookup("#emailField").queryAs(TextField.class);
        salaryField = lookup("#salaryField").queryAs(TextField.class);
        resultLabel = lookup("#resultLabel").queryAs(Label.class);
        table = lookup("#table").queryAs(TableView.class);
        adminSearchField = lookup("#adminSearchField").queryAs(TextField.class);
        adminClearBtn = lookup("#adminClearBtn").queryAs(Button.class);
        adminSearchBtn = lookup("#adminSearchBtn").queryAs(Button.class);
        clickOn(usernameField).write("testUserValid");
        clickOn(passwordField).write("testPasswordValid");
        clickOn(firstnameField).write("John");
        clickOn(lastnameField).write("Doe");
        clickOn(emailField).write("jdoe@gmail.com");
        clickOn(salaryField).write("5000");
        clickOn(createButton);
        assertThat(resultLabel).hasText("User created successfully!");
        User validUser = new User();
        validUser.setUsername("testUserValid");
        validUser.setPassword("testPasswordValid");
        Assertions.assertTrue(table.getItems().contains(validUser));


        clickOn(usernameField).write("testUserInvalid");
        clickOn(passwordField).write("testPasswordInvalid");
        clickOn(firstnameField).write("John");
        clickOn(lastnameField).write("Doe");
        clickOn(emailField).write("jdoe@gmail.com");
        clickOn(salaryField).write("-5000");
        clickOn(createButton);
        assertThat(resultLabel).hasText("User creation failed!");
        User inavlidUser = new User();
        inavlidUser.setUsername("testUserInvalid");
        inavlidUser.setPassword("testPasswordInvalid");
        Assertions.assertFalse(table.getItems().contains(inavlidUser));

        clickOn(adminSearchField).write("testUserValid");
        clickOn(adminSearchBtn);
        table = lookup("#table").queryAs(TableView.class);
        Assertions.assertTrue(table.getItems().contains(validUser) && table.getItems().size()==1);
    }


}
