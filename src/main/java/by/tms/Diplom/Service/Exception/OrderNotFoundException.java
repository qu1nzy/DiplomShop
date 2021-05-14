package by.tms.Diplom.Service.Exception;

public class OrderNotFoundException extends RuntimeException{

    public OrderNotFoundException() {
    }

    public OrderNotFoundException(String message) {
        super(message);
    }
}