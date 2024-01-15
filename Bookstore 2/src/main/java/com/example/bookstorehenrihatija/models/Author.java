package com.example.bookstorehenrihatija.models;

import com.example.bookstorehenrihatija.auxiliaries.Config;
import com.example.bookstorehenrihatija.auxiliaries.FileHandler;
import com.example.bookstorehenrihatija.auxiliaries.Handler;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.SimpleTimeZone;

public class Author extends BaseModel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1234567L;
    private String firstName;
    private String lastName;

    private static final ArrayList<Author> authors = new ArrayList<>();
    public static final String FILE_PATH = new Config().getProperty("filepath") + "/authors.ser";
    public static final File DATA_FILE = new File(FILE_PATH);
    public static ArrayList<Author> getSearchResults(String searchText){
        if(searchText == null)
            return new ArrayList<Author>();
        if(searchText.equals(""))
            return getAuthors();
        ArrayList<Author> searchResults = new ArrayList<>();
        for(Author author : getAuthors()){
            if(author.getFullName().equals(searchText)){
                searchResults.add(author);
            }
        }
        return searchResults;
    }
    @Override
    public String toString(){ return firstName + " " + lastName; }
    public String getFirstName(){ return firstName; }
    public void setFirstName(String firstName) throws IllegalArgumentException{
        if(firstName.length() == 0)
            throw new IllegalArgumentException("Name must have at least one character");
        this.firstName = firstName;
    }
    public String getLastName(){ return lastName; };
    public void setLastName(String lastName) throws IllegalArgumentException{
        if(lastName.length() == 0)
            throw new IllegalArgumentException("Name must have at least one character");
        this.lastName = lastName;
    }
    public String getFullName(){ return firstName + " " + lastName; };

    public Author(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;;
    }
    @Override
    public boolean SaveInFile() {
        boolean saved = super.save(Author.DATA_FILE);
        if(saved){
            authors.add(this);
        }
        return saved;
    }

    @Override
    public boolean isValid() {
        return firstName.length() > 0 && lastName.length() > 0;
    }

    @Override
    public boolean deleteFromFile() {
        authors.remove(this);
        try{
            if(throwError)
                throw new IOException("Mock");
            FileHandler.overwriteCurrentListToFile(DATA_FILE, getAuthors());
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public static ArrayList<Author> getAuthors(){
        if(authors.size() == 0){
            try(ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(FILE_PATH));){
              while(true){
                    Author temp = (Author)inputStream.readObject();
                    if(temp != null){
                        authors.add(temp);
                    }else{
                        break;
                    }
                }
            }catch (EOFException eofException){
                System.out.println("End of author file reached!");
            }catch(IOException | ClassNotFoundException ex){
                ex.printStackTrace();
                System.out.println("No Authors");
            }
        }
        return authors;
    }
    @Override
    public boolean equals(Object o){
        if(this == o)
            return true;
        if(o instanceof Author) {
            Author temp = (Author) o;
//            return this.firstName.equals(temp.firstName) && this.lastName.equals(temp.lastName);
            return this.toString().equals(temp.toString());
        }
        return false;
    }
}
