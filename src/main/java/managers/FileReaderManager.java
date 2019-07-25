package managers;

import dataProviders.ConfigFileReader;
import dataProviders.ExtentReportConfigReader;
import dataProviders.JsonDataReader;

public class FileReaderManager {

    private static FileReaderManager fileReaderManager = new FileReaderManager();
    private static ConfigFileReader configFileReader;
    private static JsonDataReader jsonDataReader;
    private static ExtentReportConfigReader extentReportConfigReader;

    private FileReaderManager() {
    }

    public static FileReaderManager getInstance( ) {
        return fileReaderManager;
    }

    public ConfigFileReader getConfigReader() {
        return (configFileReader == null) ? new ConfigFileReader() : configFileReader;
    }

    public JsonDataReader getJsonReader(){
        return (jsonDataReader == null) ? new JsonDataReader() : jsonDataReader;
    }

    public ExtentReportConfigReader getExtentReportConfigReader() {
        return (extentReportConfigReader == null) ? new ExtentReportConfigReader() : extentReportConfigReader;
    }
}
