package com.lucidity.utils;

import com.lucidity.enums.Environment;
import com.lucidity.filehandler.FileSearch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class PropertyReader {
    private static final Logger LOGGER = LogManager.getLogger(PropertyReader.class);
    private static final String ENVIRONMENT_KEY = "environment";
    private Environment environment;
    private Properties commonProperties = new Properties();
    private Properties envProperties = new Properties();
    private Properties testConfigurationProperties = new Properties();

    private static PropertyReader propertyReader;

    public static PropertyReader getPropertyReader(){
        if(propertyReader ==null){
            propertyReader = new PropertyReader();
        }
        return propertyReader;
    }

    public Environment getEnvironment() {
        return environment;
    }

    private PropertyReader() {
        setAllEnvProperties();
    }


    private void setAllEnvProperties() {
        setTestConfigurationProperties();
        setTestEnvProperties();
    }


    private String getPath(String fileName) {
        String configurationFilePath;
        FileSearch fileSearch = new FileSearch();
        fileSearch.searchDirectory(new File(System.getProperty("user.dir")), fileName);
        List<String> files = fileSearch.getResult().stream()
                .filter(x -> !x.contains(File.separatorChar + "build" + File.separatorChar))
                .filter(x -> !x.contains(File.separatorChar + "target" + File.separatorChar))
                .collect(Collectors.toList());

        int count = files.size();
        if (count == 0) {
            LOGGER.info(" No Config Properties file : {} found!", fileName);
            throw new RuntimeException("Unable to find the configurationFile");
        } else if (count > 1) {
            LOGGER.error("Found {} configuration files. Ambiguity may arise. Please keep only one {} file.", count, fileName);
            files.forEach(x -> LOGGER.error("file location : {}", x));
            throw new RuntimeException("multiple Configuration File found");
        } else {
            LOGGER.info("Configuration file is at : {}", files.get(0));
            return files.get(0);
        }

    }

    public String getValueForGivenKey(String key) {
        return getValueForGivenKey(key, true);
    }

    public String getValueForGivenKey(String key, boolean searchEnvSpecificFile) {

        if (System.getProperty(key) != null) {
            return System.getProperty(key);
        } else if (searchEnvSpecificFile && envProperties.getProperty(key) != null) {
            return envProperties.getProperty(key);
        } else if (commonProperties.getProperty(key) != null) {
            return commonProperties.getProperty(key);
        } else if (testConfigurationProperties.getProperty(key) != null) {
            return testConfigurationProperties.getProperty(key);
        } else {
            LOGGER.error("Key : {} not found", key);
            throw new RuntimeException("Key : $key not found");
        }
    }

    private void setTestConfigurationProperties() {
        LOGGER.info("setting Test configuration properties");
        try {
            testConfigurationProperties.load(
                    new FileInputStream(getPath("testconfiguration.properties"))
            );
        } catch (IOException ioException) {
            LOGGER.info(
                    "Either Test Configuration properties file cannot be found or multiple Test Configuration properties files are found. Please place only one 'testconfiguration.properties' in the project"
            );
        }
    }

    private void setTestEnvProperties() {
        String testEnvFileName = null;
        if (environment == null) {
            environment = Environment.valueOf(getValueForGivenKey(ENVIRONMENT_KEY, false).toUpperCase());
        }
        try {
            switch (environment) {
                case QA:
                    testEnvFileName = getPath("qa.properties");
                    break;
                case DEV:
                    testEnvFileName = getPath("dev.properties");
                    break;
                case LOCAL:
                    testEnvFileName = getPath("local.properties");
                    break;
                case DOCKER:
                    testEnvFileName = getPath("docker.properties");
                    break;
                default:
                    throw new RuntimeException(String.format("%s Environment Not defined", environment));
            }
            if (testEnvFileName != null) {
                envProperties.load(new FileInputStream(testEnvFileName));
            }
        } catch (IOException ioException) {
            LOGGER.info(
                    "Either Test Configuration properties file cannot be found or multiple Test Configuration properties files are found. Please place only one 'testconfiguration.properties' in the project"
            );
        }
        if (commonProperties.isEmpty() && envProperties.isEmpty()) {
            LOGGER.error("No/Empty Test Configuration properties File Present ");
            throw new RuntimeException("o/Empty Test Configuration properties File Present");
        }
    }
}

