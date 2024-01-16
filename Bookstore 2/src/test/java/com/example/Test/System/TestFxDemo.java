package com.example.Test.System;

import com.example.bookstorehenrihatija.controllers.LoginController;
import com.example.bookstorehenrihatija.views.LoginView;
import com.example.bookstorehenrihatija.views.MainView;
import org.junit.jupiter.api.Assertions;
import org.testfx.assertions.api.AbstractStyleableAssert;
import org.testfx.framework.junit5.ApplicationTest;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.testfx.assertions.api.Assertions.assertThat;
public class TestFxDemo extends ApplicationTest {
    Button button;

    @Override
    public void start(Stage stage) {
        LoginView loginView = new LoginView();
        LoginController controller = new LoginController(loginView, new MainView(), stage);
        Scene scene = new Scene(loginView.getView(), 320, 240);
        stage.setTitle("Bookstore");
        stage.setScene(scene);
        stage.show();
    }

//    @BeforeEach
//    public void setUp() {
//        button = lookup(".button").queryAs(Button.class);
//    }

    @Test
    public void should_contain_button() {
        button = lookup("#loginBtn").queryAs(Button.class);
        assertThat(button).hasText("Login");
    }

//    @Test
//    public void should_click_on_button() {
//        clickOn(button);
//        assertThat(button).hasText("Clicked");
//    }
}
