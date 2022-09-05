package br.com.pcdf;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.apache.commons.collections4.ListUtils;

public class ThreadController {



    private static final int START_DATE = 1900;

    private static final int END_DATE = 2009;
    private static final int THREAD_QUANTITY = 5;

    private static final Logger LOGGER = Logger.getLogger(
            Thread.currentThread().getStackTrace()[0].getClassName());


    public static void main(String[] args) {
        ThreadController threadController = new ThreadController();
        threadController.execute();
    }

    private void execute() {
        CommandsList commandsList = new CommandsList();
        List<String> commandList = commandsList.getCommandList(START_DATE, END_DATE);
        commandList.sort(Collections.reverseOrder());

        List<List<String>> listOfThreads = ListUtils
                .partition(commandList, THREAD_QUANTITY);
        int i = 0;
        listOfThreads.forEach(thread -> {
            executeThread(thread, i, listOfThreads.size());
        });
    }

    private void executeThread(final List<String> commandList, int threadNumber,
                               int quantityOfThreads) {

        final List<Thread> threadList = commandList.stream()
                .map(c -> new Worker(c))
                .map(c -> new Thread(c))
                .collect(Collectors.toList());

        threadList.forEach(thread -> thread.start());

        boolean isProcessing = false;
        do {
            isProcessing = false;
            for (Thread t : threadList) {
                if (t.isAlive()) {
                    isProcessing = true;
                    break;
                }
            }
        } while (isProcessing);

    }

    public static class Worker implements Runnable {

        private String command;

        public Worker(String command) {
            this.command = command;
        }

        @Override
        public void run() {
            CommandsList commandsList = new CommandsList();
            try {
                commandsList.executeShellCommand2(command);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
