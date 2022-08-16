package br.com.pcdf;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {

  private static final String FILE_NAME = "cache";

  public static void writeToFile(String value) {
    try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE_NAME))) {
      bufferedWriter.write(value);
    } catch (IOException e) {

    }
  }

  public static String readFromFile(String value) {
    String line = "";
    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(FILE_NAME))) {
      line = bufferedReader.readLine();
      while (line != null) {
        System.out.println(line);
        line = bufferedReader.readLine();
      }
    } catch (FileNotFoundException e) {
      // Exception handling
    } catch (IOException e) {
      // Exception handling
    }
    return line;
  }
}
