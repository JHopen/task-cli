import java.util.Arrays;

public enum TaskStatus {
    TODO("todo"),
    IN_PROGRESS("in-progress"),
    DONE("done");


    private final String name;

    TaskStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static boolean valid(String value) {
        return Arrays.stream(values())
                .map(TaskStatus::getName)
                .anyMatch(status -> status.equals(value));
    }
}
