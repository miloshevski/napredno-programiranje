package docviewer;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

interface XMLComponent{
    void addAttribute(String key,String value);
    String toXML(int indent);
}

abstract class AbstractXMLComponent implements XMLComponent{

    protected String tag;
    protected Map<String,String> attributes;

    public AbstractXMLComponent(String tag){
        this.tag = tag;
        this.attributes = new LinkedHashMap<>();
    }

    @Override
    public void addAttribute(String key, String value) {
        attributes.put(key,value);
    }

    protected String makeIndent(int indent){
        return " ".repeat(indent);
    }

    protected String attributesToString(){
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String,String> e : attributes.entrySet()){
            sb.append(" ").append(e.getKey()).append("=\"").append(e.getValue()).append("\"");
        }
        return sb.toString();
    }
}

class XMLLeaf extends AbstractXMLComponent{
    private String value;

    public XMLLeaf(String tag, String value){
        super(tag);
        this.value = value;
    }

    @Override
    public String toXML(int indent) {
        String ind = makeIndent(indent);
        return ind + "<" + tag + attributesToString() + ">" + value + "</" + tag + ">";
    }

    @Override
    public String toString() {
        return toXML(0);
    }
}

class XMLComposite extends AbstractXMLComponent{
    private List<XMLComponent> children;

    public XMLComposite(String tag){
        super(tag);
        this.children = new ArrayList<>();
    }

    public void addComponent(XMLComponent component){
        children.add(component);
    }

    @Override
    public String toXML(int indent) {
        String ind = makeIndent(indent);

        StringBuilder sb = new StringBuilder();
        sb.append(ind).append("<").append(tag).append(attributesToString()).append(">").append("\n");

        for(int i = 0; i < children.size(); i++){
            sb.append(children.get(i).toXML(indent + 4));
            if(i < children.size() - 1) sb.append("\n");
        }

        sb.append("\n").append(ind).append("</").append(tag).append(">");
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
