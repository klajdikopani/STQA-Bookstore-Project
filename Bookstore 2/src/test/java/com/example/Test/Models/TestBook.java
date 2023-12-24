package com.example.Test.Models;

import com.example.bookstorehenrihatija.auxiliaries.Config;
import com.example.bookstorehenrihatija.auxiliaries.FileHandler;
import com.example.bookstorehenrihatija.auxiliaries.Handler;
import com.example.bookstorehenrihatija.models.Author;
import com.example.bookstorehenrihatija.models.BaseModel;
import com.example.bookstorehenrihatija.models.Book;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TestBook {
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
    void test_getPurchasedPrice() {
        Assertions.assertEquals(book.getPurchasedPrice(), 20);
    }

    @Test
    void test_setPurchasedPrice() {
        Assertions.assertAll(
                ()->{
                    Throwable throwable = Assertions.assertThrows(IllegalArgumentException.class, ()->book.setPurchasedPrice(-10));
                    Assertions.assertEquals("Price cannot be negative", throwable.getMessage());
                },
                ()->{
                    book.setPurchasedPrice(10);
                    Assertions.assertEquals(10, book.getPurchasedPrice());
                }
        );

    }

    @Test
    void test_getSellingPrice() {
        Assertions.assertEquals(book.getSellingPrice(), 30);
    }

    @Test
    void test_setSellingPrice() {
        Assertions.assertAll(
                ()->{
                    Throwable throwable = Assertions.assertThrows(IllegalArgumentException.class, ()->book.setSellingPrice(-20));
                    Assertions.assertEquals("Price cannot be negative", throwable.getMessage());
                },
                ()->{
                    book.setSellingPrice(90);
                    Assertions.assertEquals(90, book.getSellingPrice());
                }
        );

    }

    @Test
    void test_getIsbn() {
        Assertions.assertEquals("1111111111111", book.getIsbn());
    }

    @Test
    void test_setIsbn() {
        Assertions.assertAll(
                ()->{
                    Throwable throwable = Assertions.assertThrows(IllegalArgumentException.class, ()->book.setIsbn("1111"));
                    Assertions.assertEquals("ISBN must be 13 characters long", throwable.getMessage());
                },
                ()->{
                    Throwable throwable = Assertions.assertThrows(IllegalArgumentException.class, ()->book.setIsbn("111111111111111111111111"));
                    Assertions.assertEquals("ISBN must be 13 characters long", throwable.getMessage());
                },
                ()->{
                    book.setIsbn("2222222222222");
                    Assertions.assertEquals("2222222222222", book.getIsbn());
                }
        );
    }

    @Test
    void test_getTitle() {
        Assertions.assertEquals("Harry Potter", book.getTitle());
    }

    @Test
    void test_setTitle() {
        Assertions.assertAll(
                ()->{
                    Throwable throwable = Assertions.assertThrows(IllegalArgumentException.class, ()->book.setTitle(""));
                    Assertions.assertEquals("Title must have at least one character", throwable.getMessage());
                },
                ()->{
                    book.setTitle("Mary Poppins");
                    Assertions.assertEquals("Mary Poppins", book.getTitle());
                }
        );
    }

    @Test
    void test_getAuthor() {
        Assertions.assertEquals(new Author("J.K.", "Rowling"), book.getAuthor());
    }

    @Test
    void test_setAuthor() {
        Author author = new Author("Oscar", "Wilde");
        book.setAuthor(author);
        Assertions.assertEquals(author, book.getAuthor());
    }

    @Test
    void test_getCount() {
        Assertions.assertEquals(50, book.getCount());
    }

    @Test
    void test_setCount() {
        Assertions.assertAll(
                ()->{
                    Throwable exception = Assertions.assertThrows(IllegalArgumentException.class, ()->book.setCount(-10));
                    Assertions.assertEquals("Count cannot be negative", exception.getMessage());
                },
                ()->{
                    book.setCount(90);
                    Assertions.assertEquals(90, book.getCount());
                }
        );
    }

    @Test
    void test_getSearchResults() {
        Book book = new Book("1111111222222",
                "Tom Sawyer",
                20, 30,
                new Author("J.K.", "Rowling"), 50);
        Book book2 = new Book("1111111333333",
                "Oliver Twist",
                20, 30,
                new Author("J.K.", "Rowling"), 50);
        Book book3 = new Book("1111111444444",
                "Heidi",
                20, 30,
                new Author("J.K.", "Rowling"), 50);
        ArrayList<Book> books = Book.getBooks();
        books.add(book);
        books.add(book2);
        books.add(book3);
        Assertions.assertAll(
                ()->{
                    ArrayList<Book> searchedBooks = Book.getSearchResults("Heidi");
                    for(Book x : searchedBooks)
                        Assertions.assertEquals(x, book3);
                },
                ()->{
                    ArrayList<Book> searchedBooks = Book.getSearchResults("Harry Potter");
                    Assertions.assertTrue(searchedBooks.size() == 0);
                },
                ()->{
                    ArrayList<Book> searchedBooks = Book.getSearchResults("");
                    Assertions.assertTrue(searchedBooks == Book.getBooks());
                },
                ()->{
                    ArrayList<Book> searchedBooks = Book.getSearchResults(null);
                    Assertions.assertTrue(searchedBooks.size() == 0);
                }
        );
    }

    @Test
    void test_saveInFile() {
        Assertions.assertAll(
                ()->{
                    Book invalidBook = new Book("1111111222222",
                            "Tom Sawyer",
                            20, 30,
                            null, 50);
                    Assertions.assertFalse(invalidBook.SaveInFile());
                    Assertions.assertFalse(Book.getBooks().contains(invalidBook));
                },
                ()->{
                    Assertions.assertTrue(book.SaveInFile());
                    ArrayList<Book> books = Book.getBooks();
                    Assertions.assertTrue(books.contains(book));
                }
        );


    }

    @ParameterizedTest
    @CsvSource({
            "1111111,Harry Potter,J.K.,Rowling,20,30,50",
            "1111111,,J.K.,Rowling,20,30,50",
            "1111111,Harry Potter,J.K.,Rowling,-10,30,50",
            "1111111,Harry Potter,J.K.,Rowling,20,-30,50",
            "11111111111111,Harry Potter,J.K.,Rowling,20,30,50"
    })
    void test_isValid_notValid(String isbn, String title, String firstName, String lastName, String purchasedPrice, String sellingPrice, String count){
        Book book = new Book(isbn, title, Float.parseFloat(purchasedPrice)
                , Float.parseFloat(sellingPrice), new Author(firstName, lastName), Integer.parseInt(count));
        Assertions.assertFalse(book.isValid());
    }
    @Test
    void test_isValid_valid(){
        Book book = new Book("1111111222222",
                "Tom Sawyer",
                20, 30,
                new Author("J.K.", "Rowling"), 50);
        Assertions.assertTrue(book.isValid());
    }
    @Test
    void test_isValid_nullAuthor(){
        Book book = new Book("1111111222222",
                "Tom Sawyer",
                20, 30,
                null, 50);
        Assertions.assertFalse(book.isValid());
    }



    @Test
    void test_deleteFromFile() {
        Assertions.assertAll(
                ()->{
                    ArrayList<Book> books  = Book.getBooks();
                    books.add(book);
                    Assertions.assertTrue(book.deleteFromFile());
                    Assertions.assertFalse(books.contains(book));
                },
                ()->{
                    book.setThrowError(true);
                    Assertions.assertFalse(book.deleteFromFile());
                    book.setThrowError(false);
                }
        );
    }

    @ParameterizedTest
    @CsvSource({
            "1111111222222,Harry Potter,J.K.,Rowling,20,30,50,false",
            "1111111111111,Oliver Twist,J.K.,Rowling,20,30,50,false",
            "1111111111111,Harry Potter,Oscar,Wilde,20,30,50,false",
            "1111111111111,Harry Potter,J.K.,Rowling,10,30,50,false",
            "1111111111111,Harry Potter,J.K.,Rowling,20,70,50,false",
            "1111111111111,Harry Potter,J.K.,Rowling,20,30,35,false",
            "1111111111111,Harry Potter,J.K.,Rowling,20,30,50,true"
    })
    void test_Equals(String isbn, String title, String firstname, String lastname, String purchasedPrice, String sellingPrice, String count, String expected) {
        Book testBook = new Book(isbn, title, Float.parseFloat(purchasedPrice),
                Float.parseFloat(sellingPrice), new Author(firstname, lastname),
                Integer.parseInt(count));
        Assertions.assertEquals(Boolean.parseBoolean(expected), book.equals(testBook));
    }

    @Test
    void test_equals_notBook(){
        String notABook = "Not a Book!";
        Assertions.assertFalse(book.equals(notABook));
    }

    @Test
    void test_equals_sameReference(){
        Assertions.assertTrue(book.equals(book));
    }
    @Test
    void test_ToString() {

        String expected = "1111111111111 Harry Potter 20.0 30.0 50 J.K. Rowling";
        Assertions.assertEquals(expected, book.toString());
    }
    @Test
    void test_getBooks(){
        Book book = new Book("1111111222222",
                "Tom Sawyer",
                20, 30,
                new Author("J.K.", "Rowling"), 50);
        Book book2 = new Book("1111111333333",
                "Oliver Twist",
                20, 30,
                new Author("J.K.", "Rowling"), 50);
        Book book3 = new Book("1111111444444",
                "Heidi",
                20, 30,
                new Author("J.K.", "Rowling"), 50);
        book.SaveInFile();
        book2.SaveInFile();
        book3.SaveInFile();
        ArrayList<Book> savedBooks = new ArrayList<>();
        savedBooks.add(book);
        savedBooks.add(book2);
        savedBooks.add(book3);
        Assertions.assertAll(
                ()->{
                    ArrayList<Book> result = Book.getBooks();
                    for(Book x : savedBooks)
                        Assertions.assertTrue(result.contains(x));
                },
                ()->{
                    ArrayList<Book> allBooks = Book.getBooks();
                    int count = allBooks.size();
                    for(int i = 0; i < count; i++){
                        allBooks.remove(0);
                    }
                    ArrayList<Book> result = Book.getBooks();
                    for(Book x : savedBooks)
                        Assertions.assertTrue(result.contains(x));
                },
                ()->{
                    deleteBooks();
                    if(!Book.DATA_FILE.exists()) {
                        try {
                            Book.DATA_FILE.createNewFile();
                        } catch (IOException e) {
                            System.out.println(Book.DATA_FILE.getPath());
                            e.printStackTrace();
                            throw new IOException();
                        }
                    }
                    Assertions.assertTrue(Book.getBooks().size() == 0);
                }
        );
    }


}