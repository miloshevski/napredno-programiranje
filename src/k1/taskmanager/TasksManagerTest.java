package k1.taskmanager;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

class DeadlineNotValidException extends Exception {
    public DeadlineNotValidException(String msg) { super(msg); }
}

class Task{
    private String category;
    private String name;
    private String description;
    private LocalDateTime deadline;
    private Integer priority;


    private static final LocalDateTime CUTOFF = LocalDateTime.of(2020, 6, 2, 0, 0, 0);

    // keep the constructor but ensure the comparison uses LocalDateTime:
    public Task(String category, String name, String description,
                LocalDateTime deadline, Integer priority) throws DeadlineNotValidException {
        if (deadline != null && deadline.isAfter(CUTOFF)) {
            throw new DeadlineNotValidException(
                    String.format("Deadline %s is after 02.06.2020", deadline));
        }
        this.category = category;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.priority = priority;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public Integer getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        // „default style“ со имиња на полињата
        return "Task{" +
                "category='" + category + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", deadline=" + deadline +
                ", priority=" + priority +
                '}';
    }
}

class TaskManager{
    private final List<Task> tasks = new ArrayList<>();

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public void readTasks(InputStream inputStream) {
        Scanner sc = new Scanner(inputStream);

        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;

            // keep empty fields if any
            String[] parts = line.split(",", -1);
            if (parts.length < 3) continue; // need at least category,name,description

            String category = parts[0];
            String name = parts[1];
            String description = parts[2];

            LocalDateTime deadline = null;
            Integer priority = null;

            if (parts.length >= 4 && !parts[3].isBlank()) {
                // supports both 2020-06-28T16:00:00 and 2020-06-28T16:00:00.000
                deadline = LocalDateTime.parse(parts[3], DTF);
            }
            if (parts.length >= 5 && !parts[4].isBlank()) {
                priority = Integer.parseInt(parts[4]);
            }

            try {
                tasks.add(new Task(category, name, description, deadline, priority));
            } catch (DeadlineNotValidException e) {
                // skip invalid-deadline task; DO NOT stop reading
                // (spec requires to continue reading the rest)
            }
        }
    }


    public void printTasks(OutputStream os, boolean includePriority, boolean includeCategory) {
        PrintWriter pw = new PrintWriter(os);

        Comparator<Task> byDeadlineDistance = Comparator.comparingLong(t -> {
            if (t.getDeadline() == null) return Long.MAX_VALUE; // tasks without deadline go last
            return Math.abs(java.time.Duration.between(LocalDateTime.now(), t.getDeadline()).toSeconds());
        });

        Comparator<Task> byPriorityThenDeadline = Comparator
                // null priority -> goes last (treated as very low priority)
                .comparing((Task t) -> t.getPriority() == null ? Integer.MAX_VALUE : t.getPriority())
                .thenComparing(byDeadlineDistance);

        Comparator<Task> finalComparator = includePriority ? byPriorityThenDeadline : byDeadlineDistance;

        if (includeCategory) {
            Map<String, List<Task>> grouped = new TreeMap<>();
            for (Task t : tasks) grouped.computeIfAbsent(t.getCategory(), k -> new ArrayList<>()).add(t);

            for (String category : grouped.keySet()) {
                pw.println(category);
                List<Task> list = grouped.get(category);
                list.sort(finalComparator);
                for (Task t : list) pw.println(t);
            }
        } else {
            List<Task> list = new ArrayList<>(tasks);
            list.sort(finalComparator);
            for (Task t : list) pw.println(t);
        }

        pw.flush();
    }


}

public class TasksManagerTest {

    public static void main(String[] args) {

        TaskManager manager = new TaskManager();

        System.out.println("Tasks reading");
        manager.readTasks(System.in);
        System.out.println("By categories with priority");
        manager.printTasks(System.out, true, true);
        System.out.println("-------------------------");
        System.out.println("By categories without priority");
        manager.printTasks(System.out, false, true);
        System.out.println("-------------------------");
        System.out.println("All tasks without priority");
        manager.printTasks(System.out, false, false);
        System.out.println("-------------------------");
        System.out.println("All tasks with priority");
        manager.printTasks(System.out, true, false);
        System.out.println("-------------------------");

    }
}
