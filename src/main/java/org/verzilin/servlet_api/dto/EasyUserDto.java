package org.verzilin.servlet_api.dto;

import java.util.Objects;

public class EasyUserDto {
    private Long id;
    private String username;

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

    public EasyUserDto(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public EasyUserDto() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EasyUserDto that = (EasyUserDto) o;
        return Objects.equals(id, that.id) && Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }

    @Override
    public String toString() {
        return "EasyUserDto{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }
}
