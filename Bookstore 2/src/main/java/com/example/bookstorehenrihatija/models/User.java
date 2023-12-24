package com.example.bookstorehenrihatija.models;

import com.example.bookstorehenrihatija.auxiliaries.Config;
import com.example.bookstorehenrihatija.auxiliaries.FileHandler;
import com.example.bookstorehenrihatija.auxiliaries.Handler;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class User extends BaseModel implements Serializable {
    private static final ArrayList<User> users = new ArrayList<>();
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private double salary;
    private Role role;


    public static final String FILE_PATH = new Config().getProperty("filepath") + "/users.ser";
    public static final File DATA_FILE = new File(FILE_PATH);
    @Serial
    private static final long serialVersionUID = 1234567L;
    public User(){}
    public User(String username, String password, String firstName, String lastName,
                String email, double salary, Role role) {
        this(username, password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.salary = salary;
        this.role = role;

    }
    public User(String username, String password){
        this.username = username;
        this.password = password;
    }
    public String getFirstName(){ return firstName; }
    public void setFirstName(String firstName) throws IllegalArgumentException{
        if(firstName.length() == 0)
            throw new IllegalArgumentException("Name must have at least one character");
        this.firstName = firstName;
    }
    public String getLastName(){ return lastName; }
    public void setLastName(String lastName) throws IllegalArgumentException{
        if(lastName.length() == 0)
            throw new IllegalArgumentException("Name must have at least one character");
        this.lastName = lastName;
    }
    public String getEmail(){ return email; }
    public void setEmail(String email) throws IllegalArgumentException{
        if(email.length() == 0 || !email.contains("@"))
            throw new IllegalArgumentException();
        this.email = email;
    }
    public double getSalary(){ return salary; }
    public void setSalary(double salary) throws IllegalArgumentException{
        if(salary < 0)
            throw new IllegalArgumentException("Salary cannot be negative");
        this.salary = salary;
    }
    public String getUsername(){ return username; }
    public void setUsername(String username) throws IllegalArgumentException{
        if(username.length() == 0)
            throw new IllegalArgumentException("Name must have at least one character");
        this.username = username;
    }
    public String getPassword(){ return password; }
    public void setPassword(String password) throws IllegalArgumentException{
        if(password.length() == 0)
            throw new IllegalArgumentException();
        this.password = password;
    }
    public Role getRole(){ return role; }
    public void setRole(Role role){ this.role = role; }
    @Override
    public String toString(){
        return "User{" +
                "username=" + getUsername() +
                ", password=" + getPassword() +
                ", role=" + getRole() +
                '}';
    }
    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if (obj instanceof User) {
            User other = (User) obj;
            return other.getUsername().equals(getUsername()) && other.getPassword().equals(getPassword());
        }
        return false;
    }
    public static User getIfExists(User potentialUser){
        for(User user : getUsers()){
            if(user.equals(potentialUser)){
                return user;
            }
        }
        return null;
    }
    public static ArrayList<User> getUsers(){
        if(users.size() == 0){
            try(ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(FILE_PATH))){
                while(true){
                    User temp = (User) inputStream.readObject();
                    if(temp != null){
                        users.add(temp);
                    }else{
                        break;
                    }
                }
            }catch(EOFException eofException){
                System.out.println("End of file reached!");
            }catch(IOException | ClassNotFoundException e){
                e.printStackTrace();
            }
        }
        return users;
    }
    @Override
    public boolean SaveInFile() {
        boolean saved = super.save(DATA_FILE);
        if(saved)
            users.add(this);
        return saved;
    }

    @Override
    public boolean isValid() {
        return getUsername() != null && getUsername().length() > 0
                && getPassword() != null && getPassword().length() > 0
                && getFirstName() != null && getFirstName().length() > 0
                && getLastName() != null && getLastName().length() > 0
                && getEmail() != null && getEmail().length() > 0
                && getSalary() > 0;
    }

    @Override
    public boolean deleteFromFile() {
        users.remove(this);
        try{
            if(throwError)
                throw new IOException("mock");
            FileHandler.overwriteCurrentListToFile(DATA_FILE, users);
        }catch(IOException ex){
            ex.printStackTrace();
            return false;
        }
        return true;
    }
    public static ArrayList<User> getSearchResults(String searchText){
        if(searchText.equals(""))
            return User.getUsers();
        ArrayList<User> results = new ArrayList<>();
        for(User user: User.getUsers()){
            if(user.getUsername().equals(searchText)
                    || user.getFirstName().equals(searchText)
                    || user.getLastName().equals(searchText)
                    || (user.getFirstName() + " " + user.getLastName()).equals(searchText))
                results.add(user);
        }
        return results;
    }
}
