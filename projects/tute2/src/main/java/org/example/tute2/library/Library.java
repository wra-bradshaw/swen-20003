package org.example.tute2.library;

import java.util.Optional;
import org.example.tute2.library.exceptions.BookAlreadyBorrowedException;
import org.example.tute2.library.exceptions.LibraryFullException;

public class Library {
  private static final int CAPACITY = 10;
  private Book[] books;
  private int length;
  private int borrowedCount;

  public Library() {
    this.books = new Book[CAPACITY];
    this.length = 0;
    this.borrowedCount = 0;
  }

  public void addBook(Book book) throws LibraryFullException {
    if (this.length >= CAPACITY) {
      throw new LibraryFullException("The library is at capacity");
    }

    this.books[this.length] = book;
    this.length++;

    if (book.getStatus() == Book.Status.BORROWED) {
      this.borrowedCount++;
    }
  }

  public Optional<Book> lookup(String title) {
    for (int i = 0; i < this.length; i++) {
      if (this.books[i].getTitle().equals(title)) {
        return Optional.ofNullable(this.books[i]);
      }
    }

    return Optional.empty();
  }

  public String getCatalogue() {
    StringBuilder builder = new StringBuilder();

    for (int i = 0; i < this.length; i++) {
      builder.append(this.books[i].toString() + "\n");
    }

    return builder.toString();
  }

  public int getBorrowedCount() {
    return this.borrowedCount;
  }

  public void borrowBook(Book book, String borrowedBy)
      throws BookAlreadyBorrowedException, IllegalArgumentException {
    book.borrow(borrowedBy);
    this.borrowedCount++;
  }

  public void returnBook(Book book) {
    if (book.getStatus() == Book.Status.BORROWED) {
      book.returnBook();
      this.borrowedCount--;
    }
  }
}
