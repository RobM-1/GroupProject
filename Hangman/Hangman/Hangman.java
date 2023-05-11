package Hangman;


import java.util.Scanner;

public class Hangman {
  public static void main(String[] args) {
      Scanner guess = new Scanner(System.in);
      System.out.println("Hello, and welcome to Hangman!");
      System.out.println("Difficulty(1-3): ");
      int diff = guess.nextInt();
      guess.nextLine();

      System.out.println("Choose a theme(food, countries, animals): ");
      String theme = guess.nextLine();

      Play obj = new Play();
      obj.Start(diff, theme);
  }
}