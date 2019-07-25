package dataProviders;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ExtentReportConfigReader {
    private Properties properties;
    private final String propertyFilePath= "src/test/resources/extent.properties";

    public ExtentReportConfigReader(){
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(propertyFilePath));
            properties = new Properties();
            try { properties.load(reader); }
            catch (IOException e) { e.printStackTrace(); }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Properties file not found at path : " + propertyFilePath);
        }finally {
            try { if(reader != null) reader.close(); }
            catch (IOException ignore) {}
        }
    }

    public String getSaveScreenShotsTo(){
        String saveScreenShotsTo = properties.getProperty("saveScreenshotsTo");
        if(saveScreenShotsTo!= null) return saveScreenShotsTo;
        else throw new RuntimeException("Screenshots path not specified in the Configuration.properties file for the Key: saveScreenshotsTo");
    }

    public String getSaveArchiveReportsTo(){
        String archiveReportsTo = properties.getProperty("archiveReportsTo");
        if(archiveReportsTo!= null) return archiveReportsTo;
        else throw new RuntimeException("Archived Reports path not specified in the Configuration.properties file for the Key: archiveReportsTo");
    }

    public String getCurrentReportPath(){
        String latestReportPath = properties.getProperty("extent.reporter.html.out");
        if(latestReportPath!= null) return latestReportPath;
        else throw new RuntimeException("Latest Reports path not specified in the Configuration.properties file for the Key: extent.reporter.html.out");
    }

}
