package com.example.bookstorehenrihatija.models;

import com.example.bookstorehenrihatija.auxiliaries.Config;
import com.example.bookstorehenrihatija.auxiliaries.FileHandler;
import com.example.bookstorehenrihatija.auxiliaries.Handler;

import java.io.*;
import java.security.PublicKey;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;

public class BookOrder extends BaseModel implements Serializable, Comparable<BookOrder> {
    private User user;
    private String userName;
    private Book book;
    private String bookTitle;
    private int count;
    private float amount;
    private float profit;
    private LocalDateTime timeCreated;
    public static final String FILE_PATH = new Config().getProperty("filepath") + "/orders.ser";
    public static final File DATA_FILE = new File(FILE_PATH);
    public static final String BILL_DIRECTORY_PATH = new Config().getProperty("filepath") + "/bills/";

    public User getUser() {
        return user;
    }
    public String getUserName(){ return userName; }
    public String getBookTitle(){ return bookTitle; }

    public Book getBook() {
        return book;
    }

    public int getCount() {
        return count;
    }

    public float getAmount() {
        return amount;
    }

    public float getProfit() {
        return profit;
    }

    public LocalDateTime getTimeCreated(){
        return timeCreated;
    }

    public BookOrder(User user, Book book, int count, LocalDateTime time){
        this.user = user;
        if(user != null)
            this.userName = user.getUsername();
        this.book = book;
        this.count = count;
        timeCreated = time;
        if(book != null){
            amount = count * book.getSellingPrice();
            profit = amount - count * book.getPurchasedPrice();
            this.bookTitle = book.getTitle();
        }
        if(!(new File(BILL_DIRECTORY_PATH).exists()))
            new File(BILL_DIRECTORY_PATH).mkdir();
    }

    @Serial
    private static final long serialVersionUID = 1234567L;
    public static ArrayList<BookOrder> orders = new ArrayList<>();
    @Override
    public boolean SaveInFile() {
        boolean saved = super.save(DATA_FILE);
        if(saved)
            orders.add(this);
        return saved;
    }

    @Override
    public boolean isValid() {
        return user != null && book != null && count > 0;
    }

    @Override
    public boolean deleteFromFile() {
        orders.remove(this);
        try{
            if(throwError)
                throw new IOException("Mock");
            FileHandler.overwriteCurrentListToFile(DATA_FILE, orders);
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public static ArrayList<BookOrder> getOrders(){
        if(orders.size() == 0){

            try(ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(FILE_PATH));){
                while (true){
                    BookOrder temp = (BookOrder) inputStream.readObject();
                    if(temp != null){
                        orders.add(temp);
                    }else
                        break;
                }
            }catch (EOFException eofException){
                System.out.println("End of orders file reached!");
            }catch (IOException | ClassNotFoundException ex){
                ex.printStackTrace();
            }
        }
        return orders;
    }
    public boolean writeBill(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy_hh-mm-ss");
        if(!isValid())
            throw new IllegalArgumentException();
        String filename = user.getUsername() + "-"
                            + book.getTitle().replaceAll(" ","") + "_"
                            + timeCreated.format(dateTimeFormatter) + ".txt";
        String fullFilePath = BILL_DIRECTORY_PATH + filename;
        System.out.println(fullFilePath);
        try{
            if(throwError)
                throw new IOException("Mock");
            File billFile = new File(fullFilePath);
            billFile.createNewFile();
            FileWriter fileWriter = new FileWriter(billFile);
            String bill = "User: " + user.getUsername() + "\n"
                            + "Book Title: " + book.getTitle() + "\n"
                            + "Copies Sold: " + count + "\n"
                            + "Amount: " + book.getSellingPrice() * count + "$\n"
                            + "Profit: " + (book.getSellingPrice() - book.getPurchasedPrice()) * count + "$\n"
                            + "Time of Transaction: " + timeCreated.format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss"));
            fileWriter.write(bill);
            fileWriter.close();
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(BookOrder o) {
        return getTimeCreated().compareTo(o.getTimeCreated());
    }
    @Override
    public boolean equals(Object o){
        if(this == o)
            return true;
        if(o instanceof BookOrder){
            BookOrder temp = (BookOrder) o;
            return this.getUser().equals(temp.getUser()) &&
                    this.getBook().equals(temp.getBook()) &&
                    this.getTimeCreated().equals(temp.getTimeCreated()) &&
                    this.getCount() == temp.getCount();
        }
        return false;
    }
}
