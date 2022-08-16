package br.com.pcdf;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileUtil {

  private static final String FILE_NAME = "cache";

  public static void writeToFile(String value) {
    Path path = Paths.get(FILE_NAME);

    try {
      Files.write(path, value.getBytes(), StandardOpenOption.CREATE);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static String readFromFile() {
    String retorno = null;
    try {
      Path path = Paths.get(FILE_NAME);
      byte[] data = Files.readAllBytes(path);
      retorno = new String(data);
      retorno = retorno != null && retorno.equals("") ? null : retorno;
    } catch (IOException e) {
      // exception handling
    }
    return retorno;
  }
}
