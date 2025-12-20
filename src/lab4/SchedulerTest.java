package lab4;

import java.util.Date;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.NavigableMap;

 class Scheduler<T> {

    // Чуваме парови (Date -> T) во сортирана мапа
    private TreeMap<Date, T> map;

    // Sheduler() - креира нов празен распоредувач
    public Scheduler() {
        map = new TreeMap<>();
    }

    // add(Date d, T t) - додава нов објект во распоредувачот
    public void add(Date d, T t) {
        // Според условот, нема два објекти со исто време,
        // така што само put е доволно.
        map.put(d, t);
    }

    // remove(Date d):boolean - го брише соодветниот елемент доколку постои
    public boolean remove(Date d) {
        return map.remove(d) != null;
    }

    // next():T - објект со дата НАЈБЛИСКА до тековната, што Е >= тековна (уште не е помината)
    public T next() {
        Date now = new Date();
        Date key = map.ceilingKey(now); // прв клуч >= now
        if (key == null) return null;
        return map.get(key);
    }

    // last():T - објект со дата НАЈБЛИСКА до тековната, што Е <= тековна (веќе е помината)
    public T last() {
        Date now = new Date();
        Date key = map.floorKey(now); // последен клуч <= now
        if (key == null) return null;
        return map.get(key);
    }

    // getAll(Date begin, Date end):ArrayList<T> - сите објекти со дата меѓу begin и end (инклузивно)
    public ArrayList<T> getAll(Date begin, Date end) {
        ArrayList<T> result = new ArrayList<>();
        if (begin.after(end)) {
            return result; // празна листа ако интервалот е наопаку
        }

        // subMap(begin, true, end, true) да бидат и begin и end вклучени
        NavigableMap<Date, T> sub = map.subMap(begin, true, end, true);
        result.addAll(sub.values());
        return result;
    }

    // getFirst():T - објект со НАЈМАЛА дата (севкупно)
    public T getFirst() {
        if (map.isEmpty()) return null;
        Date key = map.firstKey();
        return map.get(key);
    }

    // getLast():T - објект со НАЈГОЛЕМА дата (севкупно)
    public T getLast() {
        if (map.isEmpty()) return null;
        Date key = map.lastKey();
        return map.get(key);
    }
}
