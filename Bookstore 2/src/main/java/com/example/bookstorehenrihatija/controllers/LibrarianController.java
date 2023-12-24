package com.example.bookstorehenrihatija.controllers;

import com.example.bookstorehenrihatija.auxiliaries.FileHandler;
import com.example.bookstorehenrihatija.auxiliaries.Handler;
import com.example.bookstorehenrihatija.models.Book;
import com.example.bookstorehenrihatija.models.BookOrder;
import com.example.bookstorehenrihatija.models.User;
import com.example.bookstorehenrihatija.views.AuthorView;
import com.example.bookstorehenrihatija.views.BookView;
import com.example.bookstorehenrihatija.views.LibrarianView;
import com.example.bookstorehenrihatija.views.View;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.time.LocalDateTime;

public class LibrarianController {
    private final LibrarianView view;
    private final BookView bookView;
    public LibrarianController(LibrarianView view, BookView bookView){
        this.bookView = bookView;
        this.view = view;
        setSellListener();
    }
    private void setSellListener(){
        view.getSellBtn().setOnAction(e -> {
            User user = View.getCurrentUser();
            Book book = findBook(view.getIsbnField().getText());
            int count = Integer.parseInt(view.getCountField().getText());
            if(book == null){
                view.getResultLabel().setText("Book not found!");
                return;
            }else if(count < 0){
                view.getResultLabel().setText("Please enter a proper input for number of copies.");
                return;
            }else if(count > book.getCount()){
                view.getResultLabel().setText("Not enough copies in stock! Copies left: " + book.getCount());
                return;
            }
            BookOrder order = new BookOrder(user, book, count, LocalDateTime.now());
            BookOrder.getOrders().add(order);
            order.writeBill();
            view.getAmountLabel().setText("Your amount is: $" + book.getSellingPrice() * count);
            view.getResultLabel().setText("Bill written successfully!");
            int index = Book.getBooks().indexOf(book);
            Book.getBooks().get(index).setCount(book.getCount() - count);
            try{
                FileHandler.overwriteCurrentListToFile(Book.DATA_FILE, Book.getBooks());
                FileHandler.overwriteCurrentListToFile(BookOrder.DATA_FILE, BookOrder.getOrders());
            }catch (IOException ex){
                ex.printStackTrace();
            }
            bookView.getTableView().setItems(FXCollections.observableArrayList(Book.getBooks()));
        });
    }
    private Book findBook(String isbn){
        for(Book book : Book.getBooks()){
            if(book.getIsbn().equals(isbn)){
                return book;
            }
        }
        return null;
    }
}
