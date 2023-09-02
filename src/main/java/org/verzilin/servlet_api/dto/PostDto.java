package org.verzilin.servlet_api.dto;

import java.util.Objects;

public class PostDto {
    private Long id;
    private String title;
    private String text;
    private EasyUserDto author;

    public PostDto(Long id, String title, String text, EasyUserDto author) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.author = author;
    }

    public PostDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public EasyUserDto getAuthor() {
        return author;
    }

    public void setAuthor(EasyUserDto author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostDto postDto = (PostDto) o;
        return Objects.equals(id, postDto.id) && Objects.equals(title, postDto.title) && Objects.equals(text, postDto.text) && Objects.equals(author, postDto.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, text, author);
    }
}
