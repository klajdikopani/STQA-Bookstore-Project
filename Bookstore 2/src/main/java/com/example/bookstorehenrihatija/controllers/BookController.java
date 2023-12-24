package com.example.bookstorehenrihatija.controllers;

import com.example.bookstorehenrihatija.auxiliaries.FileHandler;
import com.example.bookstorehenrihatija.models.Author;
import com.example.bookstorehenrihatija.models.Book;
import com.example.bookstorehenrihatija.views.BookView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class BookController {
    private final BookView bookView;
    public BookController(BookView bookView){
        this.bookView = bookView;
        setSaveListener();
        setDeleteListener();
        setSearchListener();
        setEditListener();
    }
    private void setEditListener(){
        bookView.getTitleCol().setOnEditCommit(e -> {
            Book bookToEdit = e.getRowValue();
            int index = Book.getBooks().indexOf(bookToEdit);
            Book.getBooks().get(index).setTitle(e.getNewValue());
        });
        bookView.getIsbnCol().setOnEditCommit(e ->{
            Book bookToEdit = e.getRowValue();
            int index = Book.getBooks().indexOf(bookToEdit);
            Book.getBooks().get(index).setIsbn(e.getNewValue());
        });
        bookView.getPurchasedPriceCol().setOnEditCommit(e -> {
            Book bookToEdit = e.getRowValue();
            int index = Book.getBooks().indexOf(bookToEdit);
            Book.getBooks().get(index).setPurchasedPrice(e.getNewValue());
        });
        bookView.getSellingPriceCol().setOnEditCommit(e -> {
            Book bookToEdit = e.getRowValue();
            int index = Book.getBooks().indexOf(bookToEdit);
            Book.getBooks().get(index).setSellingPrice(e.getNewValue());
        });
        bookView.getAuthorCol().setOnEditCommit(e -> {
            Book bookToEdit = e.getRowValue();
            int index = Book.getBooks().indexOf(bookToEdit);
            Author newAuthor = findAuthor(e.getNewValue());
            Book.getBooks().get(index).setAuthor(newAuthor);
        });
        bookView.getCountCol().setOnEditCommit(e -> {
            Book bookToEdit = e.getRowValue();
            int index = Book.getBooks().indexOf(bookToEdit);
            Book.getBooks().get(index).setCount(e.getNewValue());
        });
        bookView.getEditBtn().setOnAction(e -> {
            try{
                FileHandler.overwriteCurrentListToFile(Book.DATA_FILE, Book.getBooks());
                bookView.getResultLabel().setText("Books were successfully edited");
            }catch(IOException ex){
                bookView.getResultLabel().setText("Writing books failed");
                ex.printStackTrace();
            }
        });
    }
    private void setSearchListener(){
        bookView.getSearchView().getClearBtn().setOnAction(e -> {
            bookView.getSearchView().getSearchField().setText("");
            bookView.getTableView().setItems(FXCollections.observableArrayList(Book.getBooks()));
        });
        bookView.getSearchView().getSearchBtn().setOnAction(e -> {
            String searchText = bookView.getSearchView().getSearchField().getText();
            ArrayList<Book> searchResults = Book.getSearchResults(searchText);
            bookView.getTableView().setItems(FXCollections.observableArrayList(searchResults));
        });
    }
    private void setDeleteListener(){
        bookView.getDeleteBtn().setOnAction(e -> {
            ObservableList<Book> booksInTable = bookView.getTableView().getItems();
            ObservableList<Integer> indices = bookView.getTableView().getSelectionModel().getSelectedIndices();
            for(int index : indices){
                booksInTable.get(index).deleteFromFile();
            }
            bookView.getTableView().setItems(FXCollections.observableArrayList(Book.getBooks()));
            bookView.getResultLabel().setText("Books deleted successfully!");
        });
    }
    private void setSaveListener(){
        bookView.getSaveBtn().setOnAction(e -> {
            String isbn = bookView.getIsbnField().getText();
            String title = bookView.getTitleField().getText();
            float purchasedPrice = Float.parseFloat(bookView.getPurchasedPriceField().getText());
            float sellingPrice = Float.parseFloat(bookView.getSellingPriceField().getText());
            Author author = bookView.getAuthorsComboBox().getValue();
            int count = Integer.parseInt(bookView.getCountField().getText());
            Book book = new Book(isbn, title, purchasedPrice, sellingPrice, author, count);
            if(book.SaveInFile()){
                bookView.getTableView().getItems().add(book);
                bookView.getResultLabel().setText("Book created successfully");
                resetFields();
            }else{
                bookView.getResultLabel().setText("Book creation failed!");
            }
        });
    }

    private Author findAuthor(String fullName){
        for(Author author : Author.getAuthors()){
            if(author.getFullName().equals(fullName)){
                return author;
            }
        }
        return null;
    }
    private void resetFields(){
        bookView.getIsbnField().setText("");
        bookView.getTitleField().setText("");
        bookView.getPurchasedPriceField().setText("");
        bookView.getSellingPriceCol().setText("");
    }
}
