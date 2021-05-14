package by.tms.Diplom.Service.Exception;

public class AdminExistException extends RuntimeException{

    public AdminExistException() {
    }

    public AdminExistException(String message) {
        super(message);
    }
}