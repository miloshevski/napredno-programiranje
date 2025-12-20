package k1.blokkontejner;

import java.util.*;
import java.util.stream.Collectors;

class BlockContainer<T extends Comparable<? super T>>{
    private List<List<T>> blocks;
    private int blockSize;

    public BlockContainer(int blockSize) {
        this.blockSize = blockSize;
        blocks = new ArrayList<>();
    }

    public void add(T a){
        if(blocks.isEmpty() || blocks.get(blocks.size() - 1).size() == blockSize){
            blocks.add(new ArrayList<>());
        }

        List<T>lastBlock = blocks.get(blocks.size() - 1);

        int idx = Collections.binarySearch(lastBlock,a);
        if(idx < 0){
            idx = -idx - 1;
        }
        lastBlock.add(idx,a);
    }

    public boolean remove(T a){
        if(blocks.isEmpty()){
            return false;
        }
        List<T> lastBlock = blocks.get(blocks.size() - 1);
        int idx = Collections.binarySearch(lastBlock, a);
        if(idx < 0){
            return false;
        }
        lastBlock.remove(idx);
        if(lastBlock.isEmpty()){
            blocks.remove(blocks.size() - 1);
        }
        return true;
    }

    public void sort(){
        List<T> all = new ArrayList<>();
        for(List<T> block : blocks){
            all.addAll(block);
        }
        Collections.sort(all);
        blocks.clear();
        int i = 0;
        while (i<all.size()){
            List<T> block = new ArrayList<>();
            for(int cnt = 0;cnt<blockSize && i<all.size();cnt++,i++){
                block.add(all.get(i));
            }
            blocks.add(block);
        }
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < blocks.size(); i++) {
            List<T> block = blocks.get(i);
            if (i > 0) sb.append(",");  // запирка меѓу блокови

            sb.append("[");
            for (int j = 0; j < block.size(); j++) {
                if (j > 0) sb.append(", ");
                sb.append(block.get(j));
            }
            sb.append("]");
        }

        return sb.toString();
    }
}

public class BlockContainerTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int size = scanner.nextInt();
        BlockContainer<Integer> integerBC = new BlockContainer<Integer>(size);
        scanner.nextLine();
        Integer lastInteger = null;
        for(int i = 0; i < n; ++i) {
            int element = scanner.nextInt();
            lastInteger = element;
            integerBC.add(element);
        }
        System.out.println("+++++ Integer Block Container +++++");
        System.out.println(integerBC);
        System.out.println("+++++ Removing element +++++");
        integerBC.remove(lastInteger);
        System.out.println("+++++ Sorting container +++++");
        integerBC.sort();
        System.out.println(integerBC);
        BlockContainer<String> stringBC = new BlockContainer<String>(size);
        String lastString = null;
        for(int i = 0; i < n; ++i) {
            String element = scanner.next();
            lastString = element;
            stringBC.add(element);
        }
        System.out.println("+++++ String Block Container +++++");
        System.out.println(stringBC);
        System.out.println("+++++ Removing element +++++");
        stringBC.remove(lastString);
        System.out.println("+++++ Sorting container +++++");
        stringBC.sort();
        System.out.println(stringBC);
    }
}

// Вашиот код овде



