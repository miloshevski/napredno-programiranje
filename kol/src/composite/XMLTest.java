package composite;

import java.util.*;
import java.util.stream.Collectors;

interface XMLComponent{
    void addComponent(XMLComponent component);
    String print(int indent);
    void addAttribute(String key,String value);
    String getAttributes();
}

class XMLLeaf implements XMLComponent{
    private String tag;
    private String value;
    private Map<String,String> attributes = new LinkedHashMap<>();

    public XMLLeaf(String tag,String value) {
        this.tag = tag;
        this.value = value;
    }


    @Override
    public void addComponent(XMLComponent component) {
        return;
    }

    private String format(String key){
        return String.format("%s=\"%s\"",key,attributes.get(key));
    }

    @Override
    public String getAttributes(){
        return attributes.keySet().stream().map(this::format).collect(Collectors.joining(" "));
    }

    @Override
    public String print(int indent) {
        String ind = " ".repeat(indent);
        return String.format("%s<%s%s%s>%s</%s>",ind,tag.strip(),getAttributes().trim().isEmpty() ? "" : " ",getAttributes().trim(),value,tag);
    }

    @Override
    public void addAttribute(String key, String value) {
        attributes.put(key,value);
    }

    @Override
    public String toString() {
        return print(0);
    }
}

class XMLComposite implements XMLComponent{
    private Map<String,String> attributes;
    private String tag;
    private List<XMLComponent> children;

    public XMLComposite(String tag) {
        this.tag = tag;
        attributes = new LinkedHashMap<>();
        children = new ArrayList<>();
    }

    @Override
    public void addComponent(XMLComponent component) {
        children.add(component);
    }

    @Override
    public String print(int indent) {
        String ind = " ".repeat(indent);

        String attrs = getAttributes().trim();
        String openTag = attrs.isEmpty()
                ? String.format("%s<%s>%n", ind, tag)
                : String.format("%s<%s %s>%n", ind, tag, attrs);

        StringBuilder sb = new StringBuilder();
        sb.append(openTag);

        for (XMLComponent c : children) {
            sb.append(c.print(indent + 4)).append("\n"); // +2 или +4, ама конзистентно
        }

        sb.append(String.format("%s</%s>", ind, tag));
        return sb.toString();
    }


    @Override
    public String toString() {
        return print(0);
    }

    @Override
    public void addAttribute(String key, String value) {
        attributes.put(key,value);
    }

    private String format(String key){
        return String.format("%s=\"%s\"",key,attributes.get(key));
    }

    @Override
    public String getAttributes() {
        return attributes.keySet().stream().map(this::format).collect(Collectors.joining(" "));
    }
}

public class XMLTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int testCase = sc.nextInt();
        XMLComponent component = new XMLLeaf("student", "Trajce Trajkovski");
        component.addAttribute("type", "redoven");
        component.addAttribute("program", "KNI");

        XMLComposite composite = new XMLComposite("name");
        composite.addComponent(new XMLLeaf("first-name", "trajce"));
        composite.addComponent(new XMLLeaf("last-name", "trajkovski"));
        composite.addAttribute("type", "redoven");
        component.addAttribute("program", "KNI");

        if (testCase==1) {
            System.out.println(component);
        } else if(testCase==2) {
            System.out.println(composite);
        } else if (testCase==3) {
            XMLComposite main = new XMLComposite("level1");
            main.addAttribute("level","1");
            XMLComposite lvl2 = new XMLComposite("level2");
            lvl2.addAttribute("level","2");
            XMLComposite lvl3 = new XMLComposite("level3");
            lvl3.addAttribute("level","3");
            lvl3.addComponent(component);
            lvl2.addComponent(lvl3);
            lvl2.addComponent(composite);
            lvl2.addComponent(new XMLLeaf("something", "blabla"));
            main.addComponent(lvl2);
            main.addComponent(new XMLLeaf("course", "napredno programiranje"));

            System.out.println(main);
        }
    }
}
