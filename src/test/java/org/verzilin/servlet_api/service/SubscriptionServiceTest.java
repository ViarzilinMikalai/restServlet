package org.verzilin.servlet_api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.verzilin.servlet_api.dao.SubscriptionDao;
import org.verzilin.servlet_api.dao.impl.SubscriptionDaoImpl;
import org.verzilin.servlet_api.domain.User;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

class SubscriptionServiceTest {
    private SubscriptionDao subscriptionDao = mock(SubscriptionDaoImpl.class);
    private SubscriptionService subscriptionService = new SubscriptionService(subscriptionDao);

    @Test
    void getData() throws JsonProcessingException {
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");

        User user3 = new User();
        user3.setId(3L);
        user3.setUsername("user3");

        Set<User> allUsers = new HashSet<>();
        allUsers.add(user2);
        allUsers.add(user3);

        when(subscriptionDao.getSubscriptions(1L)).thenReturn(allUsers);
        when(subscriptionDao.getSubscribers(1L)).thenReturn(allUsers);

        subscriptionService.getData(1L, "subscriptions").get();
        subscriptionService.getData(1L, "").get();


        verify(subscriptionDao, times(1)).getSubscriptions(1L);
        verify(subscriptionDao, times(1)).getSubscribers(1L);
    }

    @Test
    void subscribe() {
        subscriptionService.subscribe(1L, 2L);
        verify(subscriptionDao, times(1)).subscribe(1L, 2L);
    }

    @Test
    void unsubscribe() {
        subscriptionService.unsubscribe(1L, 2L);
        verify(subscriptionDao, times(1)).unsubscribe(1L, 2L);
    }
}