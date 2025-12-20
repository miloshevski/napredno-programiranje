package lab5.anagrams;

import java.io.InputStream;
import java.util.*;

public class Anagrams {

    public static void main(String[] args) {
        findAll(System.in);
    }
    private static Map<String,TreeSet<String>> group = new TreeMap<>();

    private static String sortedLetters(String s){
        char [] chars = s.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }

    public static void findAll(InputStream inputStream) {
        Scanner sc = new Scanner(inputStream);
        while (sc.hasNext()){
            String s = sc.nextLine();
            String key = sortedLetters(s);
            group.computeIfAbsent(key, k->new TreeSet<>());
            group.get(key).add(s);
        }
        sc.close();
        group.values().stream()
                .filter(g -> g.size() >= 5)
                .sorted(Comparator.comparing(TreeSet::first))
                .forEach(set -> System.out.println(String.join(" ",set)));
    }
}

