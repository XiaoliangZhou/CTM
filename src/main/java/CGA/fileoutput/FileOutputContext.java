package CGA.fileoutput;


import java.io.BufferedWriter;
import java.io.Serializable;

public interface FileOutputContext extends Serializable {
    BufferedWriter getFileWriter();

    String getSeparator();

    void setSeparator(String var1);

    String getFileName();
}
