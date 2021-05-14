package by.tms.Diplom.Service.Exception;

public class CategoryAlreadyExistsException extends RuntimeException{

    public CategoryAlreadyExistsException() {
    }

    public CategoryAlreadyExistsException(String message) {
        super(message);
    }
}