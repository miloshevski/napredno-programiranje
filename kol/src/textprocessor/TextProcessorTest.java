package textprocessor;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

class Text{
    public String text;

    private String process(String s){
        return s.replaceAll("[.,?!]","");
    }

    public Text(String s){
        text = process(s);
    }

    public String getText() {
        return text;
    }
    public List<Integer> vector(){
        Map<String,Integer> mapa = TextProcessor.map;
        for(String s : text.split("\\s+")){
            mapa.merge(s,1,Integer::sum);
        }
        return new ArrayList<>(mapa.values());
    }

    @Override
    public String toString() {
        return vector().stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
    }

}

class TextProcessor{
    private List<Integer> vector;
    public static Map<String ,Integer> map;
    private List<Text> texts;

    public TextProcessor(){
        vector = new ArrayList<>();
        texts = new ArrayList<>();
        map = new TreeMap<>(Comparator.comparing(String::valueOf));
    }

    public void readText(InputStream is) {
        Scanner sc = new Scanner(is);


        while (sc.hasNextLine()) {
            String line = sc.nextLine();

            line = line.toLowerCase().replaceAll("[^a-z\\s]+", " ").trim();
            line = line.replaceAll("\\s+", " ");

            Text t = new Text(line);
            texts.add(t);

            if (line.isEmpty()) continue;

            for (String word : line.split("\\s+")) {
                if (word.isEmpty()) continue;
                map.put(word,0);
            }
        }
    }

    public void printTextsVectors(OutputStream os){
        for(Text t : texts){
            System.out.printf("[%s]%n",t);
        }
    }

    public void printCorpus(OutputStream os, int n, boolean ascending){
        PrintWriter pw = new PrintWriter(os);

        Comparator<Map.Entry<String,Integer>> comparator =
                Map.Entry.comparingByValue();

        if(!ascending){
            comparator = comparator.reversed();
        }

        map.entrySet().stream()
                .sorted(comparator)
                .limit(n)
                .forEach(e -> pw.println(e.getKey() + " : " + e.getValue()));
        pw.flush();
    }
    public void mostSimilarTexts(OutputStream os) {
        PrintWriter pw = new PrintWriter(os);

        if (texts.size() < 2) {
            pw.println("Not enough texts");
            pw.flush();
            return;
        }

        // Vocabulary in lexicographic order (TreeMap -> already sorted)
        List<String> vocab = new ArrayList<>(map.keySet());

        // Precompute vectors for each text
        List<List<Integer>> vectors = texts.stream()
                .map(t -> buildVectorForText(t, vocab))
                .collect(Collectors.toList());

        double bestSim = -1.0;
        int bestI = 0, bestJ = 1;

        for (int i = 0; i < vectors.size(); i++) {
            for (int j = i + 1; j < vectors.size(); j++) {
                double sim = CosineSimilarityCalculator.cosineSimilarity(vectors.get(i), vectors.get(j));
                if (sim > bestSim) {
                    bestSim = sim;
                    bestI = i;
                    bestJ = j;
                }
            }
        }

        // Print the two most similar texts (as strings)
        pw.println(texts.get(bestI).getText());
        pw.println(texts.get(bestJ).getText());
        pw.flush();
    }

    private List<Integer> buildVectorForText(Text text, List<String> vocab) {
        // Count words in this text
        Map<String, Integer> local = new HashMap<>();

        String s = text.getText().toLowerCase().trim();
        if (!s.isEmpty()) {
            for (String w : s.split("\\s+")) {
                if (!w.isEmpty()) {
                    local.merge(w, 1, Integer::sum);
                }
            }
        }

        // Build vector aligned with global vocab order
        List<Integer> v = new ArrayList<>(vocab.size());
        for (String w : vocab) {
            v.add(local.getOrDefault(w, 0));
        }
        return v;
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