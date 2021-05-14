package by.tms.Diplom.Service.Exception;

public class CategoriesNotFoundException extends RuntimeException{
    public CategoriesNotFoundException() {
    }

    public CategoriesNotFoundException(String message) {
        super(message);
    }
}
