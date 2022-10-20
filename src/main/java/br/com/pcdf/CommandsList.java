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

    // 10.93.22.43

    private static final String DATE_FORMAT = "yyyyMMdd";
    private static final int INITIAL_YEAR = 1900;
    private static final int FINAL_YEAR = 2022;
    private static final DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
    private static final DateTimeFormatter dateFormat8 = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private static final Logger LOGGER = Logger.getLogger(
            Thread.currentThread().getStackTrace()[0].getClassName());
    private static final int MINIMAL_OF_TRYING = 2;
    private static final int LAST_DATE = 20091101;


    private int countTry = 0;


    public static void main(String[] args) {
        CommandsList run = new CommandsList();
        run.execute();
    }

    private void execute() {
        final String contentFile = FileUtil.readFromFile();
        final Integer fromFile = contentFile == null ? null : Integer.parseInt(contentFile);
        final List<String> commandList = getCommandList(fromFile);
        int i = 0;
        for (String command : commandList) {
            try {
                LOGGER.info("comandos executados:" + ++i + " de " + commandList.size());
                executeShellCommand2(command);
                String subStringNumber = command.substring(command.indexOf("py ") + 3);
                subStringNumber = subStringNumber.replace(" ", "#").split("#")[0];
                FileUtil.writeToFile(subStringNumber);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, e.toString(), e);
            }
        }
    }

    public void executeShellCommand(String command) throws IOException {
        try {
            LOGGER.info("-----------------------------------------------------------------");
            LOGGER.info("Executing " + command.toString());
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            BufferedReader buf = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "";
            while ((line = buf.readLine()) != null) {
                System.out.println(line);
            }
            LOGGER.info("-----------------------------------------------------------------");

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void executeShellCommand2(String command) throws IOException {
        try {
            LOGGER.info("-----------------------------------------------------------------");
            LOGGER.info("Executing " + command.toString());
            Process p = Runtime.getRuntime().exec(command);

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(p.getErrorStream()));

            // read the output from the command
            String s = null;
            System.out.println("Standard output of the command:\n");
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }

            // read any errors from the attempted command
            System.out.println("Standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }
            LOGGER.info("-----------------------------------------------------------------");

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }

    public List<String> getCommandList(Integer lastDate) {
        Map<String, String> listOfRange = getRangeOfDates(INITIAL_YEAR, FINAL_YEAR);
        final Set<String> keys = listOfRange.keySet();
        List<String> commands = new ArrayList();
        for (String key : keys) {
            String command =
                    "sudo python3 /home/iiadmin/projetos/persona/_001_face_vector_prod_multi.py " + key + " "
                            + listOfRange.get(key)
                            + " civil ";

            lastDate = lastDate != null ? lastDate : LAST_DATE;
            if (Integer.parseInt(key) >= lastDate) {
                commands.add(command);
            }
            ++countTry;
        }
        return commands;
    }

    public List<String> getCommandList() {
        return getCommandList(LAST_DATE);
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


            int currentDateCompare = Integer.parseInt(dateFormat.format(currentDatePlusOneDay));

            if (currentDateCompare <= LAST_DATE) {
                stringStringMap
                        .put(dateFormat.format(currentDate), dateFormat.format(currentDatePlusOneDay));
            }
            currentDate = currentDatePlusOneDay;

            initialYearTemp = Integer.parseInt(dateFormat.format(currentDate).substring(0, 4));
        }
        return stringStringMap;
    }

    public List<String> getCommandList(int startDate, int endDate) {
        Map<String, String> listOfRange = getRangeOfDates(startDate, endDate);
        final Set<String> keys = listOfRange.keySet();
        List<String> commands = new ArrayList();
        for (String key : keys) {
            String command =
                    "sudo python3 /srv/projetos/persona/_001_face_vector_prod_multi.py " + key + " "
                            + listOfRange.get(key)
                            + " civil ";
            commands.add(command);
            ++countTry;
        }
        return commands;
    }
}
