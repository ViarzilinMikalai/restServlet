package org.verzilin.servlet_api.dto;

import java.util.*;

public class UserDto {
    private Long id;
    private String username;
    private String password;
    private Set<EasyUserDto> subscribers = new HashSet<>();
    private Set<EasyUserDto> subscriptions = new HashSet<>();
    private List<PostDto> posts = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<EasyUserDto> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(Set<EasyUserDto> subscribers) {
        this.subscribers = subscribers;
    }

    public Set<EasyUserDto> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Set<EasyUserDto> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public List<PostDto> getPosts() {
        return posts;
    }

    public void setPosts(List<PostDto> posts) {
        this.posts = posts;
    }

    public UserDto() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return Objects.equals(id, userDto.id) && Objects.equals(username, userDto.username) && Objects.equals(password, userDto.password) && Objects.equals(subscribers, userDto.subscribers) && Objects.equals(subscriptions, userDto.subscriptions) && Objects.equals(posts, userDto.posts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, subscribers, subscriptions, posts);
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", subscribers=" + subscribers +
                ", subscriptions=" + subscriptions +
                ", posts=" + posts +
                '}';
    }
}
