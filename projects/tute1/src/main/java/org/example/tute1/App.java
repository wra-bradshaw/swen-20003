package org.example.tute1;

import java.util.Scanner;

public class App {
  public static void main(String[] args) {
    printRAT(5);
    printDiamond(5);
    readingExercise();
  }

  static void printDiamond(int n) {
    if (n % 2 == 0) {
      throw new IllegalArgumentException("Only odd numbers allowed");
    }

    int nHashes = 1;

    for (int i = 0; i < n; i++) {
      int nSpaces = (n - nHashes) / 2;

      System.out.print(" ".repeat(nSpaces));
      System.out.print("#".repeat(nHashes));

      System.out.print("\n");

      if (i < (n - 1) / 2) {
        nHashes += 2;
      } else {
        nHashes -= 2;
      }
    }
  }

  static void printRAT(int n) {
    for (int i = 1; i < n; i++) {
      System.out.print("#".repeat(i));
      System.out.print("\n");
    }
  }

  // Write a program that reads in a single line of text from the user. It should then print out how
  // long the text is (in characters) on one line, on the next line prints out just the first word
  // of the user's text, and on the third prints all of the user's input but the first word (without
  // the space in the middle).
  //
  // Assume that words are separated by a single space character.
  static void readingExercise() {
    Scanner scanner = new Scanner(System.in);
    System.out.print("Enter some text: ");
    String line = scanner.nextLine();

    System.out.print(line.length() + "\n");

    int spaceIdx = line.indexOf(" ");
    System.out.print(line.substring(0, spaceIdx) + "\n");

    System.out.print(line.substring(spaceIdx + 1) + "\n");

    scanner.close();
  }
}
