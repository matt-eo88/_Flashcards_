package flashcards;

public class Main {
    public static void main(String[] args) {
        FlashInterface userInterface = new FlashInterface();
        CardManager.argsParse(args);
        userInterface.startMenu();
    }
}
