package CGA.fileoutput.impl;



import CGA.fileoutput.FileOutputContext;
import org.uma.jmetal.util.JMetalException;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class DefaultFileOutputContext implements FileOutputContext {
    private static final String DEFAULT_SEPARATOR = " ";
    protected String fileName;
    protected String separator;

    public DefaultFileOutputContext(String fileName) {
        this.fileName = fileName;
        this.separator = " ";
    }

    public BufferedWriter getFileWriter() {
        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(this.fileName);
        } catch (FileNotFoundException var3) {
            throw new JMetalException("Exception when calling method getFileWriter()", var3);
        }

        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
        return new BufferedWriter(outputStreamWriter);
    }

    public String getSeparator() {
        return this.separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public String getFileName() {
        return this.fileName;
    }
}
