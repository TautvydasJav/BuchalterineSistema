package system.exceptions;
import static system.constants.UserConstants.*;

public class UserException extends Exception {
    public UserException() {
        super(EXCEPTION_MSG);
    }
}
