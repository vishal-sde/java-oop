package SimpleProjects.ExceptionAndIO;

public class InvalidMarkException extends RuntimeException {
    public InvalidMarkException(String message) {
        super(message);
    }
}
