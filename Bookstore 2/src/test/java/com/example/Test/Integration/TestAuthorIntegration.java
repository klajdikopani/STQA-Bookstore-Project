package com.example.Test.Integration;
import com.example.bookstorehenrihatija.auxiliaries.Config;
import com.example.bookstorehenrihatija.models.Author;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class TestAuthorIntegration {
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
    void test_SaveInFile(){
        Assertions.assertAll(
                ()->{
                    author.SaveInFile();
                    try(ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(author.DATA_FILE))){
                        Author savedAuthor = (Author)inputStream.readObject();
                        Assertions.assertEquals(author, savedAuthor);
                        Assertions.assertEquals(Author.getAuthors().get(0), savedAuthor);
                        Assertions.assertEquals(Author.getAuthors().get(0), author);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                },
                ()->{
                    Author notValidAuthor = new Author("", "Hatija");
                    Assertions.assertFalse(notValidAuthor.SaveInFile());
                }
        );
    }

    @Test
    void test_deleteFromFile(){
        Author author1 = new Author("Oscar", "Wilde");
        Author author2 = new Author("Ismail", "Kadare");
        Author author3 = new Author("Naim", "Frasheri");
        author1.SaveInFile();
        author2.SaveInFile();
        author3.SaveInFile();
        author1.deleteFromFile();
        Assertions.assertAll(
                ()->{
                    ArrayList<Author> allAuthors = Author.getAuthors();
                    Assertions.assertTrue(allAuthors.contains(author2));
                    Assertions.assertTrue(allAuthors.contains(author3));
                    Assertions.assertFalse(allAuthors.contains(author1));
                },
                ()->{
                    ArrayList<Author> allAuthors = Author.getAuthors();
                    int count = allAuthors.size();
                    for(int i = 0; i < count; i++){
                        allAuthors.remove(0);
                    }
                    allAuthors = Author.getAuthors();
                    Assertions.assertTrue(allAuthors.contains(author2));
                    Assertions.assertTrue(allAuthors.contains(author3));
                    Assertions.assertFalse(allAuthors.contains(author1));
                }
        );
    }
}
