package com.example.bookstorehenrihatija.views;

import com.example.bookstorehenrihatija.models.Book;
import com.example.bookstorehenrihatija.models.Role;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;

public class MainView extends View{
    @Override
    public Parent getView() {
        BorderPane borderPane = new BorderPane();
        TabPane tabPane = new TabPane();
        Tab authorTab = new Tab("Authors");
        authorTab.setContent(new AuthorView().getView());
        Tab bookTab = new Tab("Books");
        BookView bookView = new BookView();
        bookTab.setContent(bookView.getView());
        Tab librarianTab = new Tab("Librarian");
        librarianTab.setContent(new LibrarianView(bookView).getView());
        Tab managerTab = new Tab("Manager");
        managerTab.setContent(new ManagerView().getView());
        Tab adminTab = new Tab("Admin");
        adminTab.setContent(new AdminView().getView());
        Role currentRole = (getCurrentUser() != null ? getCurrentUser().getRole() : null);
        if(currentRole != null){
            if(currentRole == Role.ADMIN)
                tabPane.getTabs().add(adminTab);
            if(currentRole == Role.MANAGER || currentRole == Role.ADMIN)
            {
                checkForLowStock();
                tabPane.getTabs().addAll(managerTab, authorTab, bookTab);
            }

            tabPane.getTabs().add(librarianTab);
        }
        borderPane.setTop(tabPane);
        borderPane.setBottom(new StackPane(new TextArea(getCurrentUser().getUsername() + ", welcome to our bookstore")));
        return (Parent) borderPane;
    }
    private void checkForLowStock(){
        ArrayList<Book> lowStockBooks = new ArrayList<>();
        for(Book book : Book.getBooks()){
            if(book.getCount() < 5)
                lowStockBooks.add(book);
        }
        if(lowStockBooks.size() == 0){
            return;
        }
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(20));
        vBox.setSpacing(10);
        vBox.getChildren().add(new Label("List of low stock books:"));
        for(Book book : lowStockBooks){
            vBox.getChildren().add(new Label(book.getTitle() + ": " + book.getCount() + " copies"));
        }
        Stage stage = new Stage();
        Scene scene = new Scene(vBox);
        stage.setScene(scene);
        stage.show();
    }
}
