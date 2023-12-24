package com.example.bookstorehenrihatija.auxiliaries;


import com.example.bookstorehenrihatija.models.BaseModel;

import java.io.*;
import java.util.ArrayList;
public class FileHandler{
    public static <T extends BaseModel> void overwriteCurrentListToFile(File file, ArrayList<T> data) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(file, false);
        ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);
        for (T entity: data)
            outputStream.writeObject(entity);
        outputStream.close();
        fileOutputStream.close();
    }
}
