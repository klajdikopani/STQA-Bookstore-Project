package com.example.Test.Integration;

import com.example.bookstorehenrihatija.auxiliaries.Config;
import com.example.bookstorehenrihatija.models.Role;
import com.example.bookstorehenrihatija.models.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class TestUserIntegration {
    static String filepath;
    User user;
    static File file;
    @TempDir
    static File tempDir;
    @BeforeAll
    static void setUp() throws IOException {
        Config config = new Config();
        filepath = config.getProperty("filepath");
        config.setProperty("filepath", tempDir.getPath());
    }

    @BeforeEach
    void initUsers(){
        user = new User("username", "password",
                "John", "Doe", "jdoe@gmail.com", 5000,
                Role.ADMIN);
        File file = new File(User.FILE_PATH);
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("init users exception");
            }
        }
    }

    @AfterEach
    void deleteUsers() throws IOException {
        ArrayList<User> allUsers = User.getUsers();
        int count = allUsers.size();
        for(int i = 0; i < count; i++){
            allUsers.remove(0);
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
        File[] files = tempDir.listFiles();
        for (File f : files) {
            if(!f.delete())
                try {
                    throw new Exception("No deletion");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
        }
    }

    @Test
    void test_SaveInFile(){
        Assertions.assertAll(
                ()->{
                    user.SaveInFile();
                    try(ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(User.DATA_FILE))){
                        User savedUser = (User) inputStream.readObject();
                        Assertions.assertEquals(user, savedUser);
                        Assertions.assertEquals(User.getUsers().get(0), savedUser);
                        Assertions.assertEquals(User.getUsers().get(0), user);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                },
                ()->{
                    User notValidUser = new User(user.getUsername(), user.getPassword(),
                            user.getFirstName(), user.getLastName(), user.getEmail(), -10000, Role.ADMIN);
                    Assertions.assertFalse(notValidUser.SaveInFile());
                }
        );
    }

    @Test
    void test_deleteFromFile(){
        User user1 = new User(user.getUsername(), user.getPassword(),
                user.getFirstName(), user.getLastName(), user.getEmail(), 10000, Role.ADMIN);
        User user2 = new User("user2", user.getPassword(),
                user.getFirstName(), user.getLastName(), user.getEmail(), 10000, Role.ADMIN);
        User user3 = new User("user3", user.getPassword(),
                user.getFirstName(), user.getLastName(), user.getEmail(), 10000, Role.ADMIN);
        user1.SaveInFile();
        user2.SaveInFile();
        user3.SaveInFile();
        user1.deleteFromFile();
        Assertions.assertAll(
                ()->{
                    ArrayList<User> allUsers = User.getUsers();
                    Assertions.assertTrue(allUsers.contains(user2));
                    Assertions.assertTrue(allUsers.contains(user3));
                    Assertions.assertFalse(allUsers.contains(user1));
                },
                ()->{
                    ArrayList<User> allUsers = User.getUsers();
                    int count = allUsers.size();
                    for(int i = 0; i < count; i++){
                        allUsers.remove(0);
                    }
                    allUsers = User.getUsers();
                    Assertions.assertTrue(allUsers.contains(user2));
                    Assertions.assertTrue(allUsers.contains(user3));
                    Assertions.assertFalse(allUsers.contains(user1));
                }
        );
    }
}
