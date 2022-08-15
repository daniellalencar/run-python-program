package br.com.pcdf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandsList {

  private static final String DATE_FORMAT = "yyyyMMdd";
  private static final int INITIAL_YEAR = 1900;
  private static final int FINAL_YEAR = 2022;
  private static final DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
  private static final DateTimeFormatter dateFormat8 = DateTimeFormatter.ofPattern(DATE_FORMAT);
  private static final Logger LOGGER = Logger.getLogger(
      Thread.currentThread().getStackTrace()[0].getClassName());
  private static final int MINIMAL_OF_TRYING = 2;

  private int countTry = 0;


  public static void main(String[] args) {
    CommandsList run = new CommandsList();
    try {
      System.out.println("-------------------teste----------------");
      run.executeShellCommand("sudo python3 /home/iiadmin/projetos/persona/_001_face_vector_prod_multi.py 19010312 19010327 civil");
    } catch (IOException e) {
      System.out.println("-------------------erorororororororo----------------");
      e.printStackTrace();
    }
  }

  private void execute() {
    final List<String> commandList = getCommandList();
    for (String command : commandList) {

      try {
        LOGGER.info("-----------------------------------------------------------------");
        LOGGER.info("Executing " + command.toString());
        executeShellCommand(command);
        LOGGER.info("-----------------------------------------------------------------");
      } catch (IOException e) {
        LOGGER.log(Level.SEVERE, e.toString(), e);
        //e.printStackTrace();
      }
    }
  }

  public void executeShellCommand(String command) throws IOException {
    Process process = Runtime.getRuntime().exec(toString());
    BufferedReader stdInput = new BufferedReader(new
        InputStreamReader(process.getInputStream()));

    BufferedReader stdError = new BufferedReader(new
        InputStreamReader(process.getErrorStream()));

// Read the output from the command
    System.out.println("Standard output of the command:\n");
    String s = null;
    while ((s = stdInput.readLine()) != null) {
      LOGGER.info("Executing " + command.toString());
    }

// Read any errors from the attempted command
    System.out.println("Standard error of the command (if any):\n");
    while ((s = stdError.readLine()) != null) {
      LOGGER.log(Level.SEVERE, s);
    }
  }

  public List<String> getCommandList() {
    Map<String, String> listOfRange = getRangeOfDates(INITIAL_YEAR, FINAL_YEAR);
    final Set<String> keys = listOfRange.keySet();
    List<String> commands = new ArrayList();
    //boolean isTestTwoDir = isItMinimalOfTrying();
    boolean isTestTwoDir = true;
    for (String key : keys) {
      //if (!isItMinimalOfTrying()) {
      //  break;
     /// }
      if (!isTestTwoDir) {
        break;
      }
      String command =
          "sudo python3 /home/iiadmin/projetos/persona/_001_face_vector_prod_multi.py " + key + " "
              + listOfRange.get(key)
              + " civil ";

//      LOGGER.info("Minimal of Trying:" + countTry);
      commands.add(command);
      ++countTry;
    }
    return commands;
  }

  private boolean isItMinimalOfTrying() {
    return MINIMAL_OF_TRYING > countTry;
  }

  private Map<String, String> getRangeOfDates(int initialYear, int finalYear) {
    Date currentDate = new GregorianCalendar(initialYear, Calendar.JANUARY, 01).getTime();
    Map<String, String> stringStringMap = new LinkedHashMap<String, String>();
    int initialYearTemp = 0;
    while (initialYearTemp <= finalYear) {
      LocalDateTime localDateTime = currentDate.toInstant().atZone(ZoneId.systemDefault())
          .toLocalDateTime();

      Date currentDatePlusOneDay = Date
          .from(localDateTime.plusDays(15).atZone(ZoneId.systemDefault()).toInstant());

      stringStringMap
          .put(dateFormat.format(currentDate), dateFormat.format(currentDatePlusOneDay));
      currentDate = currentDatePlusOneDay;

      initialYearTemp = Integer.parseInt(dateFormat.format(currentDate).substring(0, 4));
    }
    return stringStringMap;
  }
}
