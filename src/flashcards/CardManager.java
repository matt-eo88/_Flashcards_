package flashcards;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class CardManager {

    private Map<String, String> cards;
    private Map<String, Integer> mistakes;
    static String importFile = "";
    static String exportFile = "";

    CardManager() {
        this.cards = new LinkedHashMap<>();
        this.mistakes = new LinkedHashMap<>();
    }

    public void addCard(String term, String definition) {
        cards.put(term, definition);
    }

    public Map<String, String> getCards() {
        return cards;
    }

    public boolean containsTerm(String term) {
        return cards.containsKey(term);
    }

    public boolean containsDefinition(String definition) {
        return cards.containsValue(definition);
    }

    public void remove(String card) {
        if (!cards.containsKey(card)) {
            System.out.println("Can't remove \"" + card + "\": there is no such card.");
            System.out.println();
            return;
        }
        cards.remove(card);
        mistakes.remove(card);
        System.out.println("The card has been removed.");
        Logger.add("The card has been removed.");
        System.out.println();
    }

    public void importCards(String fileName) {
        //String path = "/home/matt/Documents/" + fileName;
        File file = new File(fileName);
        int count = 0;
        try (Scanner scan = new Scanner(file)) {
            while (scan.hasNext()) {
                String[] line;
                line = scan.nextLine().split(":");
                var term = line[0];
                var definition = line[1];
                var mistakeCount = Integer.parseInt(line[2]);
                cards.put(term, definition);
                mistakes.put(term, mistakeCount);
                count++;
            }
            System.out.println(count + " cards have been loaded.");
            System.out.println();
            Logger.add(count + " cards have been loaded.");
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            System.out.println();
            Logger.add("File not found.");
        }
    }

    public void exportCards(String fileName) {
        //String path = "/home/matt/Documents/" + fileName;
        File file = new File(fileName);
        try (PrintWriter printWriter = new PrintWriter(file)) {
            for (var entry : cards.entrySet()) {
                int mistakeCount = mistakes.get(entry.getKey());
                String line = entry.getKey() + ":" + entry.getValue() + ":" + mistakeCount;
                printWriter.println(line);
            }
            System.out.println(cards.size() + " cards have been saved.");
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println();
        }
    }

    public static void argsParse(String[] args) {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-import":
                    importFile = args[i + 1];
                    break;
                case "-export":
                    exportFile = args[i + 1];
                    break;
            }
        }
    }

    public int getFromMistakes(String key) {
        return mistakes.get(key);
    }

    public void addToMistakes(String term, int count) {
        mistakes.put(term, count);
    }

    public void loadCardsOnStart() {
        if (!importFile.isBlank()) {
            importCards(importFile);
        }
    }

    public void saveOnExit() {
        if (!exportFile.isBlank()) {
            exportCards(exportFile);
        }
    }

    public void clearCards() {
        this.cards.clear();
    }

    public void resetStats() {
        mistakes.replaceAll((k, v) -> 0);
        String output = "Card statistics has been reset.";
        System.out.println(output);
        System.out.println();
        Logger.add(output);
    }

    public void hardestCard() {
        int count = 0;
        for (var mistakeCount : mistakes.values()) {
            if (mistakeCount > count) {
                count = mistakeCount;
            }
        }

        if (count > 0) {
            Set<String> keys = new HashSet<>();
            for (var entry : mistakes.entrySet()) {
                if (entry.getValue().equals(count)) {
                    keys.add(entry.getKey());
                }
            }
            boolean moreThanOneCard = keys.size() > 1;
            if (moreThanOneCard) {
                String result = keys.stream()
                        .map(s -> "\"" + s + "\"")
                        .collect(Collectors.joining(", "));
                String output = String.format("The hardest cards are %s. You have %d errors answering them.\n", result, count);
                System.out.print(output);
                System.out.println();
                Logger.add(output);
            } else {
                String result = keys.stream()
                        .map(s -> "\"" + s + "\"")
                        .collect(Collectors.joining(""));
                String output = String.format("The hardest card is %s. You have %d errors answering it.\n", result, count);
                System.out.print(output);
                System.out.println();
                Logger.add(output);
            }
        } else {
            String output = "There are no cards with errors.";
            System.out.println(output);
            System.out.println();
            Logger.add(output);
        }
    }
}
