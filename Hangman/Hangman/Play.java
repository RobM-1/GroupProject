package Hangman;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Play {
    private String[] words;
    private String[] hints;

    public void loadWordsFromFile(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("words/"+ filename));
        String line;
        int index = 0;
        words = new String[26];

        while ((line = reader.readLine()) != null && index < 26) {
            words[index] = line.trim();
            index++;
        }

        reader.close();
    }

    public void loadHintsFromFile(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("hints/" + filename));
        String line;
        int index = 0;
        hints = new String[26];

        while ((line = reader.readLine()) != null && index < 26) {
            hints[index] = line.trim();
            index++;
        }

        reader.close();
    }

    public void Start(int diff, String theme) {
        try {
            loadWordsFromFile(theme + "words" + diff + ".txt");
            loadHintsFromFile(theme + "hints" + diff + ".txt");
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file: " + e.getMessage());
            return;
        }

        String word;
        Random rand = new Random();
        int w1 = rand.nextInt(26);
        word = words[w1];
        String hint = hints[w1];
        String guess = "";
        int wl = word.length() + 1;
        for (int i = 1; i < wl; i++) {
            guess += "-";
        }

        String lu = "";
        int tt = 0;
        boolean canPlay = true;
        Main(guess, tt, canPlay, lu, word, hint);
    }

    public void Main(String guess, int tt, boolean canPlay, String lu, String word, String hint) {
        if (tt == 6) {
            canPlay = false;
            Lose();
        } else {

            String man[] = new String[7];
            man[0] = " --\n   |\n   |\n   |\n_____\n";
            man[1] = " --\n o |\n   |\n   |\n_____\n";
            man[2] = " --\n o |\n/  |\n   |\n_____\n";
            man[3] = " --\n o |\n/| |\n   |\n_____\n";
            man[4] = " --\n o |\n/|\\|\n   |\n_____\n";
            man[5] = " --\n o |\n/|\\|\n/  |\n_____\n";
            man[6] = " --\n o |\n/|\\|\n/ \\|\n_____\n";
            char g1[] = guess.toCharArray();
            char w2[] = word.toCharArray();
            System.out.println(man[0]);
            for (int x = 0; x < g1.length; x++) {
                System.out.print(g1[x]);
            }
            System.out.println();
            Guess(guess, tt, canPlay, lu, word, g1, w2, man, hint, tt);
        }
    }

    private int revealRandomLetter(char g1[], char w2[]) {
        Random rand = new Random();
        int index = -1;
        do {
            index = rand.nextInt(w2.length);
        } while (g1[index] != '-');
        g1[index] = w2[index];
        return index;
    }

    public void Guess(String guess, int tt, boolean canPlay, String lu, String word, char g1[], char w2[], String man[], String hint, int lifelines) {
        String tg1 = new String(g1);
        String tg2 = new String(w2);
        if (tg1.equals(tg2)) {
            Win();
        } else {
            if (tt == 6) {
                System.out.println("\n\nYou Lost! The word was: " + word);
                Lose();
            } else {
                Scanner input = new Scanner(System.in);
                System.out.print("Guess(" + hint + "): ");
                String inputString = input.next();

                // Check if the player wants to use a lifeline
                boolean lifelineUsed = false; // Flag to indicate if a lifeline has been used

                if (inputString.equals("1") && lifelines > 0 && !lifelineUsed) {
                    lifelines--;
                    lifelineUsed = true;
                    int revealedIndex = revealRandomLetter(g1, w2);
                    System.out.println("\nA random letter has been revealed!");
                    System.out.println();
                    System.out.println(man[tt]);
                    for (int x = 0; x < g1.length; x++) {
                        System.out.print(g1[x]);
                    }
                    System.out.println("\n\n");
                    Guess(guess, tt, canPlay, lu, word, g1, w2, man, hint, lifelines);
                    return;
                } else if (inputString.equals("2") && lifelines < 3) {
                    lifelines++;
                    System.out.println("\nYou gained an extra life!");
                    System.out.println();
                    lifelineUsed = true;
                    Guess(guess, tt, canPlay, lu, word, g1, w2, man, hint, lifelines);
                    return;
                }

                // Check if the input is a single letter
                if (!lifelineUsed && (inputString.length() != 1 || !Character.isLetter(inputString.charAt(0)))) {
                    System.out.println("Invalid input. Please enter a single letter or use a lifeline.");
                    System.out.println();
                    Guess(guess, tt, canPlay, lu, word, g1, w2, man, hint, lifelines);
                    return;
                }

                String letter = inputString.toLowerCase();
                if (word.contains(letter)) {
                    if (lu.contains(letter)) {
                        tt += 1;
                        System.out.println("Wrong!");
                    } else {
                        int wl = word.length();
                        for (int i = 0; i < wl; i++) {
                            char aChar = letter.charAt(0);
                            char bChar = w2[i];
                            if (bChar == aChar) {
                                g1[i] = aChar;
                            }
                        }
                    }
                } else {
                    tt += 1;
                    System.out.println("Wrong!");
                    lu += letter;
                }

                System.out.println();
                System.out.println(man[tt]);
                for (int x = 0; x < g1.length; x++) {
                    System.out.print(g1[x]);
                }
                System.out.println("\n\n");
                lu += letter;
                Guess(guess, tt, canPlay, lu, word, g1, w2, man, hint, lifelines);
            }
        }
    }

    public void Lose() {
        Scanner input1 = new Scanner(System.in);
        System.out.print("\nPlay Again?(Y/N): ");
        String pa1 = input1.next();
        if (pa1.equalsIgnoreCase("Y")) {
            Scanner input2 = new Scanner(System.in);
            System.out.print("Enter Difficulty (1-3): ");
            int diff1 = input2.nextInt();
            System.out.print("Enter Theme (animal/fruit/sports): ");
            String theme1 = input2.next();
            Start(diff1, theme1);
        } else {
            System.out.println("Thank you for playing! Goodbye.");
            System.exit(0);
        }
    }

    public void Win() {
        System.out.println("\n\nCongratulations! You Won!");
        Scanner input1 = new Scanner(System.in);
        System.out.print("Play Again?(Y/N): ");
        String pa1 = input1.next();
        if (pa1.equalsIgnoreCase("Y")) {
            Scanner input2 = new Scanner(System.in);
            System.out.print("Enter Difficulty (1-3): ");
            int diff1 = input2.nextInt();
            System.out.print("Enter Theme (animal/fruit/sports): ");
            String theme1 = input2.next();
            Start(diff1, theme1);
        } else {
            System.out.println("Thank you for playing! Goodbye.");
            System.exit(0);
        }
    }
}


