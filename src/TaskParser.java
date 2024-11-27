import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class TaskParser {
    private final List<Task> tasks;
    private Task.Builder builder = new Task.Builder();

    private final Map<String, Consumer<String>> tak = Map.of(
            "id", builder::id,
            "description", builder::description,
            "status", builder::status,
            "createdAt", builder::createdAt,
            "updatedAt", builder::updatedAt
    );

    public TaskParser(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void parseJson(String line) {
        line = line
                .replaceAll("\"tasks\": *\\[", "")
                .replaceAll("]$", "");
        String[] split = line.split(":", 2);
        if (split.length != 2) {
            return;
        }
        String key = split[0].replaceAll("\"", "").trim();
        String value = split[1].replaceAll("\"", "").trim();
        tak.get(key).accept(value);
        if (builder.check()) {
            tasks.add(builder.build());
            builder = new Task.Builder();
        }
    }

    public static String toJson(List<Task> tasks) {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{\"tasks\":[");
        String tasksJson = tasks.stream()
                .map(Task::toJson)
                .collect(Collectors.joining(","));
        jsonBuilder.append(tasksJson);
        jsonBuilder.append("]}");
        return jsonBuilder.toString();
    }
}