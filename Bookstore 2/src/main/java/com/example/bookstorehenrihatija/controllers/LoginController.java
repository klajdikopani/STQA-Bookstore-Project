package com.example.bookstorehenrihatija.controllers;

import com.example.bookstorehenrihatija.models.User;
import com.example.bookstorehenrihatija.views.LoginView;
import com.example.bookstorehenrihatija.views.View;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginController{
    private final Stage primaryStage;
    private final View nextView;
    private User currentUser;
    public User getCurrentUser(){ return currentUser;}
    public LoginController(LoginView view, View nextView, Stage primaryStage){
        this.primaryStage = primaryStage;
        this.nextView = nextView;
        addListener(view);
    }
    private void addListener(LoginView view){
        view.getLoginBtn().setId("loginBtn");
        view.getLoginBtn().setOnAction(e -> {
            String password = view.getPasswordField().getText();
            String username = view.getUsernameField().getText();
            User potentialUser = new User(username, password);
            if((currentUser = User.getIfExists(potentialUser)) != null){
                View.setCurrentUser(currentUser);
                primaryStage.setScene(new Scene(nextView.getView()));
            }else
                view.getErrorLabel().setText("Wrong username or password");
        });
    }
}
