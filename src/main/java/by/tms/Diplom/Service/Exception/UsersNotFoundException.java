package by.tms.Diplom.Service.Exception;

public class UsersNotFoundException extends RuntimeException{

    public UsersNotFoundException() {
    }

    public UsersNotFoundException(String message) {
        super(message);
    }
}
