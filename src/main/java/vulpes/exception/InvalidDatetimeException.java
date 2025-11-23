package vulpes.exception;

/**
 * Class that handles when user inputs invalid datetime
 */
public class InvalidDatetimeException extends VulpesException {
    public InvalidDatetimeException() {
        super(
                """
                        I'm sorry. Maybe my invitation got lost in the mail... (invalid date(s) and/or time(s) entered!).
                        (Date(s)/time(s) should be in 'hh:mm' or 'hh:mm dd-mmy-yyy' format)
                        (Date(s)/Time(s) cannot be before current moment and start date(s)/time(s) cannot come after end date(s)/time(s))
                        (Please enter the correct date(s)/time(s) or type 'help!' for... help.)"""
        );
    }
}

// specific exception for invalid datetime