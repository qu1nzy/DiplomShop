package by.tms.Diplom.Listener;

import by.tms.Diplom.Entity.Basket;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
@Slf4j
public class Listener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        se.getSession().setAttribute("basket", new Basket());
        log.info("listener, sessionCreated - success");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        log.info("listener, sessionDestroyed - success");
    }
}