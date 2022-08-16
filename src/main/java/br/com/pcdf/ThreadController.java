package br.com.pcdf;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.ListUtils;

public class ThreadController {

  private int firstValue = 0;
  private int lastValue = 10;
  private static final int THREAD_QUANTITY = 1200;


  public static void main(String[] args) {
    ThreadController threadController = new ThreadController();
    threadController.execute();
  }

  private void execute() {
    CommandsList commandsList = new CommandsList();
    final List<String> commandList = commandsList.getCommandList();

    List<List<String>> output = ListUtils
        .partition(commandList, 100);

    output.forEach(sublist -> executeThreads(sublist));
  }

  private void executeThreads(final List<String> commandList) {

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
