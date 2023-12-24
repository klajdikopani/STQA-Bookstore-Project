package com.example.bookstorehenrihatija.models;

import com.example.bookstorehenrihatija.auxiliaries.Config;
import com.example.bookstorehenrihatija.auxiliaries.FileHandler;
import com.example.bookstorehenrihatija.auxiliaries.Handler;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.SimpleTimeZone;

public class Book extends BaseModel implements Serializable {
    private String isbn;
    private String title;
    private float purchasedPrice;
    private float sellingPrice;
    private Author author;
    private int count;


    public static final String FILE_PATH = new Config().getProperty("filepath") + "/books.ser";
    public static final File DATA_FILE = new File(FILE_PATH);
    @Serial
    private static final long serialVersionUID = 1234567L;
    private static final ArrayList<Book> books = new ArrayList<>();
    public Book(){}
    public Book(String isbn, String title, float purchasedPrice, float sellingPrice, Author author, int count){
        this.isbn = isbn;
        this.title = title;
        this.purchasedPrice = purchasedPrice;
        this.sellingPrice = sellingPrice;
        this.author = author;
        this.count = count;
    }

    public float getPurchasedPrice(){ return  purchasedPrice; }
    public void setPurchasedPrice(float purchasedPrice) throws IllegalArgumentException{
        if(purchasedPrice < 0)
            throw new IllegalArgumentException("Price cannot be negative");
        this.purchasedPrice = purchasedPrice;
    }
    public float getSellingPrice(){return sellingPrice; }
    public void setSellingPrice(float sellingPrice) throws IllegalArgumentException{
        if(sellingPrice < 0)
            throw new IllegalArgumentException("Price cannot be negative");
        this.sellingPrice = sellingPrice;
    }
    public String getIsbn(){ return isbn; }
    public void setIsbn (String isbn) throws IllegalArgumentException{
        if(isbn.length() != 13)
            throw new IllegalArgumentException("ISBN must be 13 characters long");
        this.isbn = isbn;
    }
    public String getTitle(){ return title; }
    public void setTitle(String title) throws IllegalArgumentException{
        if(title.length() == 0)
            throw new IllegalArgumentException("Title must have at least one character");
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }
    public void setAuthor(Author author){ this.author = author; }
    public int getCount(){ return count; }
    public void setCount(int count) throws IllegalArgumentException{
        if(count < 0)
            throw new IllegalArgumentException("Count cannot be negative");
        this.count = count;
    }

    public static ArrayList<Book> getSearchResults(String searchText){
        if(searchText == null)
            return new ArrayList<Book>();
        if(searchText.equals(""))
            return getBooks();
        ArrayList<Book> searchResults = new ArrayList<>();
        for(Book book : Book.getBooks()){
            if(book.getTitle().equals(searchText)){
                searchResults.add(book);
            }
        }
        return searchResults;
    }

    @Override
    public boolean SaveInFile() {
        boolean saved = super.save(DATA_FILE);
        if(saved)
            books.add(this);
        return saved;
    }

    @Override
    public boolean isValid() {
        return isbn.length() == 13 &&
                title.length() > 0 &&
                purchasedPrice > 0 &&
                sellingPrice > 0 &&
                author != null;
    }

    @Override
    public boolean deleteFromFile() {
        books.remove(this);
        try{
            if(throwError)
                throw new IOException("mock");
            FileHandler.overwriteCurrentListToFile(DATA_FILE, books);
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static ArrayList<Book> getBooks(){
        if(books.size() == 0){
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(FILE_PATH))){
                ;
                while (true) {
                    Book temp = (Book) inputStream.readObject();
                    if (temp != null) {
                        books.add(temp);
                    } else
                        break;
                }
            }catch (EOFException eofException){
                System.out.println("End of book file reached!");
            }catch(IOException | ClassNotFoundException ex){
                ex.printStackTrace();
            }
        }
        return books;
    }
    @Override
    public boolean equals(Object o){
        if(this == o)
            return true;
        if((o instanceof Book)) {
            Book temp = (Book) o;
            return this.isbn.equals(temp.getIsbn()) &&
                    this.purchasedPrice == temp.getPurchasedPrice() &&
                this.sellingPrice == temp.getSellingPrice() &&
                this.title.equals(temp.getTitle()) &&
                this.count == temp.getCount() &&
                this.author.equals(temp.getAuthor());
        }
        return false;
    }
    @Override
    public String toString(){
        return isbn + " " + title + " " + purchasedPrice + " " + sellingPrice + " " + count + " " + author.getFullName();

    }
}
