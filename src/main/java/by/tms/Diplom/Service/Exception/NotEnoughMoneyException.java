package by.tms.Diplom.Service.Exception;

public class NotEnoughMoneyException extends RuntimeException{

    public NotEnoughMoneyException() {
    }

    public NotEnoughMoneyException(String message) {
        super(message);
    }
}
