package documentviewr;


import java.io.*;
import java.util.*;
import java.util.regex.*;

interface DocumentComponent {
    List<String> getLines();

    default String render() {
        return String.join("\n", getLines());
    }
}

// Concrete component
class BasicDocument implements DocumentComponent {
    private final List<String> lines;

    public BasicDocument(List<String> lines) {
        this.lines = new ArrayList<>(lines);
    }

    @Override
    public List<String> getLines() {
        return new ArrayList<>(lines);
    }
}

// Base decorator
abstract class DocumentDecorator implements DocumentComponent {
    protected final DocumentComponent inner;

    protected DocumentDecorator(DocumentComponent inner) {
        this.inner = inner;
    }

    @Override
    public List<String> getLines() {
        return inner.getLines();
    }
}

// Decorator: line numbers with "i: "
class LineNumbersDecorator extends DocumentDecorator {
    public LineNumbersDecorator(DocumentComponent inner) {
        super(inner);
    }

    @Override
    public List<String> getLines() {
        List<String> lines = inner.getLines();
        List<String> out = new ArrayList<>(lines.size());
        for (int i = 0; i < lines.size(); i++) {
            out.add((i + 1) + ": " + lines.get(i));
        }
        return out;
    }
}

// Decorator: add last line "Words: W"
class WordCountDecorator extends DocumentDecorator {
    public WordCountDecorator(DocumentComponent inner) {
        super(inner);
    }

    private int countWords(List<String> lines) {
        int count = 0;
        for (String line : lines) {
            String t = line.trim();
            if (t.isEmpty()) continue;
            count += t.split("\\s+").length;
        }
        return count;
    }

    @Override
    public List<String> getLines() {
        List<String> lines = inner.getLines();
        int w = countWords(lines);
        List<String> out = new ArrayList<>(lines);
        out.add("Words: " + w);
        return out;
    }
}

// Decorator: redact forbidden words (whole-word) -> ***** same length
class RedactionDecorator extends DocumentDecorator {
    private final List<String> forbidden;

    public RedactionDecorator(DocumentComponent inner, List<String> forbiddenWords) {
        super(inner);
        this.forbidden = new ArrayList<>(forbiddenWords);
    }

    private String redactLine(String line) {
        String result = line;
        for (String word : forbidden) {
            if (word == null || word.isEmpty()) continue;

            Pattern p = Pattern.compile(
                    "\\b" + Pattern.quote(word) + "\\b",
                    Pattern.CASE_INSENSITIVE
            );
            String stars = "*";
            result = p.matcher(result).replaceAll(stars);
        }
        return result;
    }

    @Override
    public List<String> getLines() {
        List<String> lines = inner.getLines();
        List<String> out = new ArrayList<>(lines.size());
        for (String line : lines) {
            out.add(redactLine(line));
        }
        return out;
    }
}

class DocumentViewer {
    private final Map<String, DocumentComponent> docs = new HashMap<>();

    public DocumentViewer() {}

    public void addDocument(String id, String text) {
        // text is already built from lines; keep as lines
        List<String> lines = Arrays.asList(text.split("\n", -1));
        docs.put(id, new BasicDocument(lines));
    }

    public void enableLineNumbers(String id) {
        docs.put(id, new LineNumbersDecorator(get(id)));
    }

    public void enableWordCount(String id) {
        docs.put(id, new WordCountDecorator(get(id)));
    }

    public void enableRedaction(String id, List<String> forbiddenWords) {
        docs.put(id, new RedactionDecorator(get(id), forbiddenWords));
    }

    public void display(String id) {
        DocumentComponent doc = get(id);
        System.out.println("=== Document " + id + " ===");
        System.out.println(doc.render());
    }

    private DocumentComponent get(String id) {
        DocumentComponent d = docs.get(id);
        if (d == null) throw new IllegalArgumentException("No document with id=" + id);
        return d;
    }
}

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        DocumentViewer viewer = new DocumentViewer();

        int n = Integer.parseInt(br.readLine().trim()); // number of documents

        for (int i = 0; i < n; i++) {
            String id = br.readLine().trim();            // document id (e.g., 5, 7)
            int linesCount = Integer.parseInt(br.readLine().trim());

            List<String> lines = new ArrayList<>();
            for (int j = 0; j < linesCount; j++) {
                lines.add(br.readLine());
            }

            String text = String.join("\n", lines);
            viewer.addDocument(id, text);
        }

        // commands until exit
        while (true) {
            String line = br.readLine();
            if (line == null) break;
            line = line.trim();
            if (line.equalsIgnoreCase("exit")) break;
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\s+");
            String cmd = parts[0];

            switch (cmd) {
                case "enableLineNumbers": {
                    String id = parts[1];
                    viewer.enableLineNumbers(id);
                    break;
                }
                case "enableWordCount": {
                    String id = parts[1];
                    viewer.enableWordCount(id);
                    break;
                }
                case "enableRedaction": {
                    String id = parts[1];
                    List<String> forbidden = new ArrayList<>();

                    // сите аргументи после id се забранети зборови
                    for (int i = 2; i < parts.length; i++) {
                        forbidden.add(parts[i]);
                    }

                    viewer.enableRedaction(id, forbidden);
                    break;
                }

                case "display": {
                    String id = parts[1];
                    viewer.display(id);
                    break;
                }
                default:
                    // ignore unknown commands or throw if your judge expects strictness
                    break;
            }
        }
    }
}
