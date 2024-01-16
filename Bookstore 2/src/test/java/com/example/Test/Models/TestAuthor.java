package com.example.Test.Models;

import com.example.bookstorehenrihatija.auxiliaries.Config;
import com.example.bookstorehenrihatija.models.Author;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TestAuthor {
    static String filepath;
    Author author;
    @TempDir
    static File tempDir;
    @BeforeAll
    static void setUp(){
        Config config = new Config();
        filepath = config.getProperty("filepath");
        config.setProperty("filepath", tempDir.getPath());
    }

    @BeforeEach
    void initAuthor(){
        author = new Author("Oscar", "Wilde");
    }

    @AfterEach
    void deleteAuthors(){
        ArrayList<Author> allAuthors = Author.getAuthors();
        int count = allAuthors.size();
        for(int i = 0; i < count; i++){
            allAuthors.remove(0);
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
            f.delete();
        }
    }
    @Test
    void getSearchResults() {
        Author author = new Author("Oscar", "Wilde");
        Author author2 = new Author("Naim", "Frasheri");
        Author author3 = new Author("Faik", "Konica");
        author.SaveInFile();
        author2.SaveInFile();
        author3.SaveInFile();
        Assertions.assertAll(
                ()->{
                    ArrayList<Author> searchedAuthors = Author.getSearchResults("Oscar Wilde");
                    for(Author x : searchedAuthors)
                        Assertions.assertEquals(x, author);
                },
                ()->{
                    ArrayList<Author> searchedAuthors = Author.getSearchResults("Ismail Kadare");
                    Assertions.assertTrue(searchedAuthors.size() == 0);
                },
                ()->{
                    ArrayList<Author> searchedAuthors = Author.getSearchResults("");
                    Assertions.assertTrue(searchedAuthors == Author.getAuthors());
                },
                ()->{
                    ArrayList<Author> searchedAuthors = Author.getSearchResults(null);
                    Assertions.assertTrue(searchedAuthors.size() == 0);
                }
        );
    }

    @Test
    void testToString() {
        Assertions.assertEquals("Oscar Wilde", author.toString());
    }

    @Test
    void getFirstName() {
        Assertions.assertEquals("Oscar", author.getFirstName());
    }

    @Test
    void setFirstName() {
        Assertions.assertAll(
                ()->{
                    Throwable exception = Assertions.assertThrows(IllegalArgumentException.class, ()->author.setFirstName(""));
                    Assertions.assertEquals("Name must have at least one character", exception.getMessage());
                },
                ()->{
                    author.setFirstName("Martin");
                    Assertions.assertEquals("Martin", author.getFirstName());
                }
        );
    }

    @Test
    void getLastName() {
        Assertions.assertEquals("Wilde", author.getLastName());
    }

    @Test
    void setLastName() {
        Assertions.assertAll(
                ()->{
                    Throwable exception = Assertions.assertThrows(IllegalArgumentException.class, ()->author.setLastName(""));
                    Assertions.assertEquals("Name must have at least one character", exception.getMessage());
                },
                ()->{
                    author.setLastName("King");
                    Assertions.assertEquals("King", author.getLastName());
                }
        );
    }

    @Test
    void getFullName() {
        Assertions.assertEquals("Oscar Wilde", author.getFullName());
    }

    @Test
    void saveInFile() {
        Assertions.assertTrue(author.SaveInFile());
        Assertions.assertTrue(Author.getAuthors().contains(author));
    }

    @Test
    void isValid() {
        Assertions.assertTrue(author.isValid());
    }

    @Test
    void deleteFromFile() {
        Assertions.assertAll(
                ()->{
                    ArrayList<Author> authors = Author.getAuthors();
                    authors.add(author);
                    Assertions.assertTrue(author.deleteFromFile());
                    Assertions.assertFalse(authors.contains(author));
                },
                ()->{
                    author.setThrowError(true);
                    Assertions.assertFalse(author.deleteFromFile());
                    author.setThrowError(false);
                }
        );
    }

    @Test
    void getAuthors() {
        Author author = new Author("Oscar", "Wilde");
        Author author2 = new Author("Naim", "Frasheri");
        Author author3 = new Author("Faik", "Konica");
        author.SaveInFile();
        author2.SaveInFile();
        author3.SaveInFile();
        ArrayList<Author> savedAuthors = new ArrayList<>();
        savedAuthors.add(author);
        savedAuthors.add(author2);
        savedAuthors.add(author3);
        Assertions.assertAll(
                ()->{
                    ArrayList<Author> result = Author.getAuthors();
                    for(Author x : savedAuthors)
                        Assertions.assertTrue(result.contains(x));
                },
                ()->{
                    ArrayList<Author> allAuthors = Author.getAuthors();
                    int count = allAuthors.size();
                    for(int i = 0; i < count; i++){
                        allAuthors.remove(0);
                    }
                    ArrayList<Author> result = Author.getAuthors();
                    for(Author x : savedAuthors)
                        Assertions.assertTrue(result.contains(x));
                },
                ()->{
                    deleteAuthors();
                    Assertions.assertTrue(Author.getAuthors().size() == 0);
                }
        );
    }

    @Test
    void testEquals() {
        Assertions.assertAll(
                ()->{
                    Assertions.assertFalse(author.equals(new Author("Martin", "Wilde")));
                },
                ()->{
                    Assertions.assertFalse(author.equals(new Author("Oscar", "King")));
                },
                ()->{
                    Assertions.assertFalse(author.equals(new String("")));
                },
                ()->{
                    Assertions.assertTrue(author.equals(author));
                },
                ()->{
                    Assertions.assertTrue(author.equals(new Author("Oscar", "Wilde")));
                }
        );
    }
}