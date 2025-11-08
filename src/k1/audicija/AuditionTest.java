package k1.audicija;

import java.util.*;


class Participant implements Comparable<Participant> {
    private final String city;
    private final String code;
    private final String name;
    private final int age;

    public Participant(String city, String code, String name, int age) {
        this.city = city;
        this.code = code;
        this.name = name;
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participant that = (Participant) o;
        return city.equals(that.city) && code.equals(that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, code);
    }

    @Override
    public int compareTo(Participant o) {
        int compare = name.compareTo(o.name);

        return compare == 0 ? Integer.compare(age, o.age) : compare;
    }

    @Override
    public String toString() {
        return String.format("%s %s %d", code, name, age);
    }

    public String getCity() {
        return city;
    }
}
class Audition {
    private final Set<Participant> set;

    public Audition() {
        this.set = new HashSet<>();
    }

    public void addParticpant(String city, String code, String name, int age) {
        set.add(new Participant(city, code, name, age));
    }

    public void listByCity(String city) {
        set.stream().filter(i->i.getCity().equals(city)).sorted().forEach(System.out::println);
    }
}

public class AuditionTest {
    public static void main(String[] args) {
        Audition audition = new Audition();
        List<String> cities = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            if (parts.length > 1) {
                audition.addParticpant(parts[0], parts[1], parts[2],
                        Integer.parseInt(parts[3]));
            } else {
                cities.add(line);
            }
        }
        for (String city : cities) {
            System.out.printf("+++++ %s +++++\n", city);
            audition.listByCity(city);
        }
        scanner.close();
    }
}