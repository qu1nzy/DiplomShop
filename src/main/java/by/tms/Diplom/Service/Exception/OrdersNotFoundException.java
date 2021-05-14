package by.tms.Diplom.Service.Exception;

public class OrdersNotFoundException extends RuntimeException{

    public OrdersNotFoundException() {
    }

    public OrdersNotFoundException(String message) {
        super(message);
    }
}