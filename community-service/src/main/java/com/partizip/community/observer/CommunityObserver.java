package com.partizip.community.observer;

@FunctionalInterface
public interface CommunityObserver {
    void notify(CommunityEvent event);
}
