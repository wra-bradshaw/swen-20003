package org.example.tute2.library;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.example.tute2.library.exceptions.BookAlreadyBorrowedException;
import org.example.tute2.library.exceptions.LibraryFullException;
import org.junit.jupiter.api.Test;

public class LibraryTest {
  @Test
  public void borrowBookUpdatesBorrowedCount()
      throws LibraryFullException, BookAlreadyBorrowedException {
    Library library = new Library();
    library.addBook(new Book("Ursula K. Le Guin", "A Wizard of Earthsea"));
    Book book = library.lookup("A Wizard of Earthsea").get();

    library.borrowBook(book, "Ged");

    assertEquals(1, library.getBorrowedCount());
    assertEquals(
        Book.Status.BORROWED, library.lookup("A Wizard of Earthsea").orElseThrow().getStatus());
  }

  @Test
  public void libraryFull() {
    Library library = new Library();
    Book book = new Book("goof book", "guy barnett");

    assertDoesNotThrow(
        () -> {
          library.addBook(book);
          library.addBook(book);
          library.addBook(book);
          library.addBook(book);
          library.addBook(book);
          library.addBook(book);
          library.addBook(book);
          library.addBook(book);
          library.addBook(book);
          library.addBook(book);
        },
        "Adding 10 books should not throw");

    assertThrows(
        LibraryFullException.class,
        () -> {
          library.addBook(book);
        },
        "Adding 11 books should throw");
  }

  @Test
  void libraryCatalogue() {
    Library library = new Library();
    Book book = new Book("goof book", "guy barnett");

    assertDoesNotThrow(
        () -> {
          library.addBook(book);
          library.addBook(book);
          library.addBook(book);
          library.addBook(book);
          library.addBook(book);
          library.addBook(book);
          library.addBook(book);
          library.addBook(book);
          library.addBook(book);
          library.addBook(book);
        },
        "Adding 10 books should not throw");

    assertEquals(
        library.getCatalogue(),
        "Book{ author: goof book, title: guy barnett, status: NOT_BORROWED }\n".repeat(10));

    assertDoesNotThrow(
        () -> library.borrowBook(book, "anthony albanese"), "Non borrowed book shouldn't throw");

    assertEquals(
        library.getCatalogue(),
        "Book{ author: goof book, title: guy barnett, status: BORROWED }\n".repeat(10));

    System.out.println(library.getCatalogue());
  }
}
