package com.example.Test.Integration;

import com.example.bookstorehenrihatija.auxiliaries.Config;
import com.example.bookstorehenrihatija.models.Author;
import com.example.bookstorehenrihatija.models.Book;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class TestBookIntegration {
    static String filepath;
    Book book;
    @TempDir
    static File tempDir;
    @BeforeAll
    static void setUp(){
        Config config = new Config();
        filepath = config.getProperty("filepath");
        config.setProperty("filepath", tempDir.getPath());
    }

    @BeforeEach
    void initBook() throws IOException {
        book = new Book();
        book.setIsbn("1111111111111");
        book.setTitle("Harry Potter");
        book.setPurchasedPrice(20);
        book.setSellingPrice(30);
        book.setAuthor(new Author("J.K.", "Rowling"));
        book.setCount(50);
        if(!Book.DATA_FILE.exists()) {
            try {
                Book.DATA_FILE.createNewFile();
            } catch (IOException e) {
                System.out.println(Book.DATA_FILE.getPath());
                e.printStackTrace();
                throw new IOException();
            }
        }
    }

    @AfterEach
    void deleteBooks(){
        ArrayList<Book> allBooks = Book.getBooks();
        int count = allBooks.size();
        for(int i = 0; i < count; i++){
            allBooks.remove(0);
        }
        deleteFiles();

    }
    @AfterAll
    static void tearDown(){
        Config config = new Config();
        config.setProperty("filepath", filepath);
        deleteFiles();
    }

    static void deleteFiles(){
        if(Book.DATA_FILE.delete())
            System.out.println("deleted");
    }

    @Test
    void test_SaveInFile(){
        Assertions.assertAll(
                ()->{
                    book.SaveInFile();
                    try(ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(Book.DATA_FILE))){
                        Book savedBook = (Book)inputStream.readObject();
                        Assertions.assertEquals(book, savedBook);
                        Assertions.assertEquals(Book.getBooks().get(0), savedBook);
                        Assertions.assertEquals(Book.getBooks().get(0), book);
                    }catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                },
                ()->{
                    Book notValidBook = new Book("11", book.getTitle(), book.getPurchasedPrice(),
                            book.getSellingPrice(), book.getAuthor(), book.getCount());
                    Assertions.assertFalse(notValidBook.SaveInFile());
                }
        );
    }

    @Test
    void test_deleteFromFile(){
        Book book1 = new Book("1111111111111", "Oliver Twist", book.getPurchasedPrice(),
                book.getSellingPrice(), book.getAuthor(), book.getCount());
        Book book2 = new Book("1111111111111", "Harry Potter", book.getPurchasedPrice(),
                book.getSellingPrice(), book.getAuthor(), book.getCount());
        Book book3 = new Book("1111111111111", "Mary Poppins", book.getPurchasedPrice(),
                book.getSellingPrice(), book.getAuthor(), book.getCount());
        book1.SaveInFile();
        book2.SaveInFile();
        book3.SaveInFile();
        book1.deleteFromFile();
        Assertions.assertAll(
                ()->{
                    ArrayList<Book> allBooks = Book.getBooks();
                    Assertions.assertTrue(allBooks.contains(book2));
                    Assertions.assertTrue(allBooks.contains(book3));
                    Assertions.assertFalse(allBooks.contains(book1));
                },
                ()->{
                    ArrayList<Book> allBooks = Book.getBooks();
                    int count = allBooks.size();
                    for(int i = 0; i < count; i++){
                        allBooks.remove(0);
                    }
                    allBooks = Book.getBooks();
                    Assertions.assertTrue(allBooks.contains(book2));
                    Assertions.assertTrue(allBooks.contains(book3));
                    Assertions.assertFalse(allBooks.contains(book1));
                }
        );
    }
}
