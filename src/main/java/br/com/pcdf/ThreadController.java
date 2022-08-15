package br.com.pcdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ThreadController {

  private static int firstValue = 0;
  private static int lastValue = 10;


  public static void main(String[] args) {
    ThreadController threadController = new ThreadController();
    threadController.execute();
  }

  private void execute() {
    CommandsList commandsList = new CommandsList();
    final List<String> commandList = commandsList.getCommandList();
    executeThreads(commandList);
  }

  private void executeThreads(final List<String> commandList) {

    List<String> list = commandList.subList(firstValue, lastValue);
    final List<Thread> threadList = list.stream()
        .map(c -> new Worker(c))
        .map(c -> new Thread(c))
        .collect(Collectors.toList());

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

    int newLastValue = firstValue + lastValue;
    int newFinal = lastValue + lastValue;

    if (newFinal <= commandList.size() && newLastValue < commandList.size()) {
      final List<String> strings = commandList.subList(newLastValue, newFinal);
      executeThreads(strings);
    }
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
        commandsList.executeShellCommand(command);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

}
