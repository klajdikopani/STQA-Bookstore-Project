package com.example.Test.Integration;

import com.example.bookstorehenrihatija.auxiliaries.Config;
import com.example.bookstorehenrihatija.models.Author;
import com.example.bookstorehenrihatija.models.Book;
import com.example.bookstorehenrihatija.models.BookOrder;
import com.example.bookstorehenrihatija.models.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class TestBookOrderIntegration {
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
    void test_SaveInFile(){
        Assertions.assertAll(
                ()->{
                    order.SaveInFile();
                    try(ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(BookOrder.DATA_FILE))){
                        BookOrder savedOrder = (BookOrder) inputStream.readObject();
                        Assertions.assertEquals(order, savedOrder);
                        Assertions.assertEquals(BookOrder.getOrders().get(0), savedOrder);
                        Assertions.assertEquals(BookOrder.getOrders().get(0), order);
                    }catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                },
                ()->{
                    BookOrder notValidOrder = new BookOrder(order.getUser(), order.getBook(), -2, order.getTimeCreated());
                    Assertions.assertFalse(notValidOrder.SaveInFile());
                }
        );
    }

    @Test
    void test_deleteFromFile(){
        BookOrder order1 = new BookOrder(order.getUser(), order.getBook(), 10, order.getTimeCreated());
        BookOrder order2 = new BookOrder(order.getUser(), order.getBook(), 20, order.getTimeCreated());
        BookOrder order3 = new BookOrder(order.getUser(), order.getBook(), 30, order.getTimeCreated());
        order1.SaveInFile();
        order2.SaveInFile();
        order3.SaveInFile();
        order1.deleteFromFile();
        Assertions.assertAll(
                ()->{
                    ArrayList<BookOrder> allOrders = BookOrder.getOrders();
                    Assertions.assertTrue(allOrders.contains(order2));
                    Assertions.assertTrue(allOrders.contains(order3));
                    Assertions.assertFalse(allOrders.contains(order1));
                },
                ()->{
                    ArrayList<BookOrder> allOrders = BookOrder.getOrders();
                    int count = allOrders.size();
                    for(int i = 0; i < count; i++){
                        allOrders.remove(0);
                    }
                    allOrders = BookOrder.getOrders();
                    Assertions.assertTrue(allOrders.contains(order2));
                    Assertions.assertTrue(allOrders.contains(order3));
                    Assertions.assertFalse(allOrders.contains(order1));
                }
        );
    }

    @Test
    void test_writeBill(){
        Assertions.assertAll(
                ()->{
                    order.SaveInFile();
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy_hh-mm-ss");
                    String filename = order.getUser().getUsername() + "-"
                            + book.getTitle().replaceAll(" ","") + "_"
                            + order.getTimeCreated().format(dateTimeFormatter) + ".txt";
                    String fullFilePath = BookOrder.BILL_DIRECTORY_PATH + filename;
                    order.writeBill();
                    String actualBill;
                    File orderFile = new File(fullFilePath);
                    try(Scanner sc = new Scanner(orderFile)){
                        StringBuilder sb = new StringBuilder();
                        while(sc.hasNextLine()){
                            sb.append(sc.nextLine());
                            sb.append('\n');
                        }
                        actualBill = sb.toString();
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    String expectedBill = "User: " + order.getUser().getUsername() + "\n"
                            + "Book Title: " + order.getBook().getTitle() + "\n"
                            + "Copies Sold: " + order.getCount() + "\n"
                            + "Amount: " + order.getBook().getSellingPrice() * order.getCount() + "$\n"
                            + "Profit: " + (order.getBook().getSellingPrice() - order.getBook().getPurchasedPrice()) * order.getCount() + "$\n"
                            + "Time of Transaction: " + order.getTimeCreated().format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss")) + "\n";
                    Assertions.assertEquals(expectedBill, actualBill);
                },
                ()->{
                    BookOrder notValidOrder = new BookOrder(order.getUser(), order.getBook(), -5, order.getTimeCreated());
                    Assertions.assertThrows(IllegalArgumentException.class, ()->notValidOrder.writeBill());
                }
        );

    }
}
