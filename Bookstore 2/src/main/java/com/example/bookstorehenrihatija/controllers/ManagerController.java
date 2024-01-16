package com.example.bookstorehenrihatija.controllers;

import com.example.bookstorehenrihatija.models.Book;
import com.example.bookstorehenrihatija.models.BookOrder;
import com.example.bookstorehenrihatija.views.ManagerView;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class ManagerController {
    private ManagerView view;
    public ManagerController(ManagerView view){
        this.view = view;
        setDailyTab();
        setWeeklyTab();
        setMonthlyTab();
        setSearhForm();
        setTotalPane();
    }
    private void setTotalPane(){
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setPadding(new Insets(20));
        vBox.setSpacing(15);
        int count = 0;
        float amount = 0;
        float profit = 0;
        vBox.getChildren().add(new Label("Total Statistics"));
        for(BookOrder order : BookOrder.getOrders()){
            count += order.getCount();
            amount += order.getAmount();
            profit += order.getProfit();
        }
        vBox.getChildren().addAll(new Label("Number of copies sold: " + count),
                                    new Label("Total amount of revenue: " + amount),
                                    new Label("Total amount of profit: " + profit));
        view.getTotalPane().setCenter(vBox);
    }
    private void setSearhForm(){
        view.getSearchView().getSearchBtn().setOnAction(e -> {
            String searchQuery = view.getSearchView().getSearchField().getText();
            if(searchQuery.equals("")){
                view.getTableView().setItems(FXCollections.observableArrayList(BookOrder.getOrders()));
                return;
            }
            ArrayList<BookOrder> ordersQueried = new ArrayList<>();
            for(BookOrder order : BookOrder.getOrders()){
                if(order.getUser().getUsername().equals(searchQuery)){
                    ordersQueried.add(order);
                }
            }
            view.getTableView().setItems(FXCollections.observableArrayList(ordersQueried));
            view.getSearchView().getSearchField().setText("");
        });
        view.getSearchView().getClearBtn().setOnAction(e -> {
            view.getSearchView().getSearchField().setText("");
        });
        view.getSearchView().getClearBtn().setOnAction(e -> {
            view.getSearchView().getSearchField().setText("");
            BookOrder.getOrders().sort(Comparator.comparing(BookOrder::getTimeCreated).reversed());
            view.getTableView().setItems(FXCollections.observableArrayList(BookOrder.getOrders()));
        });
    }
    private void setMonthlyTab(){
        VBox vBox= new VBox();
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setPadding(new Insets(20));
        vBox.setSpacing(10);
        ArrayList<BookOrder> ordersSorted = BookOrder.getOrders();
        Collections.sort(ordersSorted, Collections.reverseOrder());
        LocalDateTime currentDate = ordersSorted.get(0).getTimeCreated();
        int count = 0;
        float amount = 0;
        float profit = 0;
        for(BookOrder order : ordersSorted){
            if(!order.getTimeCreated().getMonth().equals(currentDate.getMonth())){
                vBox.getChildren().add(new Label(currentDate.getMonth().toString() + " "
                                                + currentDate.getYear() +
                        "- Copies: " + count + " Amount: " + amount + " Profit: " + profit));
                currentDate = order.getTimeCreated();
                count = 0;
                amount = 0;
                profit = 0;
            }
            count += order.getCount();
            amount += order.getAmount();
            profit += order.getProfit();
        }
        vBox.getChildren().add(new Label(currentDate.getMonth().toString() + " "
                + currentDate.getYear() +
                "- Copies: " + count + " Amount: " + amount + " Profit: " + profit));
        view.getMonthlyTab().setContent(vBox);
    }
    private void setWeeklyTab(){
        VBox vBox= new VBox();
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setPadding(new Insets(20));
        vBox.setSpacing(10);
        ArrayList<BookOrder> ordersSorted = BookOrder.getOrders();
        Collections.sort(ordersSorted, Collections.reverseOrder());
        LocalDateTime currentDate = ordersSorted.get(0).getTimeCreated();
        int count = 0;
        float amount = 0;
        float profit = 0;
        for(BookOrder order : ordersSorted){
            if(order.getTimeCreated().isBefore(currentDate.minusDays(7))){
                LocalDateTime temp = currentDate.minusDays(7);
                vBox.getChildren().add(new Label(  currentDate.getDayOfMonth() + " " +
                                            currentDate.getMonth().toString() + " " +
                                            currentDate.getYear() + " to " + temp.getDayOfMonth() + " " +
                                            temp.getMonth().toString() + " " +
                                            temp.getYear() +
                        "- Copies: " + count + " Amount: " + amount + " Profit: " + profit));
                currentDate = order.getTimeCreated();
                count = 0;
                amount = 0;
                profit = 0;
            }
            count += order.getCount();
            amount += order.getAmount();
            profit += order.getProfit();
        }
        LocalDateTime temp = currentDate.minusDays(7);
        vBox.getChildren().add(new Label(  currentDate.getDayOfMonth() + " " +
                currentDate.getMonth().toString() + " " +
                currentDate.getYear() + " to " + temp.getDayOfMonth() + " " +
                temp.getMonth().toString() + " " +
                temp.getYear() +
                "- Copies: " + count + " Amount: " + amount + " Profit: " + profit));
        view.getWeeklyTab().setContent(vBox);
    }
    private int getDayAWeekBefore(LocalDateTime time){
        if(time.getDayOfYear() > 7){
            return time.getDayOfYear() - 7;
        }else{
            return 365 - (7 - time.getDayOfYear());
        }
    }
    private void setDailyTab(){
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setPadding(new Insets(20));
        vBox.setSpacing(10);
        ArrayList<BookOrder> ordersSorted = BookOrder.getOrders();
        Collections.sort(ordersSorted, Collections.reverseOrder());
//        if(ordersSorted.size() > 0){
//            LocalDateTime currentDate = ordersSorted.get(0).getTimeCreated();
//        }
        LocalDateTime currentDate = ordersSorted.get(0).getTimeCreated();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-yyyy");
        int count = 0;
        float amount = 0;
        float profit = 0;

        for(BookOrder order : ordersSorted){
            if(currentDate.getDayOfYear() != order.getTimeCreated().getDayOfYear()){
                vBox.getChildren().add(new Label(currentDate.getMonth().toString()+ " "
                        + currentDate.getDayOfMonth() + " " + currentDate.getYear() +
                        "- Copies: " + count + " Amount: " + amount + " Profit: " + profit));
                currentDate = order.getTimeCreated();
                count = 0;
                amount = 0;
                profit = 0;
            }
            count += order.getCount();
            amount += order.getAmount();
            profit += order.getProfit();
        }
        vBox.getChildren().add(new Label(currentDate.getMonth().toString()+ " "
                + currentDate.getDayOfMonth() + " " + currentDate.getYear() +
                "- Copies: " + count + " Amount: " + amount + " Profit: " + profit));

        view.getDailyTab().setContent(vBox);
    }
}
