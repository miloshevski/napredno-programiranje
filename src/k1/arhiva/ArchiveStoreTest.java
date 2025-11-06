//package k1.arhiva;
//
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Scanner;
//
//class NonExistingItemException extends Exception{
//    public NonExistingItemException(String message) {
//        super(message);
//    }
//}
//
//class ArchiveStore{
//    private List<Archive> list;
//    private StringBuilder log;
//
//    public ArchiveStore(){
//        this.list = new ArrayList<>();
//        this.log = new StringBuilder();
//    }
//
//    public void archiveItem(Archive item, LocalDate date){
//        item.setDateArchieved(date);
//        list.add(item);
//        log.append(String.format("Item %d archived at %s%n",item.getId(),date.toString()));
//    }
//    public void openItem(int id, LocalDate date) throws NonExistingItemException {
//        Archive item = list.stream().filter(i -> i.getId() == id).findFirst().orElse(null);
//        if(item == null){
//            throw new NonExistingItemException(String.format("Item with id %d doesn't exist", id));
//        }else if(item instanceof LockedArchive e){
//            if(date.isBefore(((LockedArchive) item).getDateToOpen())){
//                log.append(String.format("Item %d cannot be opened before %s%n",item.getId(),((LockedArchive) item).getDateToOpen().toString()));
//            }else{
//                log.append(String.format("Item %d opened at %s%n", item.getId(), date.toString()));
//            }
//        }else {
//            if (((SpecialArchive) item).getCurrentOpen() == ((SpecialArchive) item).getMaxOpen()) {
//                log.append(String.format("Item %d cannot be opened more than %d times%n", item.getId(), ((SpecialArchive) item).getMaxOpen()));
//            } else {
//                log.append(String.format("Item %d opened at %s%n", item.getId(), date.toString()));
//                ((SpecialArchive) item).increaseCurrentOpen();
//            }
//        }
//    }
//    public String getLog(){
//        return log.toString();
//    }
//}
//abstract class Archive{
//    private int id;
//    private LocalDate dateArchieved;
//
//    public Archive(int id){
//        this.id = id;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public LocalDate getDateArchieved() {
//        return dateArchieved;
//    }
//
//    public void setDateArchieved(LocalDate dateArchieved) {
//        this.dateArchieved = dateArchieved;
//    }
//}
//
//class LockedArchive extends Archive{
//    private LocalDate dateToOpen;
//
//    public LockedArchive(int id, LocalDate dateToOpen) {
//        super(id);
//        this.dateToOpen = dateToOpen;
//    }
//
//    public LocalDate getDateToOpen() {
//        return dateToOpen;
//    }
//}
//class SpecialArchive extends Archive{
//    private int maxOpen;
//    private int currentOpen;
//
//    public SpecialArchive(int id,int maxOpen) {
//        super(id);
//        this.maxOpen = maxOpen;
//        this.currentOpen = 0;
//    }
//    public void increaseCurrentOpen(){
//        currentOpen++;
//    }
//    public int getMaxOpen() {
//        return maxOpen;
//    }
//
//    public int getCurrentOpen() {
//        return currentOpen;
//    }
//}
//
//public class ArchiveStoreTest {
//    public static void main(String[] args) {
//        ArchiveStore store = new ArchiveStore();
//        Date date = new Date(113, 10, 7);
//        Scanner scanner = new Scanner(System.in);
//        scanner.nextLine();
//        int n = scanner.nextInt();
//        scanner.nextLine();
//        scanner.nextLine();
//        int i;
//        for (i = 0; i < n; ++i) {
//            int id = scanner.nextInt();
//            long days = scanner.nextLong();
//            LocalDate dateToOpen = new LocalDate(date.getTime() + (days * 24 * 60
//                    * 60 * 1000));
//            LockedArchive lockedArchive = new LockedArchive(id,dateToOpen);
//            store.archiveItem(lockedArchive, date);
//        }
//        scanner.nextLine();
//        scanner.nextLine();
//        n = scanner.nextInt();
//        scanner.nextLine();
//        scanner.nextLine();
//        for (i = 0; i < n; ++i) {
//            int id = scanner.nextInt();
//            int maxOpen = scanner.nextInt();
//            SpecialArchive specialArchive = new SpecialArchive(id, maxOpen);
//            store.archiveItem(specialArchive, date);
//        }
//        scanner.nextLine();
//        scanner.nextLine();
//        while(scanner.hasNext()) {
//            int open = scanner.nextInt();
//            try {
//                store.openItem(open, date);
//            } catch(NonExistingItemException e) {
//                System.out.println(e.getMessage());
//            }
//        }
//        System.out.println(store.getLog());
//    }
//}
//
//// вашиот код овде
//
//
