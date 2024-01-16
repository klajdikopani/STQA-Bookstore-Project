package com.example.bookstorehenrihatija;

import com.example.bookstorehenrihatija.auxiliaries.Config;
import com.example.bookstorehenrihatija.controllers.LoginController;
import com.example.bookstorehenrihatija.models.*;
import com.example.bookstorehenrihatija.views.LoginView;
import com.example.bookstorehenrihatija.views.MainView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Properties;


//DO NOT USE THIS CLASS, USE AppLauncher instead
public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        LoginView loginView = new LoginView();
        LoginController controller = new LoginController(loginView, new MainView(), stage);
        Scene scene = new Scene(loginView.getView(), 320, 240);
        stage.setTitle("Bookstore");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        seedData();
        launch(args);

    }
    private static void seedData() {
        User admin = new User("admin", "Test2022", "Henri", "Hatija", "hhatija@gmail.com", 20000.0, Role.ADMIN);
        User manager = new User("manager", "Test2022", "Henri", "Hatija", "hhatija@gmail.com", 20000.0, Role.MANAGER);
        User librarian = new User("librarian", "Test2022", "Henri", "Hatija", "hhatija@gmail.com", 20000.0, Role.LIBRARIAN);
        BookOrder.getOrders().add(new BookOrder(admin, Book.getBooks().get(0), 10, LocalDateTime.parse("2023-01-14T15:32:56.000")));
        BookOrder.getOrders().add(new BookOrder(manager, Book.getBooks().get(0), 20, LocalDateTime.parse("2023-01-15T15:32:56.000")));
        BookOrder.getOrders().add(new BookOrder(admin, Book.getBooks().get(0), 10, LocalDateTime.parse("2022-11-14T15:32:56.000")));
        BookOrder.getOrders().add(new BookOrder(admin, Book.getBooks().get(0), 10, LocalDateTime.parse("2022-11-14T15:32:56.000")));
        BookOrder.getOrders().add(new BookOrder(admin, Book.getBooks().get(0), 5, LocalDateTime.parse("2022-11-14T15:32:56.000")));
        BookOrder.getOrders().add(new BookOrder(admin, Book.getBooks().get(0), 13, LocalDateTime.parse("2023-01-25T15:32:56.000")));
        BookOrder.getOrders().add(new BookOrder(admin, Book.getBooks().get(0), 12, LocalDateTime.parse("2023-01-26T15:32:56.000")));
        BookOrder.getOrders().add(new BookOrder(admin, Book.getBooks().get(0), 30, LocalDateTime.parse("2023-02-14T15:32:56.000")));
        BookOrder.getOrders().add(new BookOrder(admin, Book.getBooks().get(0), 2, LocalDateTime.parse("2023-02-15T15:32:56.000")));
        BookOrder.getOrders().add(new BookOrder(admin, Book.getBooks().get(0), 35, LocalDateTime.parse("2021-01-14T15:32:56.000")));
        if(User.DATA_FILE.length() == 0){
            try {
                ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(User.FILE_PATH));
                outputStream.writeObject(admin);
                outputStream.writeObject(manager);
                outputStream.writeObject(librarian);
                System.out.println("Wrote users to the file users.ser successfully");
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(Author.DATA_FILE.length() == 0){
            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(Author.FILE_PATH))) {
                outputStream.writeObject(new Author("Test1", "Test1"));
                outputStream.writeObject(new Author("Test2", "Test2"));
                outputStream.writeObject(new Author("Test3", "Test3"));
                System.out.println("Wrote authors to the file authors.dat successfully");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}