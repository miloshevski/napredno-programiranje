package aud9;

import java.util.*;
import java.util.stream.Collectors;

class DurationConverter {
    public static String convert(long duration) {
        long minutes = duration / 60;
        duration %= 60;
        return String.format("%02d:%02d", minutes, duration);
    }
}

enum CallStatus {
    INITIALIZED,
    IN_PROGRESS,
    PAUSED,
    ENDED
}

class InvalidOperation extends Exception {
}



class Call {
    String uuid;
    String dialer;
    String receiver;

    long initialized;
    long start = 0;
    long end = 0;

    long holdStarted = 0;
    long totalHoldDuration = 0;

    CallStatus status = CallStatus.INITIALIZED;

    public Call(String uuid, String dialer, String receiver, long initialized) {
        this.uuid = uuid;
        this.dialer = dialer;
        this.receiver = receiver;
        this.initialized = initialized;
    }

    public void updateCall(long timestamp, String action) throws InvalidOperation {

        if (action.equals("ANSWER")) {
            if (status != CallStatus.INITIALIZED)
                throw new InvalidOperation();

            start = timestamp;
            status = CallStatus.IN_PROGRESS;
        }

        else if (action.equals("HOLD")) {
            if (status != CallStatus.IN_PROGRESS)
                throw new InvalidOperation();

            holdStarted = timestamp;
            status = CallStatus.PAUSED;
        }

        else if (action.equals("RESUME")) {
            if (status != CallStatus.PAUSED)
                throw new InvalidOperation();

            totalHoldDuration += (timestamp - holdStarted);
            holdStarted = 0;
            status = CallStatus.IN_PROGRESS;
        }

        else if (action.equals("END")) {
            if (status == CallStatus.ENDED)
                throw new InvalidOperation();

            if (start == 0) {
                // missed call
                start = timestamp;
            }

            if (status == CallStatus.PAUSED) {
                totalHoldDuration += (timestamp - holdStarted);
                holdStarted = 0;
            }

            end = timestamp;
            status = CallStatus.ENDED;
        }
    }

    public long getStart() {
        return start == 0 ? initialized : start;
    }

    public long getTotalDuration() {
        if (end == 0) return 0;
        return end - start - totalHoldDuration;
    }

    public String getUuid() {
        return uuid;
    }
}

class TelcoApp {
    Map<String, Call> callsByUuid = new HashMap<>();
    Map<String, List<Call>> callsByPhoneNumber = new HashMap<>();
    Comparator<Call> byStart = Comparator.comparing(Call::getStart).thenComparing(Call::getUuid);
    Comparator<Call> byDuration = Comparator.comparing(Call::getTotalDuration).thenComparing(Call::getStart).reversed();

    void addCall(String uuid, String dialer, String receiver, long timestamp) {
        Call c = new Call(uuid, dialer, receiver, timestamp);
        callsByUuid.put(uuid, c);
        callsByPhoneNumber.putIfAbsent(dialer, new ArrayList<>());
        callsByPhoneNumber.get(dialer).add(c);
        callsByPhoneNumber.putIfAbsent(receiver, new ArrayList<>());
        callsByPhoneNumber.get(receiver).add(c);
    }

    void updateCall(String uuid, long timestamp, String action) {
        try {
            callsByUuid.get(uuid).updateCall(timestamp, action);
        } catch (InvalidOperation e) {
            System.out.println("Invalid operation " + action + " for call " + uuid);
        }
    }

    void printCall(Call c, String phoneNumber) {
        String type = c.dialer.equals(phoneNumber) ? "D" : "R";
        String otherPhoneNumber = c.dialer.equals(phoneNumber) ? c.receiver : c.dialer;
        String end = c.start==c.end ? "MISSED CALL" : String.valueOf(c.end);
        System.out.println(String.format("%s %s %d %s %s", type, otherPhoneNumber, c.getStart(), end, DurationConverter.convert(c.getTotalDuration())));

    }

    void printChronologicalReport(String phoneNumber) {
        callsByPhoneNumber.get(phoneNumber).stream().sorted(byStart).forEach(c -> {
            printCall(c, phoneNumber);
        });
    }

    void printReportByDuration(String phoneNumber) {
        callsByPhoneNumber.get(phoneNumber).stream().sorted(byDuration).forEach(c -> {
            printCall(c, phoneNumber);
        });
    }

    public void printCallsDuration() {
        TreeMap<String, Long> result = callsByUuid.values().stream().collect(Collectors.groupingBy(
                c -> String.format("%s <-> %s", c.dialer, c.receiver),
                TreeMap::new,
                Collectors.summingLong(Call::getTotalDuration)
        ));

        result.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEach(entry -> System.out.printf("%s : %s%n", entry.getKey(), DurationConverter.convert(entry.getValue())));
    }
}


public class TelcoTest2 {
    public static void main(String[] args) {
        TelcoApp app = new TelcoApp();

        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");
            String command = parts[0];

            if (command.equals("addCall")) {
                String uuid = parts[1];
                String dialer = parts[2];
                String receiver = parts[3];
                long timestamp = Long.parseLong(parts[4]);
                app.addCall(uuid, dialer, receiver, timestamp);
            } else if (command.equals("updateCall")) {
                String uuid = parts[1];
                long timestamp = Long.parseLong(parts[2]);
                String action = parts[3];
                app.updateCall(uuid, timestamp, action);
            } else if (command.equals("printChronologicalReport")) {
                String phoneNumber = parts[1];
                app.printChronologicalReport(phoneNumber);
            } else if (command.equals("printReportByDuration")) {
                String phoneNumber = parts[1];
                app.printReportByDuration(phoneNumber);
            } else {
                app.printCallsDuration();
            }
        }

    }
}