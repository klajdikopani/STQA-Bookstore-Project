package com.example.Test.Models;

import com.example.bookstorehenrihatija.auxiliaries.Config;
import com.example.bookstorehenrihatija.models.Author;
import com.example.bookstorehenrihatija.models.Book;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class TestBaseModel {
    @Test
    void test_save(){
        Assertions.assertFalse(new Author("Oscar", "Wilde").save(new File("")));
    }
}
