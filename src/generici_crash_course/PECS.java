package generici_crash_course;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PECS {

    // PRODUCER: ? extends Number (само читаме, не додаваме)
    public static double sum(List<? extends Number> list) {
        double s = 0;
        for (Number n : list) {
            s += n.doubleValue();
        }
        return s;
    }

    // CONSUMER: ? super Integer (додаваме Integer вредности)
    public static void fillWithIntegers(List<? super Integer> list) {
        list.add(10);
        list.add(20);
        list.add(30);
    }

    // Комбинација како Collections.copy(dest, src): dest = ? super T, src = ? extends T
    public static <T> void copy(List<? super T> dest, List<? extends T> src) {
        for (int i = 0; i < src.size(); i++) {
            dest.set(i, src.get(i));
        }
    }

    public static void main(String[] args) {
        // ====== пример за PRODUCER (? extends Number) ======
        List<Integer> intList = Arrays.asList(1, 2, 3, 4);
        List<Double> doubleList = Arrays.asList(1.5, 2.5, 3.5);

        System.out.println("sum(intList)    = " + sum(intList));     // List<Integer>
        System.out.println("sum(doubleList) = " + sum(doubleList));  // List<Double>

        // ====== пример за CONSUMER (? super Integer) ======
        List<Integer> listInt = new ArrayList<>();
        List<Number> listNum = new ArrayList<>();
        List<Object> listObj = new ArrayList<>();

        fillWithIntegers(listInt);  // List<Integer> ќе консумира Integer
        fillWithIntegers(listNum);  // List<Number> ќе консумира Integer
        fillWithIntegers(listObj);  // List<Object> ќе консумира Integer

        System.out.println("listInt = " + listInt);
        System.out.println("listNum = " + listNum);
        System.out.println("listObj = " + listObj);

        // ====== пример за copy(dest, src) со PECS ======
        List<Integer> src = Arrays.asList(100, 200, 300);
        List<Number> dest = new ArrayList<>(Arrays.asList(0, 0, 0));

        System.out.println("dest (before) = " + dest);
        copy(dest, src); // dest: ? super Integer, src: ? extends Integer
        System.out.println("dest (after)  = " + dest);

        // ====== пример: што НЕ СМЕЕ со ? extends ======
        List<? extends Number> nums = intList;
        Number n0 = nums.get(0);   // ОК - може да читаме
        System.out.println("first from nums = " + n0);

        // nums.add(5);          // КОМПИЛАЦИСКА ГРЕШКА - не смееш да додаваш
        // nums.add(5.5);        // КОМПИЛАЦИСКА ГРЕШКА
        // nums.add(new Object());// КОМПИЛАЦИСКА ГРЕШКА

        // ====== пример: што смееш со ? super ======
        List<? super Integer> superList = new ArrayList<Number>();
        superList.add(123); // ОК - можеш да додадеш Integer
        Object o = superList.get(0); // Читањето е само како Object
        System.out.println("first from superList = " + o);
    }
}
