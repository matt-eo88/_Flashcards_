package flashcards;

import java.util.Map;
import java.util.Random;
import java.util.Scanner;

class FlashInterface {

    private final Scanner scan;
    private CardManager cardManager;
    private boolean systemOn;

    FlashInterface() {
        this.scan = new Scanner(System.in);
        this.cardManager = new CardManager();
        this.systemOn = true;
    }

    public void startMenu() {
        cardManager.clearCards();
        cardManager.loadCardsOnStart();
        while (systemOn) {
            String menu = "Input the action (add, remove, import, export, ask, log, hardest card, reset stats, exit):";
            System.out.println(menu);
            Logger.add(menu);
            String input = scan.nextLine();
            Logger.add(input);
            switch (input) {
                case "add":
                    createCards();
                    break;
                case "remove":
                    removeCard();
                    break;
                case "import":
                    importFile();
                    break;
                case "export":
                    exportToFile();
                    break;
                case "ask":
                    play();
                    break;
                case "log":
                    Logger.loggerOutToFile();
                    break;
                case "hardest card":
                    cardManager.hardestCard();
                    break;
                case "reset stats":
                    cardManager.resetStats();
                    break;
                case "exit":
                    System.out.println("Bye bye!");
                    cardManager.saveOnExit();
                    systemOn = false;
                    break;
                default:
                    System.out.println("Unknown action.");
            }
        }

    }

    private void createCards() {
        System.out.println("The card:");
        Logger.add("The card:");
        String term = scan.nextLine();
        Logger.add(term);
        if (cardManager.containsTerm(term)) {
            String output = "The card \"" + term + "\" already exists.";
            System.out.println(output);
            System.out.println();
            Logger.add(output);
            return;
        }

        System.out.println("The definition of the card:");
        Logger.add("The definition of the card:");
        String definition = scan.nextLine();
        Logger.add(definition);
        if (cardManager.containsDefinition(definition)) {
            String output = "The definition \"" + definition + "\" already exists.";
            System.out.println(output);
            System.out.println();
            Logger.add(output);
            return;
        }

        cardManager.addCard(term, definition);
        cardManager.addToMistakes(term, 0);
        String output = "The pair (\"" + term + "\":\"" + definition + "\") has been added.";
        System.out.println(output);
        System.out.println();
    }

    private void removeCard() {
        System.out.println("The card:");
        Logger.add("The card:");
        String card = scan.nextLine();
        Logger.add(card);
        cardManager.remove(card);
    }

    private void importFile() {
        System.out.println("File name:");
        Logger.add("File name:");
        String fileName = scan.nextLine();
        Logger.add(fileName);
        cardManager.importCards(fileName);
    }

    private void exportToFile() {
        System.out.println("File name:");
        String fileName = scan.nextLine();
        cardManager.exportCards(fileName);
    }

    private void play() {
        askQuestions();
    }


    private void askQuestions() {
        Map<String, String> cards = cardManager.getCards();
        Object[] terms = cards.keySet().toArray();
        Random random = new Random();
        System.out.println("How many times to ask?");
        Logger.add("How many times to ask?");
        int t = Integer.parseInt(scan.nextLine());
        for (int i = 0; i < t; i++) {
            String term = String.valueOf(terms[random.nextInt(terms.length)]);
            String definition = cards.get(term);
            String output = String.format("Print the definition of \"%s\":\n", term);
            System.out.print(output);
            Logger.add(output);
            String answer = scan.nextLine();
            Logger.add(answer);
            if (answer.equalsIgnoreCase(definition)) {
                String correct = "Correct answer.";
                System.out.println(correct);
                Logger.add(correct);
            } else {
                cardManager.addToMistakes(term, cardManager.getFromMistakes(term) + 1);
                String wrong = String.format(cards.containsValue(answer) ?
                        "Wrong. The right answer is \"%1$s\", but your definition is correct for \"%2$s\".\n" :
                        "Wrong. The right answer is \"%1$s\".\n", definition, getKeyFromValue(cards, answer));
                System.out.print(wrong);
                Logger.add(wrong);
            }
        }
    }

    private static String getKeyFromValue(Map<String, String> map, String value) {
        for (var entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

}
