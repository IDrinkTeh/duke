package vulpes.parser;

import vulpes.exception.VulpesException;
import vulpes.command.Command;
import vulpes.command.AddCommand;
import vulpes.command.DeleteCommand;
import vulpes.command.ExitCommand;
import vulpes.command.ListCommand;
import vulpes.command.StatusCommand;

import java.time.*;
import java.time.LocalDateTime;

/**
 * Class that parses input from the user and decides execution depending on user input
 */
public class Parser {
    /**
     * Method that parses input from user and decides which command to execute
     * @param fullCommand Whole line from user to parse
     * @throws VulpesException if any part of line is not issued in format expected
     */
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

    /**
     * Method that handles addition of new task to list
     * Includes checks and handlers to ensure data is valid for execution
     * @param command Indicates which of the 3 possible tasks were selected for addition
     * @param params Required parameters for proper execution of specified command
     * @throws VulpesException if any part of line is not issued in format expected
     */
    private static AddCommand parseAdd(String command, String params) throws VulpesException { // handlers moved
        if (params.isEmpty()) {
            throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (" + command + "' requires more details!).");
        }

        LocalDateTime datetime;
        LocalDateTime datetime2;

        switch (command) {
            case "todo":
                if (params.contains(" /by") || params.contains(" /from") || params.contains(" /to")) {
                    throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (todo cannot use time keywords like /by, /from, or /to!).");
                }

                return new AddCommand(Command.TaskType.TODO, params); // create if checks passed

            case "deadline":
                if (!params.contains(" /by ")) {
                    throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (deadline command requires a '/by' keyword!).");
                }
                String[] deadlineParts = params.split(" /by ", 2); // lump the time and date into one index in the array, if both time and date exists
                String deadlineDescription = deadlineParts[0].trim();
                String deadlineBy = (deadlineParts.length > 1) ? deadlineParts[1].trim() : "";

                if (deadlineDescription.isEmpty() || deadlineBy.isEmpty()) {
                    throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (deadline description and datetime cannot be empty!).");
                }

                // time : hh:mm in 24hr, date : dd-mm-yyyy
                // allow user 2 options
                // deadline description time -> defaults to today
                // deadline description time date
                // only accept in certain format, todo: flesh out datetime, not in interest of time at the moment

                deadlineBy = deadlineBy.trim();

                if (deadlineBy.length() == 5) { // time only
                    try {
                        datetime = LocalDate.now().atTime(LocalTime.parse(deadlineBy)); // https://www.baeldung.com/java-combine-local-date-time // https://www.geeksforgeeks.org/java/java-time-localtime-class-in-java/
                    } catch (Exception e) {
                        throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (time entered is invalid!).");
                    }
                } else if (deadlineBy.length() == 16) { // assume user is keying in both time and date
                    try {
                        datetime = LocalDateTime.parse(deadlineBy);
                    } catch (Exception e) {
                        throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (date entered is invalid!");
                    }
                } else {
                    throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (date/time should be in 'hh:mm' or 'hh:mm dd-mmy-yyy' format!");
                }

                if (datetime.isBefore(LocalDateTime.now())) { // take it that entering deadlines right as of this current minute is acceptable still ; https://www.baeldung.com/java-comparing-dates
                    throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (the deadline is already over!).");
                }

                return new AddCommand(Command.TaskType.DEADLINE, deadlineDescription, datetime); // create if checks passed

            case "event":
                if (!params.contains(" /from ") || !params.contains(" /to ")) {
                    throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (event requires '/from' and '/to' keywords!).");
                }

                String[] fromParts = params.split(" /from ", 2); // lump from and to into one index in the array
                String eventDescription = fromParts[0].trim();

                if (fromParts.length < 2) {
                    throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (event requires date/times after the keywords!).");
                }

                String[] toParts = fromParts[1].split(" /to ", 2); // split from and to, both have date and time lumped together (if date and time both exists)

                if (toParts.length < 2) {
                    throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (event requires both start and end date/times after the keywords!).");
                }

                String eventFrom = toParts[0].trim();
                String eventTo = toParts[1].trim();

                if (eventDescription.isEmpty() || eventFrom.isEmpty() || eventTo.isEmpty()) {
                    throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (event description, start date/time, and end date/time cannot be empty!).");
                }

                // time : hh:mm in 24hr, date : dd-mm-yyyy
                // allow user 3 options
                // event description time time -> defaults to today
                // event description time time date -> start time today end datetime
                // event description time date time date
                // only accept in certain format, todo: flesh out datetime, not in interest of time at the moment

                eventFrom = eventFrom.trim();

                if (eventFrom.length() == 5 && eventTo.length() == 5) { // event description time time
                    try {
                        datetime = LocalDate.now().atTime(LocalTime.parse(eventFrom)); // https://www.baeldung.com/java-combine-local-date-time // https://www.geeksforgeeks.org/java/java-time-localtime-class-in-java/
                        datetime2 = LocalDate.now().atTime(LocalTime.parse(eventTo));
                    } catch (Exception e) {
                        throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (time(s) entered are invalid!).");
                    }
                } else if (eventFrom.length() == 5 && eventTo.length() == 16) { // event description time time date
                    try {
                        datetime = LocalDate.now().atTime(LocalTime.parse(eventFrom));
                        datetime2 =  LocalDateTime.parse(eventTo);
                    } catch (Exception e) {
                        throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (dates/times entered are invalid!");
                    }
                } else if (eventFrom.length() == 16 && eventTo.length() == 16) { // event description time date time date
                    try {
                        datetime = LocalDateTime.parse(eventFrom);
                        datetime2 = LocalDateTime.parse(eventTo);
                    } catch (Exception e) {
                        throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (dates/times entered are invalid!");
                    }
                } else {
                       throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (dates and/or times should be in 'hh:mm' or 'hh:mm dd-mmy-yyy' format!");
                }

                if (datetime2.isBefore(LocalDateTime.now())) { // take it that entering deadlines right as of this current minute is acceptable still ; take it that entering a start datetime before this moment is fine too, only emd date datetime before this moment not acceptable
                    throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (the deadline is already over!).");
                }

                if (datetime2.isBefore(datetime)) {
                    throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (start date/time is later than end date/time!)");
                }

                return new AddCommand(Command.TaskType.EVENT, eventDescription, datetime, datetime2); // create if checks passed

            default: // in case
                throw new VulpesException("Uh-oh, we got it VERY wrong...");
        }
    }

    /**
     * Method that handles deletion of existing task from list
     * Includes checks and handlers to ensure data is valid for execution
     * @param params Required parameters for proper execution of specified command
     * @throws VulpesException if any part of line is not issued in format expected
     */
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

    /**
     * Method that handles deletion of existing task from list
     * Includes checks and handlers to ensure data is valid for execution
     * @param command Indicates whether selected task is to be marked or unmarked
     * @param params Required parameters for proper execution of specified command
     * @throws VulpesException if any part of line is not issued in format expected
     */
    private static StatusCommand parseStatus(String command, String params) throws VulpesException { // handlers moved
        if (params.isEmpty()) {
            throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (" + command + " command requires a task number!).");
        }
        try {
            int taskIndex = Integer.parseInt(params);
            boolean isDone = command.equals("mark");
            return new StatusCommand(taskIndex, isDone);
        } catch (NumberFormatException e) {
            throw new VulpesException("I'm sorry. Maybe my invitation got lost in the mail... (the task number requires... well, a number... not '" + params + "').");
        }
    }
}