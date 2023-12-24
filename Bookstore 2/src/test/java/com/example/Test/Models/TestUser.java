package com.example.Test.Models;

import com.example.bookstorehenrihatija.auxiliaries.Config;
import com.example.bookstorehenrihatija.models.Role;
import com.example.bookstorehenrihatija.models.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TestUser {
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
    void getFirstName() {
        Assertions.assertEquals("John", user.getFirstName());
    }

    @Test
    void setFirstName() {
        Assertions.assertAll(
                ()->{
                    Throwable exception = Assertions.assertThrows(IllegalArgumentException.class, ()->user.setFirstName(""));
                    Assertions.assertEquals("Name must have at least one character", exception.getMessage());
                },
                ()->{
                    user.setFirstName("Jake");
                    Assertions.assertEquals("Jake", user.getFirstName());
                }
        );
    }

    @Test
    void getLastName() {
        Assertions.assertEquals("Doe", user.getLastName());
    }

    @Test
    void setLastName() {
        Assertions.assertAll(
                ()->{
                    Throwable exception = Assertions.assertThrows(IllegalArgumentException.class, ()->user.setLastName(""));
                    Assertions.assertEquals("Name must have at least one character", exception.getMessage());
                },
                ()->{
                    user.setLastName("Smith");
                    Assertions.assertEquals("Smith", user.getLastName());
                }
        );
    }

    @Test
    void getEmail() {
        Assertions.assertEquals("jdoe@gmail.com", user.getEmail());
    }

    @Test
    void setEmail() {
        Assertions.assertAll(
                ()->{
                    Assertions.assertThrows(IllegalArgumentException.class, ()->user.setEmail(""));
                },
                ()->{
                    Assertions.assertThrows(IllegalArgumentException.class, ()->user.setEmail("johndoe"));
                },
                ()->{
                    user.setEmail("johndoe@gmail.com");
                    Assertions.assertEquals("johndoe@gmail.com", user.getEmail());
                }
        );
    }

    @Test
    void getSalary() {
        Assertions.assertEquals(5000.0, user.getSalary());
    }

    @Test
    void setSalary() {
        Assertions.assertAll(
                ()->{
                    Throwable exception = Assertions.assertThrows(IllegalArgumentException.class, ()->user.setSalary(-1000));
                    Assertions.assertEquals("Salary cannot be negative", exception.getMessage());
                },
                ()->{
                    user.setSalary(7000);
                    Assertions.assertEquals(7000.0, user.getSalary());
                }
        );
    }

    @Test
    void getUsername() {
        Assertions.assertEquals("username", user.getUsername());
    }

    @Test
    void setUsername() {
        Assertions.assertAll(
                ()->{
                    Assertions.assertThrows(IllegalArgumentException.class, ()->user.setUsername(""));
                },
                ()->{
                    user.setUsername("user");
                    Assertions.assertEquals("user", user.getUsername());
                }
        );
    }

    @Test
    void getPassword() {
        Assertions.assertEquals("password", user.getPassword());
    }

    @Test
    void setPassword() {
        Assertions.assertAll(
                ()->{
                    Assertions.assertThrows(IllegalArgumentException.class, ()->user.setPassword(""));
                },
                ()->{
                    user.setPassword("pass");
                    Assertions.assertEquals("pass", user.getPassword());
                }
        );
    }

    @Test
    void getRole() {
        Assertions.assertEquals(Role.ADMIN, user.getRole());
    }

    @Test
    void setRole() {
        user.setRole(Role.MANAGER);
        Assertions.assertEquals(Role.MANAGER, user.getRole());
    }

    @Test
    void testToString() {
        Assertions.assertEquals("User{username=username, password=password, role=ADMIN}", user.toString());
    }

    @Test
    void testEquals() {
        Assertions.assertAll(
                ()->{
                    Assertions.assertTrue(user.equals(user));
                },
                ()->{
                    User invalidUser = new User("user", "password");
                    Assertions.assertFalse(user.equals(invalidUser));
                },
                ()->{
                    User invalidUser = new User("username", "pass");
                    Assertions.assertFalse(user.equals(invalidUser));
                },
                ()->{
                    User validUser = new User("username", "password");
                    Assertions.assertTrue(user.equals(validUser));
                },
                ()->{
                    Assertions.assertFalse(user.equals(new String("")));
                }
        );
    }

    @Test
    void getIfExists() {
        ArrayList<User> allUsers = User.getUsers();
        allUsers.add(user);
        Assertions.assertAll(
                ()->{
                    User possibleUser = User.getIfExists(new User("username", "password"));
                    Assertions.assertTrue(user == possibleUser);
                },
                ()->{
                    User possibleUser = User.getIfExists(new User("user", "pass"));
                    Assertions.assertTrue(possibleUser == null);
                }
        );
    }

    @Test
    void getUsers() {
        User user1 = new User("username", "password",
                "John", "Doe", "jdoe@gmail.com", 5000,
                Role.ADMIN);
        User user2 = new User("username2", "password2",
                "John", "Doe", "jdoe@gmail.com", 5000,
                Role.MANAGER);
        User user3 = new User("username3", "password3",
                "John", "Doe", "jdoe@gmail.com", 5000,
                Role.LIBRARIAN);
        user1.SaveInFile();
        user2.SaveInFile();
        user3.SaveInFile();
        ArrayList<User> savedUsers = new ArrayList<>();
        savedUsers.add(user1);
        savedUsers.add(user2);
        savedUsers.add(user3);
        Assertions.assertAll(
                ()->{
                    ArrayList<User> result = User.getUsers();
                    for(User x : savedUsers)
                        Assertions.assertTrue(result.contains(x));
                },
                ()->{
                    ArrayList<User> allUsers = User.getUsers();
                    int count = allUsers.size();
                    for(int i = 0; i < count; i++){
                        allUsers.remove(0);
                    }
                    ArrayList<User> result = User.getUsers();
                    for(User x : savedUsers)
                        Assertions.assertTrue(result.contains(x));
                },
                ()->{
                    deleteUsers();
                    Assertions.assertTrue(User.getUsers().size() == 0);
                }
        );
    }

    @ParameterizedTest
    @CsvSource({
            "username,password,John,Doe,jdoe@gmail.com,5000,2,true",
            ",password,John,Doe,jdoe@gmail.com,5000,2,false",
            "username,password,,Doe,jdoe@gmail.com,5000,2,false",
            "username,password,John,,jdoe@gmail.com,5000,2,false",
            "username,password,John,Doe,,5000,2,false",
            "username,password,John,Doe,jdoe@gmail.com,-1000,2,false"
    })
    void saveInFile(String username, String password, String firstName,
                    String lastName, String email, String salary, String role, String expected) {
        User newUser = new User(username, password, firstName,
                            lastName, email, Double.parseDouble(salary),
                Role.ADMIN);
        Assertions.assertEquals(Boolean.parseBoolean(expected), newUser.SaveInFile());
    }

    @Test
    void deleteFromFile() {
        Assertions.assertAll(
                ()->{
                    user = new User("username99", "password",
                            "John", "Doe", "jdoe@gmail.com", 5000,
                            Role.ADMIN);
                    ArrayList<User> users = User.getUsers();
                    users.add(user);
                    user.deleteFromFile();
                    Assertions.assertFalse(users.contains(user));
                },
                ()->{
                    ArrayList<User> users = User.getUsers();
                    users.add(user);
                    user.setThrowError(true);
                    Assertions.assertFalse(user.deleteFromFile());
                    user.setThrowError(false);
                }
        );
    }

    @Test
    void getSearchResults() {
        User user1 = new User("username1", "password",
                "John", "Doe", "jdoe@gmail.com", 5000,
                Role.ADMIN);
        User user2 = new User("username2", "password2",
                "Jake", "Doe", "jdoe@gmail.com", 5000,
                Role.MANAGER);
        User user3 = new User("username3", "password3",
                "John", "Smith", "jdoe@gmail.com", 5000,
                Role.LIBRARIAN);
        ArrayList<User> users = User.getUsers();
        users.add(user1);
        users.add(user2);
        users.add(user3);
        Assertions.assertAll(
                ()->{
                    ArrayList<User> searchedUsers = User.getSearchResults("John");

                    Assertions.assertTrue(searchedUsers.contains(user1));
                    Assertions.assertTrue(searchedUsers.contains(user3));
                    Assertions.assertFalse(searchedUsers.contains(user2));
                },
                ()->{
                    ArrayList<User> searchedUsers = User.getSearchResults("Doe");
                    Assertions.assertTrue(searchedUsers.contains(user1));
                    Assertions.assertTrue(searchedUsers.contains(user2));
                    Assertions.assertFalse(searchedUsers.contains(user3));
                },
                ()->{
                    ArrayList<User> searchedUsers = User.getSearchResults("John Doe");
                    Assertions.assertTrue(searchedUsers.contains(user1));
                    Assertions.assertFalse(searchedUsers.contains(user2));
                    Assertions.assertFalse(searchedUsers.contains(user3));
                },
                ()->{
                    ArrayList<User> searchedUsers = User.getSearchResults("username1");
                    Assertions.assertTrue(searchedUsers.contains(user1));
                    Assertions.assertFalse(searchedUsers.contains(user2));
                    Assertions.assertFalse(searchedUsers.contains(user3));
                },
                ()->{
                    ArrayList<User> searchedUsers = User.getSearchResults("");
                    Assertions.assertTrue(searchedUsers == users);
                }
        );
    }
}