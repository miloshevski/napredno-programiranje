package k1.iminja;
import java.util.*;
import java.util.stream.Collectors;

class Names{
    private Map<String, Integer> map;
    private List<String> list;
    public Names(){
        map = new HashMap<>();
        list = new ArrayList<>();
    }
    public int unique(String s){
        s = s.toLowerCase();
        Set<Character> set = new HashSet<>();
        for(int i=0;i<s.length();i++){
            set.add(s.charAt(i));
        }
        return set.size();
    }
    public void addName(String name){
        map.put(name,map.getOrDefault(name,0) + 1);
        list.add(name);
    }
    public void printN(int n){
        list.stream().filter(w -> map.get(w) >= n).
                sorted().
                collect(Collectors.toList()).stream().distinct().forEach(z -> {
                    System.out.println(String.format("%s (%d) %d",z,map.get(z),unique(z)));
                });

    }
    public String findName(int len, int  x){
        List<String>list1 = list.stream().distinct().filter(i -> i.length() < len).sorted().collect(Collectors.toList());
        if(x < list1.size()){
            return list1.get(x);
        }
        int idx = x % list1.size();

        return list1.get(idx);
    }
}

public class NamesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        Names names = new Names();
        for (int i = 0; i < n; ++i) {
            String name = scanner.nextLine();
            names.addName(name);
        }
        n = scanner.nextInt();
        System.out.printf("===== PRINT NAMES APPEARING AT LEAST %d TIMES =====\n", n);
        names.printN(n);
        System.out.println("===== FIND NAME =====");
        int len = scanner.nextInt();
        int index = scanner.nextInt();
        System.out.println(names.findName(len, index));
        scanner.close();

    }
}

// vashiot kod ovde