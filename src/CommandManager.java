import org.graalvm.collections.Pair;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class CommandManager {
    private final static Map<String, Pair<BiConsumer<String[], List<Task>>, Function<String[], Boolean>>> COMMANDS = Map.of(
            "add", Pair.create(CommandManager::add, CommandManager::validateAdd),
            "update", Pair.create(CommandManager::update, CommandManager::validateUpdate),
            "delete", Pair.create(CommandManager::delete, CommandManager::validateDelete),
            "list", Pair.create(CommandManager::list, CommandManager::validateList),
            "mark-in-progress", Pair.create(CommandManager::markInProgress, CommandManager::validateMarkInProgress),
            "mark-done", Pair.create(CommandManager::markDone, CommandManager::validateMarkDone));

    public static BiConsumer<String[], List<Task>> getImplementation(String command) {
        return COMMANDS.get(command).getLeft();
    }

    public static Function<String[], Boolean> getValidator(String command) {
        return COMMANDS.containsKey(command) ? COMMANDS.get(command).getRight() : (a) -> false;
    }

    private static void add(final String[] command, final List<Task> tasks) {
        List<Long> list = tasks.stream()
                .map(Task::getId)
                .sorted()
                .toList()
                .reversed();
        long current = list.isEmpty() ? 0L : list.getFirst();
        Task task = new Task(++current, command[1], TaskStatus.TODO, LocalDateTime.now(), LocalDateTime.now());
        tasks.add(task);
        System.out.println("Task added with id " + current);
    }

    private static void update(final String[] command, final List<Task> tasks) {
        tasks.stream()
                .filter(task -> task.getId() == Long.parseLong(command[1]))
                .findFirst()
                .ifPresent(task -> {
                    task.setDescription(command[2]);
                    task.setUpdatedAt(LocalDateTime.now());
                });
    }

    private static void delete(final String[] command, final List<Task> tasks) {
        tasks.removeIf(task -> task.getId() == Long.parseLong(command[1]));
    }

    private static void list(final String[] command, final List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("Task list is empty");
            return;
        }
        System.out.println("id | description | status | created | updated");
        tasks.stream()
                .filter(task -> command.length != 2 || task.getStatus() == TaskStatus.valueOf(command[1]))
                .forEach(System.out::println);
    }

    private static void markInProgress(final String[] command, final List<Task> tasks) {
        tasks.stream().filter(task -> task.getId() == Long.parseLong(command[1]))
                .findFirst()
                .ifPresent(task -> task.setStatus(TaskStatus.IN_PROGRESS));
    }

    private static void markDone(final String[] command, final List<Task> tasks) {
        tasks.stream().filter(task -> task.getId() == Long.parseLong(command[1]))
                .findFirst()
                .ifPresent(task -> task.setStatus(TaskStatus.DONE));
    }

    private static boolean validateAdd(String[] command) {
        return command.length == 2 && command[0].equals("add");
    }

    private static boolean validateUpdate(String[] command) {
        return command.length == 3 && command[0].equals("update") && isNumber(command[1]);
    }

    private static boolean validateDelete(String[] command) {
        return command.length == 2 && command[0].equals("delete") && isNumber(command[1]);
    }

    private static boolean validateList(String[] command) {
        return command.length == 1 || (command.length == 2 && TaskStatus.valid(command[1]));
    }

    private static boolean validateMarkInProgress(String[] command) {
        return command.length == 2 && command[0].equals("mark-in-progress") && isNumber(command[1]);
    }

    private static boolean validateMarkDone(String[] command) {
        return command.length == 2 && command[0].equals("mark-delete") && isNumber(command[1]);
    }

    private static boolean isNumber(final String number) {
        return number.matches("^\\d+$");
    }
}
