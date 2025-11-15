package vulpes.parser;

import vulpes.exception.VulpesException;
import vulpes.storage.Storage;
import vulpes.task.Deadline;
import vulpes.task.Event;
import vulpes.task.Task;
import vulpes.task.Todo;
import vulpes.tasklist.TaskList;
import vulpes.ui.Ui;
import vulpes.command.Command;
import vulpes.command.AddCommand;
import vulpes.command.DeleteCommand;
import vulpes.command.ExitCommand;
import vulpes.command.ListCommand;
import vulpes.command.StatusCommand;

public class Parser {

    public static Command parse(String fullCommand) throws VulpesException { // parses full command string, only interprets and packages; supersedes the prior processor
        String[] parts = fullCommand.trim().split(" ", 2);
        String command = parts[0].toLowerCase();
        String params = (parts.length > 1) ? parts[1].trim() : ""; // handle spaces

        switch (command) { // only passing back to command, no execution
            case "bye":
                return new ExitCommand();

            case "list":
                return new ListCommand();

            case "delete":
                return parseDelete(params);

            case "mark":
            case "unmark":
                return parseStatus(command, params);

            case "todo":
            case "deadline":
            case "event":
                return parseAdd(command, params);

            default:
                throw new VulpesException("I don't know what you're talking about, but it sounds illegal (it looks like you aren't issuing a valid command!).");
        }
    }

    private static AddCommand parseAdd(String command, String params) throws VulpesException { // handlers moved
        if (params.isEmpty()) {
            throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (" + command + "' requires more details!).");
        }

        switch (command) {
            case "todo":
                if (params.contains(" /by") || params.contains(" /from") || params.contains(" /to")) {
                    throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (todo cannot use time keywords like /by, /from, or /to!).");
                }

                return new AddCommand(Command.TaskType.TODO, params, null, null, null); // create if checks passed

            case "deadline":
                if (!params.contains(" /by ")) {
                    throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (deadline command requires a '/by' keyword!).");
                }
                String[] deadlineParts = params.split(" /by ", 2);
                String deadlineDescription = deadlineParts[0].trim();
                String deadlineBy = (deadlineParts.length > 1) ? deadlineParts[1].trim() : "";

                if (deadlineDescription.isEmpty() || deadlineBy.isEmpty()) {
                    throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (deadline description and time cannot be empty!).");
                }

                return new AddCommand(Command.TaskType.DEADLINE, deadlineDescription, deadlineBy, null, null); // create if checks passed

            case "event":
                if (!params.contains(" /from ") || !params.contains(" /to ")) {
                    throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (event requires '/from' and '/to' keywords!).");
                }
                String[] fromParts = params.split(" /from ", 2);
                String eventDescription = fromParts[0].trim();
                if (fromParts.length < 2) {
                    throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (event requires dates after the keywords!).");
                }
                String[] toParts = fromParts[1].split(" /to ", 2);
                if (toParts.length < 2) {
                    throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (event requires dates after the keywords!).");
                }
                String eventFrom = toParts[0].trim();
                String eventTo = toParts[1].trim();

                if (eventDescription.isEmpty() || eventFrom.isEmpty() || eventTo.isEmpty()) {
                    throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (event description, start time, and end time cannot be empty!).");
                }

                return new AddCommand(Command.TaskType.EVENT, eventDescription, null, eventFrom, eventTo); // create if checks passed

            default: // in case
                throw new VulpesException("Uh-oh, we got it VERY wrong...");
        }
    }

    private static DeleteCommand parseDelete(String params) throws VulpesException { // handlers moved
        if (params.isEmpty()) {
            throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (delete command requires a task number!).");
        }
        try {
            int taskIndex = Integer.parseInt(params);
            return new DeleteCommand(taskIndex);
        } catch (NumberFormatException e) {
            throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (the task number requires... well, a number... not '" + params + "').");
        }
    }

    private static StatusCommand parseStatus(String command, String params) throws VulpesException { // handlers moved
        if (params.isEmpty()) {
            throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (" + command + " command requires a task number!).");
        }
        try {
            int taskIndex = Integer.parseInt(params);
            boolean isMark = command.equals("mark");
            return new StatusCommand(taskIndex, isMark);
        } catch (NumberFormatException e) {
            throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (the task number requires... well, a number... not '" + params + "').");
        }
    }
}