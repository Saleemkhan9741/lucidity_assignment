package com.lucidity.filehandler;


import com.lucidity.utils.PropertyReader;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;

public class FileUtil {

  private static Platform platform;

  /**
   * Deletes the content of the directory
   *
   * @param directory name of the directory (along with the path) to delete its content
   */
  public static void deleteDirectoryContent(File directory) {
    try {
      if (directory.isDirectory() && directory.list().length > 0) {
        FileUtils.cleanDirectory(directory);
      }
    } catch (IOException e) {
      System.out.println("No content in the directory: " + directory);
    }
  }

  /**
   * Deletes the directory along with the content
   *
   * @param directory name of the directory (along with the path) to delete
   * @return true if the directory is deleted, else false
   */
  public static boolean deleteDirectory(File directory) {
    deleteDirectoryContent(directory);
    return directory.delete();
  }

  public static String getSystemFileSeperator() {
    return System.getProperty("file.separator");
  }

  /**
   * Gets all the Directories in the path sorted by the Date of Modification (Oldest Directory being
   * at the 0th index)
   *
   * @param path path of the directory
   * @return array of the directories sorted by the Date of Modification
   */
  public static File[] getDirectoriesSortedByDataModified(String path) {
    File directory = new File(path);
    File[] directories = directory.listFiles();

    Arrays.sort(
        directories,
        new Comparator<File>() {
          public int compare(File f1, File f2) {
            return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
          }
        });
    return directories;
  }

  /**
   * Creates the path along with the directory if it does not exist and remove the content of the
   * folder
   *
   * @param path directory along with the path to be created
   * @return directory which is created
   */
  public static File createDirectoryInPath(String path) {
    File directory = new File(path);

    if (!directory.exists()) {
      if (directory.mkdirs()) {
        System.out.println("Directory: " + path + " is created!");
      } else {
        System.out.println("Failed to create directory: " + path);
      }
    } else {
      System.out.println("Directory already exists: " + path);
    }
    return directory;
  }

  /**
   * Gets the platform name based upon the operating system which is being in use
   *
   * @return platform enum (WINDOWS, MAC or LINUX) on the basis of operating system
   */
  public static Platform getCurrentPlatform() {
    if (platform == null) {
      String operatingSystem = System.getProperty("os.name").toLowerCase();
      if (operatingSystem.contains("win")) {
        platform = Platform.WINDOWS;
      } else if (operatingSystem.contains("nix")
          || operatingSystem.contains("nux")
          || operatingSystem.contains("aix")) {
        platform = Platform.LINUX;
      } else if (operatingSystem.contains("mac")) {
        platform = Platform.MAC;
      }
    }
    return platform;
  }

  /**
   * Copies the contents (directories and files) from the source to the destination (by creating the
   * destination folder)
   *
   * @param source path from where the directories and files are needed to be copied
   * @param destination path to which all the copied content is pasted by creating the directory, if
   *     does not exist
   */
  public static boolean copyFilesTo(String source, String destination) {
    File sourceFile = new File(source);
    File destinationFile = createDirectoryInPath(destination);
    try {
      FileUtils.copyDirectory(sourceFile, destinationFile);
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Writes the file with the content
   *
   * @param requestFileName file name along with the path where content is to be written
   * @param content content which is needed to be written in requestFileName
   */
  public static void writeFile(String requestFileName, String content) {
    try (FileWriter file = new FileWriter(requestFileName)) {
      file.write(content);
      file.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Reads the content of the file
   *
   * @param filePath path of the file whose content is needed to be read
   * @return the content of the file in String format
   * @throws IOException if the file is not found
   */
  public static String readFile(String filePath) throws IOException {
    FileReader fr = new FileReader(filePath);

    int i;
    String content = "";
    while ((i = fr.read()) != -1) {
      content = content + (char) i;
    }
    fr.close();
    return content;
  }

  /**
   * Changes the path of the file according to the Current Operating System
   *
   * @param path file path according to any Operating System
   * @return file path according to the Operating System in use for execution
   */
  public static String getSystemPath(String path) {
    Platform platform = FileUtil.getCurrentPlatform();
    if (platform == Platform.WINDOWS) {
      path.replace("\\", "\\\\");
      path.replace("/", "\\\\");
    } else {
      path.replace("\\", "/");
      path.replace("\\\\", "/");
    }
    return path;
  }

  public static String getRootDirectory() {
    return System.getProperty("user.dir");
  }

  public static String getOutputFolderPath() {
    String path = getSystemPath(getRootDirectory() + PropertyReader.getPropertyReader().getValueForGivenKey("path.output"));
    FileUtil.createDirectoryInPath(path);
    return path;
  }

  public static String getLatestDataOutputPath() {
    String path =
        getSystemPath(
            getOutputFolderPath() + PropertyReader.getPropertyReader().getValueForGivenKey("path.latest.data.output"));
    FileUtil.createDirectoryInPath(path);
    return path;
  }

  public static String getArchivePath() {
    String path =
        getSystemPath(getOutputFolderPath() + PropertyReader.getPropertyReader().getValueForGivenKey("path.archive"));
    FileUtil.createDirectoryInPath(path);
    return path;
  }

  public static File getFile(String outputFilePath, String fileName) throws IOException {
    return getFile(outputFilePath, fileName, false);
  }

  public static File createFile(String outputFilePath, String fileName) throws IOException {
    return getFile(outputFilePath, fileName, true);
  }

  public static File getFile(String fileName) throws IOException {
    return getFile(getLatestDataOutputPath(), fileName, false);
  }

  public static File createFile(String fileName) throws IOException {
    return getFile(getLatestDataOutputPath(), fileName, true);
  }

  public static File getFile(String outputFilePath, String fileName, boolean deleteIfExists)
      throws IOException {
    Path path = Path.of(outputFilePath, fileName);
    if (deleteIfExists) {
      if (Files.exists(path)) {
        Files.delete(path);
      }
      Files.createFile(path);
    }

    return path.toFile();
  }
}
