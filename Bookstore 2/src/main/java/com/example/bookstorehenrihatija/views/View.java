package com.example.bookstorehenrihatija.views;

import com.example.bookstorehenrihatija.models.User;
import javafx.scene.Parent;

public abstract class View {
    public static User currentUser = null;
    public static User getCurrentUser(){ return currentUser; }
    public static void setCurrentUser(User currentUser){ View.currentUser = currentUser; }
    public abstract Parent getView();
}
