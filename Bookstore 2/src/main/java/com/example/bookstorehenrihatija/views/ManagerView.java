package com.example.bookstorehenrihatija.views;

import com.example.bookstorehenrihatija.controllers.ManagerController;
import com.example.bookstorehenrihatija.models.Book;
import com.example.bookstorehenrihatija.models.BookOrder;
import javafx.collections.FXCollections;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.time.LocalDateTime;

public class ManagerView extends View{
    private final BorderPane borderPane = new BorderPane();
    private final TableView<BookOrder> tableView = new TableView<>();
    private final TableColumn<BookOrder, String> userCol = new TableColumn<>("User");
    private final TableColumn<BookOrder, String> titleCol = new TableColumn<>("Book");
    private final TableColumn<BookOrder, Integer> countCol = new TableColumn<>("Count");
    private final TableColumn<BookOrder, Float> amountCol = new TableColumn<>("Amount");
    private final TableColumn<BookOrder, Float> profitCol = new TableColumn<>("Profit");
    private final TableColumn<BookOrder, LocalDateTime> dateCol = new TableColumn<>("Date");
    private final SearchView searchView = new SearchView("Search for a user: ");
    private final TabPane tabPane = new TabPane();
    private final Tab dailyTab = new Tab("Daily");
    private final Tab weeklyTab = new Tab("Weekly");
    private final Tab monthlyTab = new Tab("Monthly");
    private final BorderPane totalPane = new BorderPane();

    public BorderPane getTotalPane() {
        return totalPane;
    }

    public Tab getDailyTab(){ return dailyTab; }

    public Tab getWeeklyTab() {
        return weeklyTab;
    }

    public Tab getMonthlyTab() {
        return monthlyTab;
    }

    public TableView<BookOrder> getTableView(){ return tableView; }
    public SearchView getSearchView(){ return searchView; }

    public ManagerView(){
        new ManagerController(this);
        setTable();
        setStatistics();
    }

    private void setStatistics(){
        tabPane.getTabs().addAll(monthlyTab, weeklyTab, dailyTab);
    }

    private void setTable(){
        tableView.setEditable(false);
        tableView.setItems(FXCollections.observableArrayList(BookOrder.getOrders()));
        userCol.setCellValueFactory(
                new PropertyValueFactory<>("userName")
        );
        titleCol.setCellValueFactory(
                new PropertyValueFactory<>("bookTitle")
        );
        countCol.setCellValueFactory(
                new PropertyValueFactory<>("count")
        );
        amountCol.setCellValueFactory(
                new PropertyValueFactory<>("amount")
        );
        profitCol.setCellValueFactory(
                new PropertyValueFactory<>("profit")
        );
        dateCol.setCellValueFactory(
                new PropertyValueFactory<>("timeCreated")
        );
        tableView.getColumns().addAll(userCol, titleCol, countCol, amountCol, profitCol, dateCol);
    }
    @Override
    public Parent getView() {
        HBox hbox = new HBox();
        hbox.getChildren().addAll(tableView,tabPane,totalPane);
        borderPane.setCenter(hbox);
        borderPane.setTop(searchView.getSearchPane());
        return borderPane;
    }
}
