package by.tms.Diplom.Service.Exception;

public class ItemsNotFoundException extends RuntimeException {

    public ItemsNotFoundException() {
    }
    public ItemsNotFoundException(String message) {
        super(message);
    }

}