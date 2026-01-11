package observer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

interface User{
    void notify(String mailingListName,String text);
}

interface MailingList{
    void subscribe(User user);
    void unsubscribe(User user);
    void publish(String text);
}

abstract class AbstractUser implements User{
    protected String name;
    private String email;

    public AbstractUser(String name, String email) {
        this.name = name;
        this.email = email;
    }
}

class MailingListUser extends AbstractUser implements User{

    public MailingListUser(String name, String email) {
        super(name, email);
    }

    @Override
    public void notify(String mailingListName, String text) {
        System.out.printf("[USER] %s received email from %s: %s%n",name,mailingListName,text);
    }
}

class FilteredMailingListUser extends AbstractUser implements User{
    private String word;

    public FilteredMailingListUser(String name, String email, String word) {
        super(name, email);
        this.word = word;
    }

    private boolean filteredText(String text){
        return text.toLowerCase().contains(word.toLowerCase());
    }

    @Override
    public void notify(String mailingListName, String text) {
        if(filteredText(text)){
            System.out.printf("[FILTERED USER] %s received filtered email from %s: %s%n",name,mailingListName,text);
        }
    }
}

class AdminUser extends AbstractUser implements User{

    public AdminUser(String name, String email) {
        super(name, email);
    }

    @Override
    public void notify(String mailingListName, String text) {
        System.out.printf("[ADMIN LOG] MailingList=%s | Message=%s%n",mailingListName,text);
    }
}

abstract class AbstractMailingList implements MailingList{
    protected String email;
    protected List<User> users;

    public AbstractMailingList(String email) {
        this.email = email;
        this.users = new ArrayList<>();
    }
}

class SimpleMailingList extends AbstractMailingList implements MailingList{

    public SimpleMailingList(String email) {
        super(email);
    }

    @Override
    public void subscribe(User user) {
        users.add(user);
    }

    @Override
    public void unsubscribe(User user) {
        users.remove(user);
    }

    @Override
    public void publish(String text) {
        for(User u : users){
            u.notify(email,text);
        }
    }
}



public class MailingListTest {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(br.readLine());

        Map<String, MailingList> mailingLists = new HashMap<>();
        Map<String, User> usersByEmail = new HashMap<>();

        for (int i = 0; i < n; i++) {
            String line = br.readLine();
            String[] parts = line.split(" ");

            String command = parts[0];

            switch (command) {

                case "CREATE_LIST": {
                    String listName = parts[1];
                    mailingLists.put(listName, new SimpleMailingList(listName));
                    break;
                }

                case "ADD_USER": {
                    String listName = parts[1];
                    String type = parts[2];
                    String name = parts[3];
                    String email = parts[4];

                    User user;
                    if (type.equals("NORMAL")) {
                        user = new MailingListUser(name, email);
                    } else if (type.equals("FILTERED")) {
                        String keyword = parts[5];
                        user = new FilteredMailingListUser(name, email, keyword);
                    } else { // ADMIN
                        user = new AdminUser(name, email);
                    }

                    usersByEmail.put(email, user);
                    mailingLists.get(listName).subscribe(user);
                    break;
                }

                case "REMOVE_USER": {
                    String listName = parts[1];
                    String email = parts[2];

                    User user = usersByEmail.get(email);
                    mailingLists.get(listName).unsubscribe(user);
                    break;
                }

                case "PUBLISH": {
                    String listName = parts[1];
                    String text = line.substring(
                            line.indexOf(listName) + listName.length() + 1
                    );
                    mailingLists.get(listName).publish(text);
                    break;
                }
            }
        }
    }
}
