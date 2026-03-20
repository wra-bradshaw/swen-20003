package org.example.tute2.library;

import java.util.Optional;
import org.example.tute2.library.exceptions.BookAlreadyBorrowedException;

public class Book {
  enum Status {
    BORROWED,
    NOT_BORROWED
  }

  private String author;
  private String title;
  private String borrowedBy;
  private Status status;

  public Book(String author, String title) {
    this(author, title, Status.NOT_BORROWED, null);
  }

  public Book(String author, String title, Status status, String borrowedBy) {
    this.author = author;
    this.title = title;

    if (status == Status.BORROWED) {
      if (borrowedBy == null || borrowedBy.trim().isEmpty()) {
        throw new IllegalArgumentException("Borrower name cannot be empty");
      }

      this.status = Status.BORROWED;
      this.borrowedBy = borrowedBy;
    } else {
      this.status = Status.NOT_BORROWED;
    }
  }

  public String getAuthor() {
    return this.author;
  }

  public String getTitle() {
    return this.title;
  }

  public Status getStatus() {
    return this.status;
  }

  public Optional<String> getBorrowedBy() {
    return Optional.ofNullable(this.borrowedBy);
  }

  void borrow(String borrowedBy)
      throws BookAlreadyBorrowedException, IllegalArgumentException {
    if (this.status == Status.BORROWED) {
      throw new BookAlreadyBorrowedException(this.title + " has already been borrowed.");
    }
    if (borrowedBy == null || borrowedBy.trim().isEmpty()) {
      throw new IllegalArgumentException("Borrower name cannot be empty");
    }

    this.status = Status.BORROWED;
    this.borrowedBy = borrowedBy;
  }

  void returnBook() {
    this.status = Status.NOT_BORROWED;
    this.borrowedBy = null;
  }

  @Override
  public String toString() {
    return "Book{ author: "
        + this.author
        + ", title: "
        + this.title
        + ", status: "
        + this.status
        + " }";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }

    Book b = (Book) o;
    return b.author.equals(this.author) && b.title.equals(this.title) && b.status == this.status;
  }
}
