package lab5.chat;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.TreeSet;


class ChatRoom {
    private String name;
    private Set<String> users = new TreeSet<>();

    public String getName() {
        return name;
    }

    public ChatRoom(String name) {
        this.name = name;
    }

    public void addUser(String username){
        users.add(username);
    }

    public void removeUser(String username){
        users.remove(username);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        if (users.isEmpty()) {
            sb.append("\nEMPTY");
        } else {
            for (String u : users) {
                sb.append("\n").append(u);
            }
        }
        return sb.toString();
    }


    public boolean hasUser(String username){
        return users.contains(username);
    }

    public int numUsers(){
        return users.size();
    }
}

class ChatSystem{
    private Map<String,ChatRoom> map = new TreeMap<>();
    private Set<String> users = new TreeSet<>();
    public ChatSystem(){}

    public void removeRoom(String roomName){
        map.remove(roomName);
    }
    public void addRoom(String roomName){
        map.put(roomName,new ChatRoom(roomName));
    }

    public ChatRoom getRoom(String roomName) throws NoSuchRoomException {
        ChatRoom room = map.get(roomName);
        if (room == null) {
            throw new NoSuchRoomException(roomName);
        }
        return room;
    }


    public void register(String userName){
        users.add(userName);
        if(map.isEmpty()){
            return;
        }

        map.values().stream()
                .min(Comparator
                        .comparingInt(ChatRoom::numUsers)
                        .thenComparing(ChatRoom::getName))  // tie-breaker
                .ifPresent(r -> r.addUser(userName));
    }


    public void joinRoom(String userName,String roomName)
            throws NoSuchUserException, NoSuchRoomException {
        if(!users.contains(userName)){
            throw new NoSuchUserException(userName);
        }
        ChatRoom room = map.get(roomName);
        if(room == null){
            throw new NoSuchRoomException(roomName);
        }
        room.addUser(userName);
    }


    public void registerAndJoin(String userName, String roomName)
            throws NoSuchUserException, NoSuchRoomException {
        // само регистрирај го user-от
        users.add(userName);
        // па приклучи го во собата
        joinRoom(userName, roomName);
    }


    public void leaveRoom(String username, String roomName)
            throws NoSuchRoomException, NoSuchUserException {
        if(!users.contains(username)){
            throw new NoSuchUserException(username);
        }
        ChatRoom room = map.get(roomName);
        if(room == null){
            throw new NoSuchRoomException(roomName);
        }
        room.removeUser(username);
    }

    public void followFriend(String username,String friendUsername)
            throws NoSuchUserException {
        if(!users.contains(username)){
            throw new NoSuchUserException(username);
        }
        if(!users.contains(friendUsername)){
            throw new NoSuchUserException(friendUsername);
        }

        map.values().stream()
                .filter(r -> r.hasUser(friendUsername))
                .forEach(r -> r.addUser(username));
    }

}

class NoSuchRoomException extends Exception{
    public NoSuchRoomException(String message) {
        super(message);
    }
}

class NoSuchUserException extends Exception{
    public NoSuchUserException(String message) {
        super(message);
    }
}
public class ChatSystemTest {

    public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchRoomException {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if ( k == 0 ) {
            ChatRoom cr = new ChatRoom(jin.next());
            int n = jin.nextInt();
            for ( int i = 0 ; i < n ; ++i ) {
                k = jin.nextInt();
                if ( k == 0 ) cr.addUser(jin.next());
                if ( k == 1 ) cr.removeUser(jin.next());
                if ( k == 2 ) System.out.println(cr.hasUser(jin.next()));
            }
            System.out.println("");
            System.out.println(cr.toString());
            n = jin.nextInt();
            if ( n == 0 ) return;
            ChatRoom cr2 = new ChatRoom(jin.next());
            for ( int i = 0 ; i < n ; ++i ) {
                k = jin.nextInt();
                if ( k == 0 ) cr2.addUser(jin.next());
                if ( k == 1 ) cr2.removeUser(jin.next());
                if ( k == 2 ) cr2.hasUser(jin.next());
            }
            System.out.println(cr2.toString());
        }
        if ( k == 1 ) {
            ChatSystem cs = new ChatSystem();
            Method mts[] = cs.getClass().getMethods();
            while ( true ) {
                String cmd = jin.next();
                if ( cmd.equals("stop") ) break;
                if ( cmd.equals("print") ) {
                    System.out.println(cs.getRoom(jin.next())+"\n");continue;
                }
                for ( Method m : mts ) {
                    if ( m.getName().equals(cmd) ) {
                        String params[] = new String[m.getParameterTypes().length];
                        for ( int i = 0 ; i < params.length ; ++i ) params[i] = jin.next();
                        m.invoke(cs, (Object[]) params);
                    }
                }
            }
        }
    }

}
