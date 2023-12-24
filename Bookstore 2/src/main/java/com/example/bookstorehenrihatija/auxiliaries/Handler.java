package com.example.bookstorehenrihatija.auxiliaries;

import com.example.bookstorehenrihatija.models.BaseModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public interface Handler {
    <T extends BaseModel> void overwriteCurrentListToFile(File file, ArrayList<T> data) throws IOException;
}
