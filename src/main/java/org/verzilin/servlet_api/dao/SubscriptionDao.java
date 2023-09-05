package org.verzilin.servlet_api.dao;

import org.verzilin.servlet_api.domain.User;

import java.util.Set;

public interface SubscriptionDao {
    boolean subscribe(Long ownerId, Long subscriberId);
    boolean unsubscribe(Long ownerId, Long subscriberId);
    Set<User> getSubscribers (Long owner);
    Set<User> getSubscriptions (Long subscriber);
}
