package com.partizip.event.interfaces;

import com.partizip.event.entity.EventNotification;

public interface EventObserver {
    void notify(EventNotification event);
}
