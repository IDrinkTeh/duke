package vulpes.parser;

import vulpes.command.*;
import vulpes.exception.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Class that parses input from the user and decides execution depending on user input
 */
public class Parser {
    // AI enhanced formatters for more versatility and to prevent bugs from user input
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATETIME_FORMAT_DASH = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
    private static final DateTimeFormatter DATETIME_FORMAT_DOT = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
    private static final DateTimeFormatter DATE_FORMAT_DASH = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter DATE_FORMAT_DOT = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    /**
     * Method that parses input from user and decides which command to execute
     *
     * @param fullCommand Whole line from user to parse
     * @return executable Command object
     * @throws VulpesException if any part of line is not issued in format expected
     */
    public static Command parse(String fullCommand) throws VulpesException { // parses full command string, only interprets and packages; supersedes the prior processor
        String[] parts = fullCommand.trim().split(" ", 2); // split command from rest of the params
        String command = parts[0].toLowerCase(); // handle case issues
        String params = (parts.length > 1) ? parts[1].trim() : ""; // handle spaces

        switch (command) { // only passing back to command, no execution
        case "bye":
            return new ExitCommand();

        case "list":
            return new ListCommand();

        case "view":
            return parseView(params);

        case "find":
            return new FindCommand(params);

        case "archives":
            return new ArchivesCommand();

        case "help!":
            return new HelpCommand();

        case "archive": // fallthrough
        case "unarchive":
            return parseArchive(command, params);

        case "delete":
            return parseDelete(params);

        case "mark": // fallthrough
        case "unmark":
            return parseStatus(command, params);

        case "todo": // fallthrough
        case "deadline": // fallthrough
        case "event":
            return parseAdd(command, params);

        default:
            throw new InvalidCommandException(); // specific exception for invalid command
        }
    }

    /**
     * Method to parse datetime string from input
     * Made more robust with AI help - tries multiple formats - time-only, time with date
     *
     * @param dateTimeString The string to parse, e.g., "20:00" or "21:00 24-11-2025".
     * @return A LocalDateTime object. If only time is provided, it defaults to today's date.
     * @throws DateTimeParseException if the string cannot be parsed with any known format.
     */
    private static LocalDateTime parseDateTimeString(String dateTimeString) throws DateTimeParseException {
        // parse as full date and time first (dash format)
        try {
            return LocalDateTime.parse(dateTimeString, DATETIME_FORMAT_DASH);
        } catch (DateTimeParseException e) {
        } // fallthrough to next format

        // try dot format
        try {
            return LocalDateTime.parse(dateTimeString, DATETIME_FORMAT_DOT);
        } catch (DateTimeParseException e) {
        } // fallthrough to next format

        // try to parse as time-only as last case
        LocalTime time = LocalTime.parse(dateTimeString, TIME_FORMAT);
        return LocalDate.now().atTime(time); // default as today's date
    }

    /**
     * Method that handles addition of new task to list
     * Includes checks and handlers to ensure data is valid for execution
     *
     * @param command Indicates which of the 3 possible tasks were selected for addition
     * @param params  Required parameters for proper execution of specified command
     * @throws VulpesException if any part of line is not issued in format expected
     */
    private static AddCommand parseAdd(String command, String params) throws VulpesException { // had some AI tuneups
        // https://www.baeldung.com/java-combine-local-date-time
        // https://www.geeksforgeeks.org/java/java-time-localtime-class-in-java/
        // https://www.baeldung.com/java-comparing-dates

        if (params.isEmpty()) {
            throw new InvalidParametersException(command); // specific exception for command that has invalid params
        }

        switch (command) {
        case "todo":
            if (params.contains("/by") || params.contains("/from") || params.contains("/to")) {
                throw new InvalidDatetimeFormatException("To-do cannot use time keywords like /by, /from, or /to!");
            }
            return new AddCommand(Command.TaskType.TODO, params); // create if checks passed

        case "deadline":
            if (params.contains("/from") || params.contains("/to")) {
                throw new InvalidDatetimeFormatException("Deadline cannot use keywords /from and/or /to!");
            }

            if (!params.contains("/by ")) {
                throw new InvalidDatetimeFormatException("Deadline requires /by keyword!");
            }
            String[] parts = params.split(" /by ", 2); // lump the time and date into one index in the array, if both time and date exists
            String description = parts[0].trim();
            if (description.isEmpty() || parts.length < 2 || parts[1].trim().isEmpty()) {
                throw new InvalidParametersException("Deadline description and date/time cannot be empty.");
            }

            // time : hh:mm in 24hr, date : dd-mm-yyyy
            // allow user 2 options
            // deadline description time -> defaults to today
            // deadline description time date
            // only accept in certain format

            String byString = parts[1].trim(); // handle spaces

            try {
                LocalDateTime by = parseDateTimeString(byString);
                if (by.isBefore(LocalDateTime.now())) {
                    throw new InvalidDatetimeException(); // specific exception for invalid datetime
                }
                return new AddCommand(Command.TaskType.DEADLINE, description, by); // create if checks passed
            } catch (DateTimeParseException e) {
                throw new InvalidParametersException("deadline");
            }

        case "event":
            if (!params.contains(" /from ") || !params.contains(" /to ")) {
                throw new InvalidDatetimeFormatException("Event requires '/from' and '/to' keywords.");
            }
            String[] fromParts = params.split(" /from ", 2); // lump from and to into one index in the array
            description = fromParts[0].trim(); // handle spaces

            if (description.isEmpty() || fromParts.length < 2) {
                throw new InvalidParametersException("Event requires a description and date/times.");
            }
            String[] toParts = fromParts[1].split(" /to ", 2); // split from and to, both have date and time lumped together (if date and time both exists)
            if (toParts.length < 2 || toParts[0].trim().isEmpty() || toParts[1].trim().isEmpty()) {
                throw new InvalidParametersException(command); // missing params
            }
            String fromString = toParts[0].trim(); // handle spaces
            String toString = toParts[1].trim(); // handle spaces

            try {
                LocalDateTime from = parseDateTimeString(fromString);
                LocalDateTime to = parseDateTimeString(toString);

                if (to.isBefore(from)) {
                    throw new InvalidDatetimeException(); // specific exception for invalid datetime
                }
                if (to.isBefore(LocalDateTime.now())) {
                    throw new InvalidDatetimeException(); // specific exception for invalid datetime
                }
                return new AddCommand(Command.TaskType.EVENT, description, from, to); // create if checks passed
            } catch (DateTimeParseException e) {
                throw new InvalidParametersException("event");
            }
        default:
            assert false : "Somehow hit unreachable case in parseAdd. Command was: " + command;
            throw new CriticalException(); // just in case assertions are disabled during testing
        }
    }

