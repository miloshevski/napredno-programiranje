package docviewer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

interface IDocument{
    List<String> getLines();
    default String render(){
        return String.join("\n",getLines());
    }
}

class DocumentComponent implements IDocument{

    private final List<String> lines;

    DocumentComponent(List<String> lines) {
        this.lines = lines;
    }


    @Override
    public List<String> getLines() {
        return new ArrayList<>(lines);
    }
}
abstract class BaseDecorator implements IDocument{
    protected final IDocument inner;

    BaseDecorator(IDocument inner) {
        this.inner = inner;
    }

    @Override
    public List<String> getLines() {
        return inner.getLines();
    }
}

class LineDecorator extends BaseDecorator{

    LineDecorator(IDocument inner) {
        super(inner);
    }

    @Override
    public List<String> getLines() {
        List<String> lines = inner.getLines();
        List<String> decorated = new ArrayList<>();
        int i = 1;

        for(String l : lines){
            decorated.add(String.format("%d. %s",i++,l));
        }

        return decorated;
    }
}

class WordCountDecorator extends BaseDecorator{

    WordCountDecorator(IDocument inner) {
        super(inner);
    }

    private int countWords(List<String> list){
        int count = 0;
        for(String line : list){
            String t = line.trim();
            if(t.isEmpty()) continue;
            count += t.split("\\s+").length;
        }
        return count;
    }

    @Override
    public List<String> getLines() {
        List<String> lines = inner.getLines();
        List<String> out = new ArrayList<>(lines);
        out.add("Words: " + countWords(lines));
        return out;
    }
}

class RedactionDecorator extends BaseDecorator{
    private final List<String> forbidden;

    RedactionDecorator(IDocument inner, List<String> forbidden) {
        super(inner);

        this.forbidden = forbidden;
    }


}