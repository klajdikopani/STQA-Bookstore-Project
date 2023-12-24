package com.example.bookstorehenrihatija.models;

import com.example.bookstorehenrihatija.auxiliaries.CustomObjectOutputStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public abstract class BaseModel {
    public void setThrowError(boolean throwError) {
        this.throwError = throwError;
    }

    protected boolean throwError = false;
    public abstract boolean SaveInFile();
    public boolean save(File dataFile){
        if(!isValid()){
            return false;
        }
        try{
            ObjectOutputStream outputStream;
            FileOutputStream fileOutputStream = new FileOutputStream(dataFile, true);
            if(dataFile.length() == 0){
                outputStream = new ObjectOutputStream(fileOutputStream);
            }else {
                outputStream = new CustomObjectOutputStream(fileOutputStream);
            }
            outputStream.writeObject(this);
            outputStream.close();
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }

        return true;
    }
    public abstract boolean isValid();
    public abstract boolean deleteFromFile();
}