    /**
     * Method that handles deletion of existing task from list
     * Includes checks and handlers to ensure data is valid for execution
     *
     * @param params Required parameters for proper execution of specified command
     * @throws VulpesException if any part of line is not issued in format expected
     */
    private static DeleteCommand parseDelete(String params) throws VulpesException { // handlers moved
        if (params.isEmpty()) {
            throw new InvalidParametersException("delete"); // specific exception for command that has invalid params
        }
        try {
            int taskIndex = Integer.parseInt(params);
            return new DeleteCommand(taskIndex);
        } catch (NumberFormatException e) {
            throw new InvalidParametersException("delete"); // specific exception for command that has invalid params
        }
    }

    /**
     * Method that handles deletion of existing task from list
     * Includes checks and handlers to ensure data is valid for execution
     *
     * @param command Indicates whether selected task is to be marked or unmarked
     * @param params  Required parameters for proper execution of specified command
     * @throws VulpesException if any part of line is not issued in format expected
     */
    private static StatusCommand parseStatus(String command, String params) throws VulpesException { // handlers moved
        if (params.isEmpty()) {
            throw new InvalidParametersException("mark/unmark"); // specific exception for command that has invalid params
        }
        try {
            int taskIndex = Integer.parseInt(params);
            boolean isDone = command.equals("mark");
            return new StatusCommand(taskIndex, isDone);
        } catch (NumberFormatException e) {
            throw new InvalidParametersException("mark/unmark"); // specific exception for command that has invalid params
        }
    }

    /**
     * Method that handles movement of task to and from archives
     * Includes checks and handlers to ensure data is valid for execution
     *
     * @param command Indicates whether selected task is to be archived or unarchived
     * @param params  Required parameters for proper execution of specified command
     * @throws VulpesException if any part of line is not issued in format expected
     */
    private static ArchiveCommand parseArchive(String command, String params) throws VulpesException { // handlers moved
        if (params.isEmpty()) {
            throw new InvalidParametersException("archive/unarchive"); // specific exception for command that has invalid params
        }
        try {
            int taskIndex = Integer.parseInt(params);
            boolean isDone = command.equals("archive");
            return new ArchiveCommand(taskIndex, isDone);
        } catch (NumberFormatException e) {
            throw new InvalidParametersException("archive/unarchive"); // specific exception for command that has invalid params
        }
    }

    /**
     * Method that handles search of date from list and archives
     * Includes checks and handlers to ensure data is valid for execution
     *
     * @param params  Required parameters for proper execution of specified command
     * @throws InvalidDateException if any part of line is not issued in format expected
     */
    private static ViewCommand parseView(String params) throws InvalidDateException {
        try {
            LocalDate criteria = LocalDate.parse(params, DATE_FORMAT_DOT);
            return new ViewCommand(criteria); // create if checks passed
        } catch (DateTimeParseException e) {
        } // fallthrough to next format
        try {
            LocalDate criteria = LocalDate.parse(params, DATE_FORMAT_DASH);
            return new ViewCommand(criteria); // create if checks passed
        } catch (DateTimeParseException e) {
            throw new InvalidDateException();
        }
    }

}