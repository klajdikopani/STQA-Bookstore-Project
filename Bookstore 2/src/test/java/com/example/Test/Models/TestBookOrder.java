package com.example.Test.Models;

import com.example.bookstorehenrihatija.auxiliaries.Config;
import com.example.bookstorehenrihatija.models.Author;
import com.example.bookstorehenrihatija.models.Book;
import com.example.bookstorehenrihatija.models.BookOrder;
import com.example.bookstorehenrihatija.models.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TestBookOrder {
    static String filepath;
    LocalDateTime time;
    BookOrder order;
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
    void initOrder(){
        book = new Book("1111111222222",
                "Tom Sawyer",
                20, 30,
                new Author("J.K.", "Rowling"), 50);
        time = LocalDateTime.now();
        order = new BookOrder(new User("username", "password"),
                        book, 20, time);
    }

    @AfterEach
    void deleteOrders(){
        ArrayList<BookOrder> allOrders = BookOrder.getOrders();
        int count = allOrders.size();
        for(int i = 0; i < count; i++){
            allOrders.remove(0);
        }
        deleteFiles();

    }
    @AfterAll
    static void tearDown(){
        Config config = new Config();
        config.setProperty("filepath", filepath);

    }

    void deleteFiles(){
        File[] files = tempDir.listFiles();
        for (File f : files) {
            if(f.isDirectory())
                for(File x : f.listFiles()) {
                    x.delete();
                }
            f.delete();
        }
    }
    @Test
    void getUser() {
        Assertions.assertEquals(new User("username", "password"), order.getUser());
    }

    @Test
    void getUserName() {
        Assertions.assertEquals("username", order.getUserName());
    }

    @Test
    void getBookTitle() {
        Assertions.assertEquals("Tom Sawyer", order.getBookTitle());
    }

    @Test
    void getBook() {
        Assertions.assertEquals(new Book("1111111222222",
                "Tom Sawyer",
                20, 30,
                new Author("J.K.", "Rowling"), 50), order.getBook());

    }

    @Test
    void getCount() {
        Assertions.assertEquals(20, order.getCount());
    }

    @Test
    void getAmount() {
        Assertions.assertEquals(600, order.getAmount());
    }

    @Test
    void getProfit() {
        Assertions.assertEquals(200, order.getProfit());
    }

    @Test
    void getTimeCreated() {
        Assertions.assertEquals(time, order.getTimeCreated());
    }

    @Test
    void saveInFile() {
        order.SaveInFile();
        Assertions.assertTrue(BookOrder.getOrders().contains(order));
    }

    @Test
    void isValid() {
        Assertions.assertAll(
                ()->{
                    BookOrder invalidOrder = new BookOrder(null,
                            new Book(), 20, time);
                    Assertions.assertFalse(invalidOrder.isValid());
                },
                ()->{
                    BookOrder invalidOrder = new BookOrder(new User("username", "password"),
                            null, 20, time);
                    Assertions.assertFalse(invalidOrder.isValid());
                },
                ()->{
                    BookOrder invalidOrder =  new BookOrder(new User("username", "password"),
                            new Book(), -10, time);
                    Assertions.assertFalse(invalidOrder.isValid());
                },
                ()->{
                    Assertions.assertTrue(order.isValid());
                }

        );
    }

    @Test
    void deleteFromFile() {
        Assertions.assertAll(
                ()->{
                    ArrayList<BookOrder> allOrders = BookOrder.getOrders();
                    allOrders.add(order);
                    Assertions.assertTrue(order.deleteFromFile());
                    Assertions.assertFalse(allOrders.contains(order));
                },
                ()->{
                    order.setThrowError(true);
                    Assertions.assertFalse(order.deleteFromFile());
                    order.setThrowError(false);
                }
        );
    }

    @Test
    void writeBill() {
        Assertions.assertAll(
                ()->{
                    BookOrder invalidOrder = new BookOrder(null,
                            new Book(), 20, time);
                    Assertions.assertThrows(IllegalArgumentException.class, ()->invalidOrder.writeBill());
                },
                ()->{
                    BookOrder invalidOrder = new BookOrder(new User("username", "password"),
                            null, 20, time);
                    Assertions.assertThrows(IllegalArgumentException.class, ()->invalidOrder.writeBill());
                },
                ()->{
                    BookOrder invalidOrder =  new BookOrder(new User("username", "password"),
                            new Book(), -10, time);
                    Assertions.assertThrows(IllegalArgumentException.class, ()->invalidOrder.writeBill());
                },
                ()->{
                    order.setThrowError(true);
                    Assertions.assertFalse(order.writeBill());
                    order.setThrowError(false);
                },
                ()->{
                    Assertions.assertTrue(order.writeBill());
                }

        );

    }

    @Test
    void compareTo() {
        Assertions.assertAll(
                ()->{
                    LocalDateTime newTime = LocalDateTime.now();
                    BookOrder newOrder = new BookOrder(new User("user", "pass"),
                            book, 20, newTime);
                    Assertions.assertEquals(time.compareTo(newTime), order.compareTo(newOrder));
                },
                ()->{
                    Assertions.assertTrue(order.compareTo(order) == 0);
                }
        );
    }

    @Test
    void testEquals() {
        Assertions.assertAll(
                ()->{
                    BookOrder newOrder = new BookOrder(new User("newuser", "pass"),
                            book, 20, time);
                    Assertions.assertFalse(order.equals(newOrder));
                },
                ()->{
                    Book newBook = new Book("1111222222222",
                            "Different book",
                            20, 30,
                            new Author("J.K.", "Rowling"), 50);
                    BookOrder newOrder = new BookOrder(new User("username", "password"),
                            newBook, 20, time);
                    Assertions.assertFalse(order.equals(newOrder));
                },
                ()->{
                    LocalDateTime newTime = LocalDateTime.now();
                    BookOrder newOrder = new BookOrder(new User("username", "password"),
                            book, 20, newTime);
                    Assertions.assertFalse(order.equals(newOrder));
                },
                ()->{
                    BookOrder newOrder = new BookOrder(new User("username", "password"),
                            book, 30, time);
                    Assertions.assertFalse(order.equals(newOrder));
                },
                ()->{
                    Assertions.assertTrue(order.equals(order));
                },
                ()->{
                    BookOrder newOrder = new BookOrder(new User("username", "password"),
                            book, 20, time);
                    Assertions.assertTrue(order.equals(newOrder));
                },
                ()->{
                    Assertions.assertFalse(order.equals(new String(" ")));
                }
        );
    }
    @Test
    void test_getOrders(){
        BookOrder order1 = new BookOrder(new User("user1", "password"),
                book, 20, time);
        BookOrder order2 = new BookOrder(new User("user2", "password"),
                book, 20, time);
        BookOrder order3 = new BookOrder(new User("user3", "password"),
                book, 20, time);
        order1.SaveInFile();
        order2.SaveInFile();
        order3.SaveInFile();
        ArrayList<BookOrder> savedOrders = new ArrayList<>();
        savedOrders.add(order1);
        savedOrders.add(order2);
        savedOrders.add(order3);
        Assertions.assertAll(
                ()->{
                    ArrayList<BookOrder> result = BookOrder.getOrders();
                    for(BookOrder x : savedOrders)
                        Assertions.assertTrue(result.contains(x));
                },
                ()->{
                    ArrayList<BookOrder> allOrders = BookOrder.getOrders();
                    int count = allOrders.size();
                    for(int i = 0; i < count; i++){
                        allOrders.remove(0);
                    }
                    ArrayList<BookOrder> result = BookOrder.getOrders();
                    for(BookOrder x : savedOrders)
                        Assertions.assertTrue(result.contains(x));
                },
                ()->{
                    deleteOrders();
                    System.out.println(BookOrder.getOrders().size());
                    Assertions.assertTrue(BookOrder.getOrders().size() == 0);
                }
        );
    }
}