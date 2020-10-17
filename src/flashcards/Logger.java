package flashcards;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Scanner;

abstract class Logger {

    static LinkedList<String> logger = new LinkedList<>();
    static final Scanner scanner = new Scanner(System.in);

    public static void add(String log) {
        logger.add(log);
    }

    public static void loggerOutToFile() {
        System.out.println("File name:");
        logger.add("File name:");
        String logfile = scanner.nextLine();
        logger.add(logfile);
        File file = new File(logfile);
        try (PrintWriter printWriter = new PrintWriter(file)) {
            for (var line : logger) {
                printWriter.println(line);
            }
        } catch (IOException e) {
            String error = String.format("An exception occurs %s", e.getMessage());
            System.out.println(error);
            System.out.println();
            logger.add(error);
        }
        String output = "The log has been saved.";
        logger.add(output);
        System.out.println(output);
        System.out.println();
    }

}
