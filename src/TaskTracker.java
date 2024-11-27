
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TaskTracker {
    private static final String FILE_NAME = "tasks.txt";

    private static final List<Task> tasks = new ArrayList<>();

    public static void main(String[] args) {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            loadTasks(file);
        }

        if (!validateCommand(args)) {
            System.out.print("""
                    Invalid command
                    list of commands:
                    add <description>,
                    update <id> <new_description>,
                    delete <id>,
                    list [<status>],
                    mark-in-progress <id>,
                    mark-done <id>""");
            return;
        }

        executeCommand(args);

        String json = TaskParser.toJson(tasks);
        saveToFile(json);
    }

    private static void executeCommand(String[] command) {
        CommandManager.getImplementation(command[0]).accept(command, tasks);
    }

    private static boolean validateCommand(String[] command) {
        if (command.length == 0 || command.length > 3) {
            return false;
        }
        return CommandManager.getValidator(command[0]).apply(command);
    }

    private static void saveToFile(String json) {
        try (FileWriter fileWriter = new FileWriter(FILE_NAME);
             BufferedWriter writer = new BufferedWriter(fileWriter)) {
            writer.write(json);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void loadTasks(final File file) {
        TaskParser parser = new TaskParser(tasks);
        try (FileReader fileReader = new FileReader(file);
             BufferedReader reader = new BufferedReader(fileReader)) {
            Arrays.stream(reader.lines()
                            .map(line -> line.replaceAll("[{}]", ""))
                            .collect(Collectors.joining())
                            .split(",")).toList()
                    .forEach(parser::parseJson);
        } catch (IOException | IllegalStateException e) {
            System.out.printf("There is problem with existing file %s, please fix it manually, or remove it%n", FILE_NAME);
        }
    }
}
