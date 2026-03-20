package org.example.tute2.library.exceptions;

public class BookAlreadyBorrowedException extends Exception {
  public BookAlreadyBorrowedException(String message) {
    super(message);
  }
}
