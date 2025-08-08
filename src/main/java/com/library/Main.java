package com.library;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n==== Library Menu ====");
            System.out.println("1. Add Book");
            System.out.println("2. Register Member");
            System.out.println("3. Borrow Book");
            System.out.println("4. Return Book");
            System.out.println("5. View Borrow Records");
            System.out.println("6. List All Books");
            System.out.println("7. List All Members");
            System.out.println("8. Search Books");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1": {
                    System.out.println("=== Add a New Book ===");
                    System.out.print("Enter book title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter author name: ");
                    String author = scanner.nextLine();
                    System.out.print("Enter ISBN: ");
                    String isbn = scanner.nextLine();

                    Book book = new Book(title, author, isbn);
                    book.saveToDatabase();
                    break;
                }

                case "2": {
                    System.out.println("=== Register a Member ===");
                    System.out.print("Enter member name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter member ID: ");
                    String memberId = scanner.nextLine();

                    Member member = new Member(name, memberId);
                    member.saveToDatabase();
                    break;
                }
                case "3": {
    System.out.println("=== Borrow a Book ===");
    System.out.print("Enter Member ID: ");
    String memberId = scanner.nextLine();
    System.out.print("Enter Book ISBN: ");
    String isbn = scanner.nextLine();

    BorrowRecord.borrowBook(memberId, isbn);
    break;
}

case "4": {
    System.out.println("=== Return a Book ===");
    System.out.print("Enter Member ID: ");
    String memberId = scanner.nextLine();
    System.out.print("Enter Book ISBN: ");
    String isbn = scanner.nextLine();

    BorrowRecord.returnBook(memberId, isbn);
    break;
}
    case "5": {
    System.out.println("=== Borrow Records ===");
    BorrowRecord.viewAllRecords();
    break;
}
    case "6":
    Book.listAllBooks();
    break;
    case "7":
    Member.listAllMembers();
    break;
    case "8": {
    System.out.print("Enter keyword (title or ISBN): ");
    String keyword = scanner.nextLine();
    Book.searchBooks(keyword);
    break;
}

                case "0":
                    running = false;
                    System.out.println("👋 Exiting Library System...");
                    break;

                default:
                    System.out.println("❌ Invalid option. Try again.");
            }
        }

        scanner.close();
    }
}