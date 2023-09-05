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
import java.io.StringReader;
import java.sql.SQLException;
import java.util.Optional;

import static org.mockito.Mockito.*;

class PostServletTest {
    private PostService postService = mock(PostService.class);
    private HttpServletRequest request = mock(HttpServletRequest.class);
    private HttpServletResponse response = mock(HttpServletResponse.class);
    private PrintWriter printWriter = mock(PrintWriter.class);
    private BufferedReader reader = mock(BufferedReader.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private PostServlet postServlet = new PostServlet(postService);


    @Test
    void testDoGet() throws IOException, ServletException {
        EasyUserDto easyUserDto = new EasyUserDto();
        easyUserDto.setId(1L);
        easyUserDto.setUsername("user");

        PostDto postDto = new PostDto();
        postDto.setId(1L);
        postDto.setTitle("title");
        postDto.setText("text");
        postDto.setAuthor(easyUserDto);

        when(request.getPathInfo()).thenReturn("posts/1");
        when(postService.getPosts(1L)).thenReturn(Optional.of(objectMapper.writeValueAsString(postDto)));
        when(response.getWriter()).thenReturn(printWriter);

        postServlet.doGet(request, response);

        verify(response).setContentType("application/json; charset=UTF-8");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response.getWriter()).write(objectMapper.writeValueAsString(postDto));
    }

    @Test
    void testDoPost() throws ServletException, IOException, SQLException {
        String bodyParams = "{\"id\":1,\"title\":\"title\",\"text\":\"text\",\"author\":{\"id\":1,\"username\":\"user\"}}";
        when(request.getReader()).thenReturn(reader);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(bodyParams)));
        when(response.getWriter()).thenReturn(printWriter);
        when(postService.createPost(bodyParams)).thenReturn(true);

        postServlet.doPost(request, response);

        verify(postService, times(1)).createPost(bodyParams);
        verify(response, times(1)).setStatus(HttpServletResponse.SC_CREATED);
        verify(response, times(1)).setCharacterEncoding("UTF-8");
        verify(printWriter, times(1)).write("Объект создан!");
    }
    @Test
    void testDoPut() throws ServletException, IOException, SQLException {
        String bodyParams = "{\"id\":1,\"title\":\"title\",\"text\":\"text\",\"author\":{\"id\":1,\"username\":\"user\"}}";
        String requestPath = "posts/1";
        when(request.getPathInfo()).thenReturn(requestPath);
        when(request.getReader()).thenReturn(reader);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(bodyParams)));
        when(response.getWriter()).thenReturn(printWriter);
        when(postService.updatePost(1L, bodyParams)).thenReturn(true);

        postServlet.doPut(request, response);

        verify(postService, times(1)).updatePost(1L, bodyParams);
        verify(response, times(1)).setStatus(HttpServletResponse.SC_OK);
        verify(response, times(1)).setCharacterEncoding("UTF-8");
        verify(printWriter, times(1)).write("Объект обновлен!");
    }

    @Test
    void testDoDelete() throws IOException, ServletException {
        String requestPath = "posts/1";
        when(request.getPathInfo()).thenReturn(requestPath);
        when(response.getWriter()).thenReturn(printWriter);
        when(postService.removePost(1L)).thenReturn(true);

        postServlet.doDelete(request, response);

        verify(postService, times(1)).removePost(1L);
        verify(response, times(1)).setStatus(HttpServletResponse.SC_OK);
        verify(response, times(1)).setCharacterEncoding("UTF-8");
        verify(printWriter, times(1)).write("Объект удален!");
    }
}