package model;

import com.google.common.eventbus.EventBus;

public class EventBusClass {
    private static EventBus instance;

    public static EventBus getInstance() {
        if (instance == null) {
            synchronized (EventBusClass.class) {
                if (null == instance) {
                    instance = new EventBus();
                }
            }
        }
        return instance;
    }
}
