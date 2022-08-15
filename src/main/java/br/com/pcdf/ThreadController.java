package br.com.pcdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ThreadController {

  private static int firstValue = 0;
  private static int lastValue = 10;

  public static void main(String[] args) {
    ThreadController threadController = new ThreadController();
    threadController.tet();
  }

  private void tet() {
    List testlist = new ArrayList();
    testlist.add("test1");
    testlist.add("test2");
  }

  private void execute() {
    int[] numbers = new int[100000];
    Random rnd = new Random();
    for (int index = 0; index < numbers.length; index++) {
      numbers[index] = rnd.nextInt();
    }
    Thread[] threads = new Thread[10];
    Worker[] workers = new Worker[10];
    int range = numbers.length / 10;
    for (int index = 0; index < 10; index++) {
      int startAt = index * range;
      int endAt = startAt + range;
      //workers[index] = new Worker(startAt, endAt, numbers);
    }
    for (int index = 0; index < 10; index++) {
      threads[index] = new Thread(workers[index]);
      threads[index].start();
    }
    boolean isProcessing = false;
    do {
      isProcessing = false;
      for (Thread t : threads) {
        if (t.isAlive()) {
          isProcessing = true;
          break;
        }
      }
    } while (isProcessing);
    for (Worker worker : workers) {
     // System.out.println("Max = " + worker.getMax());
    }
  }

  private void execute2() {
    CommandsList commandsList = new CommandsList();
    final List<String> commandList = commandsList.getCommandList();

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

    list = commandList.subList(firstValue + lastValue, lastValue + lastValue);
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
