package br.com.pcdf;

import java.io.IOException;
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
import java.util.logging.Logger;

public class Run {

  private static final String DATE_FORMAT = "yyyyMMdd";
  private static final DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
  private static final DateTimeFormatter dateFormat8 = DateTimeFormatter.ofPattern(DATE_FORMAT);
  private static final Logger LOGGER = Logger.getLogger(
      Thread.currentThread().getStackTrace()[0].getClassName());
  private static final int MINIMAL_OF_TRYING = 2;

  private static int countTry = 2;


  public static void main(String[] args) {
    Run run = new Run();
    run.execute();
  }

  private void execute() {
    final List commandList = getCommandList();
    for (Object command : commandList) {
      try {
        LOGGER.info("Executing " + command.toString());
        Process process = Runtime.getRuntime().exec(command.toString());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private List getCommandList() {
    Map<String, String> listOfRange = getRangeOfDates(1800, 2022);
    final Set<String> keys = listOfRange.keySet();
    List commands = new ArrayList();
    boolean isTestTwoDir = isItMinimalOfTrying();
    for (String key : keys) {
      if (!isItMinimalOfTrying()) {
        break;
      }
      String command =
          "sudo python3 /home/iiadmin/projetos/persona/_001_face_vector_prod_multi.py " + key + " "
              + listOfRange.get(key)
              + " civil ";

      LOGGER.info("Minimal of Trying:" + countTry);
      commands.add(command);
      ++countTry;
    }
    return commands;
  }

  private boolean isItMinimalOfTrying() {
    return MINIMAL_OF_TRYING >= countTry;
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
