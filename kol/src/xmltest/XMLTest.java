package xmltest;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


interface XMLComponent{
    void addAttribute(String key,String value);
    void addComponent(XMLComponent component);
    String toXML(int indent);
}

class XMLLeaf implements XMLComponent{

    private final String tag;
    private final String value;
    private final Map<String, String > attributes;

    XMLLeaf(String tag, String value) {
        this.tag = tag;
        this.value = value;
        attributes = new LinkedHashMap<>();
    }


    @Override
    public void addAttribute(String key, String value) {
        attributes.put(key,value);
    }

    @Override
    public void addComponent(XMLComponent component) {
        throw new UnsupportedOperationException("XMLLeaf cannot have child components");
    }

    @Override
    public String toXML(int indent) {
        String indentStr = "    ".repeat(Math.max(0, indent));
        StringBuilder sb = new StringBuilder();

        sb.append(indentStr).append("<").append(tag);

        for(Map.Entry<String,String> e : attributes.entrySet()){
            sb.append(" ").append(e.getKey()).append("=\"").append(e.getValue()).append("\"");
        }

        sb.append(">").append(value).append("</").append(tag).append(">");
        return sb.toString();
    }

    @Override
    public String toString() {
        return toXML(0);
    }
}

class XMLComposite implements XMLComponent{
    private final String tag;
    private final Map<String,String> attributes;
    private final List<XMLComponent> children;

    public XMLComposite(String tag) {
        attributes = new LinkedHashMap<>();
        children = new ArrayList<>();
        this.tag = tag;
    }

    @Override
    public void addAttribute(String key, String value) {
        attributes.put(key,value);
    }

    @Override
    public void addComponent(XMLComponent component) {
        children.add(component);
    }

    @Override
    public String toXML(int indent) {
        String indentStr = "    ".repeat(Math.max(0,indent));
        String childIndentStr = "    ".repeat(Math.max(0,indent+1));
        StringBuilder sb = new StringBuilder();

        sb.append(indentStr).append("<").append(tag);
        for(Map.Entry<String ,String > e : attributes.entrySet()){
            sb.append(" ").append(e.getKey()).append("=\"").append(e.getValue()).append("\"");

        }
        sb.append(">");

        if(!children.isEmpty()){
            sb.append("\n");
            for(int i=0;i<children.size();i++){
                sb.append(children.get(i).toXML(indent+1));
                if(i < children.size()-1) sb.append("\n");
            }
            sb.append("\n").append(indentStr);
        }
        // Closing tag
        sb.append("</").append(tag).append(">");
        return sb.toString();
    }

    @Override
    public String toString() {
        return toXML(0);
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
