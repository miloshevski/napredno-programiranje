package lab5.library;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

class Book{
    private String isbn;
    private String title;
    private int year;
    private String author;
    private int totalborows;

    public Book(String isbn, String title,String author, int year) {
        this.isbn = isbn;
        this.title = title;
        this.year = year;
        this.author = author;
        totalborows = 0;
    }

    public String getAuthor() {
        return author;
    }

    public void borrowbook(){
        totalborows++;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public int getTotalborows() {
        return totalborows;
    }
}

class Member{
    private String id;
    private String name;
    private int totalBorrows;
    private int currentlyBorrowed;
    public Member(String id, String name) {
        this.id = id;
        this.name = name;
        totalBorrows = 0;
        currentlyBorrowed = 0;
    }

    public void borrow(){
        totalBorrows++;
        currentlyBorrowed++;
    }
    public void returnBook(){
        currentlyBorrowed--;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getTotalBorrows() {
        return totalBorrows;
    }

    public int getCurrentlyBorrowed() {
        return currentlyBorrowed;
    }

    @Override
    public String toString() {
        return String.format("%s (%s) - borrowed now: %d, total borrows: %d",name,id,currentlyBorrowed,totalBorrows);
    }
}

class LibrarySystem{
    private String name;
    private Map<String, Member> membermap;
    private Map<String, Integer> memberBorrows;
    private Map<String, Book> bookmap;
    private Map<String,Integer> bookcopies;
    private Map<String,Queue<String>> queuemap;
    private Map<String,Integer> bookborows;
    private Map<String,Set<String >> bookBorowers;

    public LibrarySystem(String name) {
        this.name = name;
        membermap = new HashMap<>();
        memberBorrows = new HashMap<>();
        bookmap = new HashMap<>();
        bookcopies = new HashMap<>();
        queuemap = new HashMap<>();
        bookborows = new HashMap<>();
        bookBorowers = new HashMap<>();
    }

    public void registerMember(String id, String fullName){
        Member m = new Member(id,fullName);
        membermap.put(id,m);
        memberBorrows.put(id,0);
    }

    public void addBook(String isbn, String title, String author, int year){
        Book b = new Book(isbn, title,author, year);
        if(!bookmap.containsKey(isbn)){
            bookmap.put(isbn,b);
            bookcopies.put(isbn,0);
        }
        bookcopies.put(isbn,bookcopies.get(isbn) + 1);
        bookborows.put(isbn,0);
        bookBorowers.put(isbn,new HashSet<>());
    }
    public void borrowBook(String memberId, String isbn){
        if(!bookmap.containsKey(isbn)){
            return;
        }
        if(bookcopies.get(isbn) == 0){
            if(!queuemap.containsKey(isbn)){
                Queue<String> q = new ArrayDeque<>();
                q.add(memberId);
                queuemap.put(isbn,q);
            }
            else{
                queuemap.get(isbn).add(memberId);
            }
        }else {
            Member m = membermap.get(memberId);
            m.borrow();
            bookborows.put(isbn,bookborows.get(isbn) + 1);
            bookcopies.put(isbn,bookcopies.get(isbn) - 1);
            bookmap.get(isbn).borrowbook();
            bookBorowers.get(isbn).add(memberId);
        }
    }
    public void returnBook(String memberId, String isbn){
        Member m = membermap.get(memberId);
        m.returnBook();
        bookBorowers.get(isbn).remove(memberId);
        bookcopies.put(isbn,bookcopies.get(isbn) + 1);
        Queue<String> qq = queuemap.get(isbn);
        if(qq != null && !qq.isEmpty()){
            Member mm = membermap.get(qq.poll());
            borrowBook(mm.getId(),isbn);
        }
    }

    public void printMembers(){
        membermap.values().stream().sorted(Comparator.comparing(Member::getCurrentlyBorrowed).reversed().thenComparing(Member::getName))
                .forEach(System.out::println);
    }
    public void printBooks() {
        bookmap.values().stream()
                .sorted(Comparator.comparingInt(Book::getTotalborows).reversed()
                        .thenComparingInt(Book::getYear))
                .forEach(b -> {
                    System.out.printf("%s - \"%s\" by %s (%d), available: %d, total borrows: %d%n",
                            b.getIsbn(),
                            b.getTitle(),
                            b.getAuthor(),
                            b.getYear(),
                            bookcopies.getOrDefault(b.getIsbn(), 0),
                            b.getTotalborows());
                });
    }


    public void printBookCurrentBorrowers(String isbn){
        StringBuffer sb = new StringBuffer();
        if(bookBorowers.get(isbn).isEmpty()){
            return;
        }
        bookBorowers.get(isbn).stream().sorted().forEach(s ->  {
            sb.append(s).append(" ");
        });
        String s = sb.toString().trim().replaceAll(" ",", ");
        System.out.println(s);
    }
    public void printTopAuthors() {
        Map<String, Integer> m = bookmap.values().stream()
                .collect(Collectors.groupingBy(
                        Book::getAuthor,
                        Collectors.summingInt(Book::getTotalborows)
                ));

        m.entrySet().stream()
                .sorted(
                        Comparator.<Map.Entry<String, Integer>>comparingInt(Map.Entry::getValue)
                                .reversed()
                                .thenComparing(Map.Entry::getKey)
                )
                .forEach(e -> System.out.printf("%s - %d%n", e.getKey(), e.getValue()));
    }


}

public class LibraryTester {
    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        try {
            String libraryName = br.readLine();
            //   System.out.println(libraryName); //test
            if (libraryName == null) return;

            libraryName = libraryName.trim();
            LibrarySystem lib = new LibrarySystem(libraryName);

            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.equals("END")) break;
                if (line.isEmpty()) continue;

                String[] parts = line.split(" ");

                switch (parts[0]) {

                    case "registerMember": {
                        lib.registerMember(parts[1], parts[2]);
                        break;
                    }

                    case "addBook": {
                        String isbn = parts[1];
                        String title = parts[2];
                        String author = parts[3];
                        int year = Integer.parseInt(parts[4]);
                        lib.addBook(isbn, title, author, year);
                        break;
                    }

                    case "borrowBook": {
                        lib.borrowBook(parts[1], parts[2]);
                        break;
                    }

                    case "returnBook": {
                        lib.returnBook(parts[1], parts[2]);
                        break;
                    }

                    case "printMembers": {
                        lib.printMembers();
                        break;
                    }

                    case "printBooks": {
                        lib.printBooks();
                        break;
                    }

                    case "printBookCurrentBorrowers": {
                        lib.printBookCurrentBorrowers(parts[1]);
                        break;
                    }

                    case "printTopAuthors": {
                        lib.printTopAuthors();
                        break;
                    }

                    default:
                        break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
