//package k1.phonebook;
//
//import java.util.*;
//
//class Contact implements Comparable<Contact>{
//    private String name;
//    private String number;
//
//    public Contact(String name, String number) {
//        this.name = name;
//        this.number = number;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public String getNumber() {
//        return number;
//    }
//
//    @Override
//    public int compareTo(Contact o) {
//        return number.compareTo(o.number);
//    }
//}
//
//class DuplicateNumberException extends Exception{
//    public DuplicateNumberException(String message) {
//        super(String.format("Duplicate number: %s",message));
//    }
//}
//
//class PhoneBook{
//    private Map<String, List<Contact>> map;
//
//    public PhoneBook() {
//        this.map = new TreeMap<>();
//    }
//
//    public void addContact(String name, String number) throws DuplicateNumberException {
//        map.values().stream()
//                .flatMap(Collection::stream)
//                .forEach(c -> {
//                    if(c.getNumber().equals(number)){
//                        try {
//                            throw new DuplicateNumberException(number);
//                        } catch (DuplicateNumberException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }
//                });
//        map.computeIfAbsent(name,k -> new ArrayList<>()).add(new Contact(name,number));
//    }
//
//
//}
//
//public class PhoneBookTest {
//
//    public static void main(String[] args) {
//        PhoneBook phoneBook = new PhoneBook();
//        Scanner scanner = new Scanner(System.in);
//        int n = scanner.nextInt();
//        scanner.nextLine();
//        for (int i = 0; i < n; ++i) {
//            String line = scanner.nextLine();
//            String[] parts = line.split(":");
//            try {
//                phoneBook.addContact(parts[0], parts[1]);
//            } catch (DuplicateNumberException e) {
//                System.out.println(e.getMessage());
//            }
//        }
//        while (scanner.hasNextLine()) {
//            String line = scanner.nextLine();
//            System.out.println(line);
//            String[] parts = line.split(":");
//            if (parts[0].equals("NUM")) {
//                phoneBook.contactsByNumber(parts[1]);
//            } else {
//                phoneBook.contactsByName(parts[1]);
//            }
//        }
//    }
//
//}
//
//// Вашиот код овде
//
