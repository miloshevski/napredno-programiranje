package k1.textprocesor;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

class TextProcessor{
    private Set<String> corpus;
    private List<String> texts;
    public TextProcessor(){
        this.corpus = new TreeSet<>();
        this.texts = new ArrayList<>();
    }
    private List<String> vocabulary() {
        return new ArrayList<>(corpus); // веќе е сортиран (TreeSet), но го правиме List
    }
    private List<Integer> vectorFor(String text, List<String> vocab){
        Map<String,Integer> freq = new HashMap<>();
        for(String w : text.split("\\s+")){
            if(w.isEmpty()){
                continue;
            }
            freq.merge(w, 1,Integer::sum);
        }
        List<Integer>vec = new ArrayList<>(vocab.size());
        for(String w : vocab){
            vec.add(freq.getOrDefault(w,0));
        }
        return vec;
    }
    public void printTextvectors(OutputStream os){
        PrintWriter pw = new PrintWriter(os);
        List<String> vocab = vocabulary();
        for(String text : texts){
            List<Integer> vec = vectorFor(text, vocab);
            pw.println(vec.toString().replaceAll("\\s+"," "));
        }
        pw.flush();
    }
    public void printCorpus(OutputStream os, int n, boolean ascending){
        PrintWriter pw = new PrintWriter(os);

        // глобални фреквенции
        Map<String, Integer> global = new HashMap<>();
        for (String text : texts) {
            for (String w : text.split("\\s+")) {
                if (w.isEmpty()) continue;
                global.merge(w, 1, Integer::sum);
            }
        }

        Comparator<Map.Entry<String,Integer>> cmp =
                Comparator.comparing(Map.Entry<String,Integer>::getValue)
                        .thenComparing(Map.Entry::getKey); // стабилно по име

        if (!ascending) cmp = cmp.reversed();

        global.entrySet().stream()
                .sorted(cmp)
                .limit(n)
                .forEach(e -> pw.println(e.getKey() + " " + e.getValue()));

        pw.flush();
    }

    public void mostSimilarTexts(OutputStream os){
        PrintWriter pw = new PrintWriter(os);
        if (texts.size() < 2) {
            pw.println("Not enough texts");
            pw.flush();
            return;
        }

        List<String> vocab = vocabulary();
        List<List<Integer>> vectors = new ArrayList<>();
        for (String t : texts) vectors.add(vectorFor(t, vocab));

        double best = -1.0;
        int bi = -1, bj = -1;

        for (int i = 0; i < texts.size(); i++) {
            for (int j = i + 1; j < texts.size(); j++) {
                double sim = CosineSimilarityCalculator.cosineSimilarity(vectors.get(i), vectors.get(j));
                if (sim > best) { best = sim; bi = i; bj = j; }
            }
        }

        // Испечати ги двата текста (или индекси — зависи што ти бара тестот)
        pw.println(texts.get(bi));
        pw.println(texts.get(bj));
        // Ако сакаш, можеш и да го испечатиш и бројот (best)
        // pw.printf(Locale.US, "similarity=%.6f%n", best);

        pw.flush();
    }


    public void readText(InputStream inputStream){
        Scanner sc = new Scanner(inputStream);
        String line = sc.nextLine().toLowerCase()
                .replaceAll("[^a-zA-Z\\s]", "");

        texts.add(line);
        String [] l = line.split("\\s+");
        corpus.addAll(Arrays.asList(l));
    }

    public void printTextsVectors(OutputStream os){

    }
}

class CosineSimilarityCalculator {
    public static double cosineSimilarity (Collection<Integer> c1, Collection<Integer> c2) {
        int [] array1;
        int [] array2;
        array1 = c1.stream().mapToInt(i -> i).toArray();
        array2 = c2.stream().mapToInt(i -> i).toArray();
        double up = 0.0;
        double down1=0, down2=0;

        for (int i=0;i<c1.size();i++) {
            up+=(array1[i] * array2[i]);
        }

        for (int i=0;i<c1.size();i++) {
            down1+=(array1[i]*array1[i]);
        }

        for (int i=0;i<c1.size();i++) {
            down2+=(array2[i]*array2[i]);
        }

        return up/(Math.sqrt(down1)*Math.sqrt(down2));
    }
}

public class TextProcessorTest {

    public static void main(String[] args) {
        TextProcessor textProcessor = new TextProcessor();

        textProcessor.readText(System.in);

        System.out.println("===PRINT VECTORS===");
        textProcessor.printTextsVectors(System.out);

        System.out.println("PRINT FIRST 20 WORDS SORTED ASCENDING BY FREQUENCY ");
        textProcessor.printCorpus(System.out,  20, true);

        System.out.println("PRINT FIRST 20 WORDS SORTED DESCENDING BY FREQUENCY");
        textProcessor.printCorpus(System.out, 20, false);

        System.out.println("===MOST SIMILAR TEXTS===");
        textProcessor.mostSimilarTexts(System.out);
    }
}