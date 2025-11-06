package lab2.generics;

import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
import java.util.LinkedList;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;

class ArrayTransformer{
    public static <T,R>ResizableArray<R> map(ResizableArray<T> source, Function<? super T, ? extends R> mapper){
        ResizableArray<R> result = new ResizableArray<>();
        for(int i=0;i<source.count();i++){
            R mapped = mapper.apply(source.elementAt(i));
            result.addElement(mapped);
        }
        return result;
    }
    public static <T>ResizableArray<T>filter(ResizableArray<T> source, Predicate<? super T> predicate){
        ResizableArray<T> result = new ResizableArray<>();
        for(int i=0;i<source.count();i++){
            T elem = source.elementAt(i);
            if(predicate.test(elem)){
                result.addElement(elem);
            }
        }
        return result;
    }
}

class ResizableArray<T>{
    private T[] array;
    private int size;
    @SuppressWarnings("unchecked")
    public ResizableArray(){
        this.array = (T[]) new Object[1];
        this.size = 0;
    }

    public static <T,R> ResizableArray<R> copyIf(ResizableArray<? extends T> source, Function<? super T,? extends R> mapper){
        ResizableArray<R> result = new ResizableArray<>();
        for(int i=0;i<source.count();i++){
            T elem = source.elementAt(i);
            R mapped = mapper.apply(elem);
            result.addElement(mapped);
        }
        return result;
    }


    public void addElement(T element){
        if(size >= array.length){
            array = Arrays.copyOf(array,array.length*2);
        }
        array[size++] = element;
    }

    public boolean removeElement(T element) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(array[i], element)) {
                System.arraycopy(array, i + 1, array, i, size - i - 1);
                array[--size] = null;
                if (size < array.length / 2) {
                    int newCap = Math.max(1, array.length / 2);
                    array = Arrays.copyOf(array, newCap);
                }
                return true;
            }
        }
        return false;
    }

    public boolean contains(T element) {
        return Arrays.stream(array)
                .limit(size)
                .anyMatch(x -> Objects.equals(x, element));
    }

    public Object[] toArray() {
        return Arrays.copyOf(array, size, Object[].class);
    }

    public boolean isEmpty(){
        return size == 0;
    }
    public int count(){
        return size;
    }

    public T elementAt(int idx) {
        if(idx >= size || idx < 0){
            throw new ArrayIndexOutOfBoundsException();
        }
        return array[idx];
    }

    public static <T> void copyAll(ResizableArray<? super T> dest,
                                   ResizableArray<? extends T> src) {
        int n = src.count();                // snapshot size ONCE
        for (int i = 0; i < n; i++) {
            dest.addElement(src.elementAt(i));
        }
    }


}

class IntegerArray extends ResizableArray<Integer>{
    public IntegerArray() {
        super();
    }

    private IntStream getStream(){
        return Arrays.stream(toArray()).limit(count()).mapToInt(Integer.class::cast);
    }

    public double sum() throws ArrayIndexOutOfBoundsException {
        return getStream().sum();
    }

    public double mean() throws ArrayIndexOutOfBoundsException {
        return sum() / count();
    }
    public int countNonZero(){
        return (int)getStream().filter(i -> i != 0).count();
    }
    public IntegerArray distinct(){
        IntegerArray ia = new IntegerArray();

        getStream().distinct().forEach(ia::addElement);

        return ia;
    }
    public IntegerArray increment(int o){
        IntegerArray ia = new IntegerArray();

        getStream().map(i -> i + o).forEach(ia::addElement);
        return ia;
    }

}

public class ResizableArrayTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int test = jin.nextInt();
        if ( test == 0 ) { //test ResizableArray on ints
            ResizableArray<Integer> a = new ResizableArray<Integer>();
            System.out.println(a.count());
            int first = jin.nextInt();
            a.addElement(first);
            System.out.println(a.count());
            int last = first;
            while ( jin.hasNextInt() ) {
                last = jin.nextInt();
                a.addElement(last);
            }
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(a.removeElement(first));
            System.out.println(a.contains(first));
            System.out.println(a.count());
        }
        if ( test == 1 ) { //test ResizableArray on strings
            ResizableArray<String> a = new ResizableArray<String>();
            System.out.println(a.count());
            String first = jin.next();
            a.addElement(first);
            System.out.println(a.count());
            String last = first;
            for ( int i = 0 ; i < 4 ; ++i ) {
                last = jin.next();
                a.addElement(last);
            }
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(a.removeElement(first));
            System.out.println(a.contains(first));
            System.out.println(a.count());
            ResizableArray<String> b = new ResizableArray<String>();
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
            System.out.println(b.removeElement(first));
            System.out.println(b.contains(first));
            System.out.println(b.removeElement(first));
            System.out.println(b.contains(first));

            System.out.println(a.removeElement(first));
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
        }
        if ( test == 2 ) { //test IntegerArray
            IntegerArray a = new IntegerArray();
            System.out.println(a.isEmpty());
            while ( jin.hasNextInt() ) {
                a.addElement(jin.nextInt());
            }
            jin.next();
            System.out.println(a.sum());
            System.out.println(a.mean());
            System.out.println(a.countNonZero());
            System.out.println(a.count());
            IntegerArray b = a.distinct();
            System.out.println(b.sum());
            IntegerArray c = a.increment(5);
            System.out.println(c.sum());
            if ( a.sum() > 100 )
                ResizableArray.copyAll(a, a);
            else
                ResizableArray.copyAll(a, b);
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.contains(jin.nextInt()));
            System.out.println(a.contains(jin.nextInt()));
        }
        if ( test == 3 ) { //test insanely large arrays
            LinkedList<ResizableArray<Integer>> resizable_arrays = new LinkedList<ResizableArray<Integer>>();
            for ( int w = 0 ; w < 500 ; ++w ) {
                ResizableArray<Integer> a = new ResizableArray<Integer>();
                int k =  2000;
                int t =  1000;
                for ( int i = 0 ; i < k ; ++i ) {
                    a.addElement(i);
                }

                a.removeElement(0);
                for ( int i = 0 ; i < t ; ++i ) {
                    a.removeElement(k-i-1);
                }
                resizable_arrays.add(a);
            }
            System.out.println("You implementation finished in less then 3 seconds, well done!");
        }
    }

}
