package ro.notfound.co_gui.logging;

import java.io.FileWriter;
import java.io.IOException;

public class FileLogger implements ILog {
    private String fileName;
    private FileWriter myFile;

    public FileLogger(String fileName) {
        this.fileName = fileName;

        //creating the text file
        //file exists, so we open it for writing
        try{
            myFile = new FileWriter(fileName, true);

        } catch (IOException e) {
            e.printStackTrace();
        }

        //file doesn't exist, so we need to create it
        try{
            myFile = new FileWriter(fileName);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void write(long longVar) {
        try{
            myFile.write(Long.toString(longVar));

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void write(String stringVar) {
        try {
            myFile.write(stringVar);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(Object... values) {
        try {
            for(Object obj: values) {
                myFile.write(obj.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeTime(long value, TimeUnit unit) {
        try{
            myFile.write((int) TimeUnit.toTimeUnit(value, unit));
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void writeTime(String string, long value, TimeUnit unit) {
        try{
            myFile.write(string + " " + TimeUnit.toTimeUnit(value, unit) + " " + unit);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            myFile.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
