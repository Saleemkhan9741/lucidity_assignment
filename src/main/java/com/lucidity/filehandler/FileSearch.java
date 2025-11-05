package com.lucidity.filehandler;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileSearch {
    private static final Logger LOGGER = LogManager.getLogger(FileSearch.class);
    /**
     * Name of the file to Search
     */
    private String fileNameToSearch;

    /**
     * List of names of all the files after search is done
     */
    private List<String> result = new ArrayList<>();

    /**
     * Gets the name of the file which is being searched
     *
     * @return the name of the file which is being searched
     */
    public String getFileNameToSearch() {
        return fileNameToSearch;
    }

    /**
     * Sets the name of the file which is being searched
     *
     * @param fileNameToSearch the name of the file which is being searched
     */
    public void setFileNameToSearch(String fileNameToSearch) {
        this.fileNameToSearch = fileNameToSearch;
    }

    /**
     * List of names of all the files after search is done
     *
     * @return the list of the files which are searched in the directory
     */
    public List<String> getResult() {
        return result;
    }

    /**
     * Searches whether the file is present in the directory
     *
     * @param directory        path of the directory where the file is needed to be searched
     * @param fileNameToSearch name of the file to search
     */
    public void searchDirectory(File directory, String fileNameToSearch) {
        setFileNameToSearch(fileNameToSearch);
        if (directory.isDirectory()) {
            search(directory);
        } else {
            LOGGER.info(directory.getAbsoluteFile() + " is not a directory!");
        }
    }

    /**
     * Helps 'searchDirectory' method to search the file
     *
     * @param file directory where the file is needed to search
     */
    private void search(File file) {
        if (file.isDirectory()) {
            // check the permission to read the file
            if (file.canRead()) {
                for (File temp : file.listFiles()) {
                    if (temp.isDirectory()) {
                        search(temp);
                    } else {
                        if (getFileNameToSearch().toLowerCase().equals(temp.getName().toLowerCase())) {
                            result.add(temp.getAbsoluteFile().toString());
                        }
                    }
                }
            } else {
                LOGGER.info(file.getAbsoluteFile() + "Permission Denied");
            }
        }
    }
}