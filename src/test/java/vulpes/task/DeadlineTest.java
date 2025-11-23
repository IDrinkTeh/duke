package vulpes.task;

import java.time.LocalDateTime;
import java.time.format.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.format.DateTimeFormatter;

/**
 * Class conducts JUnit tests for deadline
 */
public class DeadlineTest {

    private DateTimeFormatter testFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm a"); // formatter to create dates for asserts, same as deadline formatting

    /**
     * Method tests constructor and initial state of new deadline
     * new deadline should be unmarked when created
     */
    @Test
    public void deadlineOne_notDone() {
        LocalDateTime by = LocalDateTime.of(2025, 11, 20, 23, 10); // around 11.10 pm 20 Nov at time of writing
        Deadline deadline = new Deadline("Do some last minute coding", by);
        assertFalse(deadline.isDone); // status is protected
    }

    /**
     * Method tests overloaded constructor created with marker
     * task should be marked as done
     */
    @Test
    public void deadlineTwo_isDone() {
        LocalDateTime by = LocalDateTime.of(2025, 11, 20, 23, 15); // around 11.15 pm 20 Nov at time of writing
        Deadline deadline = new Deadline("1", "Do some last minute coding", by);
        assertTrue(deadline.isDone);
    }

    /**
     * Method tests overloaded constructor created without marker
     * task should be marked as undone
     */
    @Test
    public void deadlineTwo_isNotDone() {
        LocalDateTime by = LocalDateTime.of(2025, 11, 20, 23, 15); // around 11.15 pm 20 Nov at time of writing
        Deadline deadline = new Deadline("0", "Do some last minute coding", by);
        assertFalse(deadline.isDone);
    }

    /**
     * Method tests getter
     * should return correct object
     */
    @Test
    public void deadline_correctGetter() {
        LocalDateTime expected = LocalDateTime.of(2025, 11, 20, 23, 15); // around 11.15 pm 20 Nov at time of writing
        Deadline deadline = new Deadline("Do some last minute coding", expected);
        assertEquals(expected, deadline.getBy());
    }

    /**
     * Method tests setter
     * should updates date and time correctly
     */
    @Test
    public void deadline_correctSetter() {
        LocalDateTime initial = LocalDateTime.of(2025, 11, 20, 23, 20); // around 11.20 pm 20 Nov at time of writing
        Deadline deadline = new Deadline("sixsevennn", initial);

        LocalDateTime amended = LocalDateTime.of(2026, 6, 7, 6, 7); // sixsevennn
        deadline.setBy(amended);

        assertEquals(amended, deadline.getBy());
    }

    /**
     * Method tests printing for deadline that is incomplete
     * status icon should be '[ ]'
     */
    @Test
    public void toString_notDone() {
        LocalDateTime by = LocalDateTime.of(2025, 11, 20, 23, 25); // around 11.25 pm 20 Nov at time of writing
        Deadline deadline = new Deadline("Do some last minute coding", by);

        String expectedDate = by.format(testFormatter); // date
        String expectedResult = "[D][ ] Do some last minute coding (by: " + expectedDate + ")";

        assertEquals(expectedResult, deadline.toString());
    }

    /**
     * Method tests printing for deadline that is complete
     * status icon should be '[X]'
     */
    @Test
    public void toString_isDone() {
        LocalDateTime by = LocalDateTime.of(2025, 11, 20, 23, 50); // around 11.50 pm 20 Nov at time of writing
        Deadline deadline = new Deadline("Im tired", by);
        deadline.setStatus(true); // mark done

        String expectedDate = by.format(testFormatter);
        String expectedResult = "[D][X] Im tired (by: " + expectedDate + ")";

        assertEquals(expectedResult, deadline.toString());
    }

    /**
     * Method tests writing for deadline that is complete
     * status data should be '0'
     */
    @Test
    public void toFileString_notDone() {
        LocalDateTime by = LocalDateTime.of(2025, 11, 20, 23, 55); // around 11.55 pm 20 Nov at time of writing
        Deadline deadline = new Deadline("I should sleep...", by);

        String expectedDate = by.format(testFormatter);
        String expectedResult = "D|0|I should sleep...|" + expectedDate;

        assertEquals(expectedResult, deadline.toFileString());
    }

    /**
     * Method tests writing for deadline that is complete
     * status data should be '1'
     */
    @Test
    public void toFileString_isDone() {
        LocalDateTime by = LocalDateTime.of(2025, 11, 20, 23, 55);
        Deadline deadline = new Deadline("Soon...", by);
        deadline.setStatus(true); // Mark the task as done

        String expectedDate = by.format(testFormatter);
        String expectedResult = "D|1|Soon...|" + expectedDate;

        assertEquals(expectedResult, deadline.toFileString());
    }
}