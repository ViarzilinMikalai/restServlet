package org.verzilin.servlet_api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.factory.Mappers;
import org.verzilin.servlet_api.dao.SubscriptionDao;
import org.verzilin.servlet_api.domain.User;
import org.verzilin.servlet_api.dto.EasyUserDto;
import org.verzilin.servlet_api.mapper.UserMapper;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class SubscriptionService {
    private SubscriptionDao subscriptionDao;

    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    private ObjectMapper objectMapper = new ObjectMapper();

    public Optional<String> getData(Long ownerId, String getData) throws JsonProcessingException {
        Optional<String> request = Optional.empty();

        Set<User> users;
        if (getData.equals("subscriptions")) {
            users = subscriptionDao.getSubscriptions(ownerId);
        } else {
            users = subscriptionDao.getSubscribers(ownerId);
        }

        if (!users.isEmpty()) {
            Set<EasyUserDto> userDtos = users
                    .stream()
                    .map(userMapper::toEasyUserDto)
                    .collect(Collectors.toSet());
            request = Optional.of(objectMapper.writeValueAsString(userDtos));
        }
        return request;
    }

    public SubscriptionService(SubscriptionDao subscriptionDao) {
        this.subscriptionDao = subscriptionDao;
    }

    public boolean subscribe(Long ownerId, Long subscriberId) {
        return subscriptionDao.subscribe(ownerId, subscriberId);
    }

    public boolean unsubscribe(Long ownerId, Long subscriberId) {
        return subscriptionDao.unsubscribe(ownerId, subscriberId);
    }
}
