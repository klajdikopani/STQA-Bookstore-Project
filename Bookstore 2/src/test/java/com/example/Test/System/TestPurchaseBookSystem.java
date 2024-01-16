package com.example.Test.System;

import com.example.bookstorehenrihatija.controllers.ManagerController;
import com.example.bookstorehenrihatija.models.*;
import com.example.bookstorehenrihatija.views.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationTest;
import org.junit.jupiter.api.*;
import static org.testfx.assertions.api.Assertions.assertThat;


public class TestPurchaseBookSystem extends ApplicationTest {
    Stage primaryStage;
    TextField isbnField;
    TextField countField;
    Button sellBtn;
    Label libAmountLabel;
    TableView<BookOrder> managerTable;
    Tab librarianTab;
    TextField managerSearchField;
    Button managerSearchBtn;
    Button managerClearBtn;
    User dummyUser = new User();

    @Override
    public void start(Stage stage){

        LibrarianView libView = new LibrarianView(new BookView());

        dummyUser.setUsername("dummyUsername4");
        dummyUser.setPassword("dummyPassword4");
        dummyUser.setRole(Role.ADMIN);
        View.setCurrentUser(dummyUser);
//        Scene scene = new Scene(libView.getView(), 700, 700);
        Scene scene = new Scene(new MainView().getView(), 700, 700);
        stage.setScene(scene);
        stage.show();
    }
    @Test
    public void test_sellBook(){
        clickOn((Node) lookup(".tab-pane > .tab-header-area > .headers-region > .tab").nth(7).query());;
        isbnField = lookup("#isbnField").queryAs(TextField.class);
        countField = lookup("#countField").queryAs(TextField.class);
        sellBtn = lookup("#sellBtn").queryAs(Button.class);
        libAmountLabel = lookup("#libAmountLabel").queryAs(Label.class);
        Book.getBooks().add(
                new Book("0000000000000", "Dummy Title", 1000, 3000,
                        new Author("dummyFirstname", "dummyLastname"), 50)
        );
        clickOn(isbnField).write("0000000000000");
        clickOn(countField).write("5");
        clickOn(sellBtn);
        assertThat(libAmountLabel).hasText("Your amount is: $15000.0");


        clickOn((Node) lookup(".tab-pane > .tab-header-area > .headers-region > .tab").nth(4).query());
        managerSearchField = lookup("#managerSearchField").queryAs(TextField.class);
        managerSearchBtn = lookup("#managerSearchBtn").queryAs(Button.class);
        managerClearBtn = lookup("#managerClearBtn").queryAs(Button.class);
        clickOn(managerSearchField).write("dummy text");
        clickOn(managerSearchBtn);
        clickOn(managerClearBtn);
        managerTable = lookup("#managerTable").queryAs(TableView.class);
//        clickOn((Node) lookup("#managerTable").nth(1).query());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        for(BookOrder order : managerTable.getItems()){
            if(order.getUser().equals(dummyUser)) {
                org.junit.jupiter.api.Assertions.assertTrue(true);
                return;
            }
        }
        org.junit.jupiter.api.Assertions.assertTrue(false);

    }
}
