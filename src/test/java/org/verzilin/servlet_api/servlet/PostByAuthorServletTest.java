package org.verzilin.servlet_api.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.verzilin.servlet_api.dto.EasyUserDto;
import org.verzilin.servlet_api.dto.PostDto;
import org.verzilin.servlet_api.service.PostService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

class PostByAuthorServletTest {
    private PostService postService = mock(PostService.class);
    private HttpServletRequest request = mock(HttpServletRequest.class);
    private HttpServletResponse response = mock(HttpServletResponse.class);
    private PrintWriter printWriter = mock(PrintWriter.class);
    private BufferedReader reader = mock(BufferedReader.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private PostByAuthorServlet postServlet = new PostByAuthorServlet(postService);

    @Test
    void doGet() throws IOException, ServletException {
        EasyUserDto easyUserDto = new EasyUserDto();
        easyUserDto.setId(1L);
        easyUserDto.setUsername("user");

        PostDto postDto = new PostDto();
        postDto.setId(1L);
        postDto.setTitle("title");
        postDto.setText("text");
        postDto.setAuthor(easyUserDto);

        List<PostDto> posts = List.of(postDto);

        when(request.getPathInfo()).thenReturn("posts-by-author/1");
        when(postService.getPostsByAuthorId(1L)).thenReturn(Optional.of(objectMapper.writeValueAsString(posts)));
        when(response.getWriter()).thenReturn(printWriter);

        postServlet.doGet(request, response);

        verify(response).setContentType("application/json; charset=UTF-8");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response.getWriter()).write(objectMapper.writeValueAsString(posts));
    }
}