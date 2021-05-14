package by.tms.Diplom.Service.Exception;


public class ItemAlreadyExistsException extends RuntimeException {

    public ItemAlreadyExistsException() {
    }

    public ItemAlreadyExistsException(String message) {
        super(message);
    }

}
