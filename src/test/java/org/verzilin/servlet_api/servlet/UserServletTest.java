package org.verzilin.servlet_api.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.verzilin.servlet_api.dto.UserDto;
import org.verzilin.servlet_api.service.UserService;

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

class UserServletTest {
    private UserService userService = mock(UserService.class);
    private HttpServletRequest request = mock(HttpServletRequest.class);
    private HttpServletResponse response = mock(HttpServletResponse.class);
    private PrintWriter printWriter = mock(PrintWriter.class);
    private BufferedReader reader = mock(BufferedReader.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private UserServlet userServlet = new UserServlet(userService);


    @Test
    void testDoGet() throws IOException, ServletException {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("user");

        when(request.getPathInfo()).thenReturn("courses/1");
        when(userService.getUsers(1L)).thenReturn(Optional.of(objectMapper.writeValueAsString(userDto)));
        when(response.getWriter()).thenReturn(printWriter);

        userServlet.doGet(request, response);

        verify(response).setContentType("application/json; charset=UTF-8");
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response.getWriter()).write(objectMapper.writeValueAsString(userDto));
    }

    @Test
    void testDoPost() throws ServletException, IOException, SQLException {
        String bodyParams = "{\"id\":1,\"username\":\"user\",\"password\":null,\"subscribers\":[],\"subscriptions\":[]," +
                "\"posts\":[{\"id\":1,\"title\":\"title\",\"text\":\"text\",\"author\":{\"id\":1,\"username\":\"user\"}}]}";
        when(request.getReader()).thenReturn(reader);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(bodyParams)));
        when(response.getWriter()).thenReturn(printWriter);
        when(userService.createUser(bodyParams)).thenReturn(true);

        userServlet.doPost(request, response);

        verify(userService, times(1)).createUser(bodyParams);
        verify(response, times(1)).setStatus(HttpServletResponse.SC_CREATED);
        verify(response, times(1)).setCharacterEncoding("UTF-8");
        verify(printWriter, times(1)).write("Объект создан!");
    }
    @Test
    void testDoPut() throws ServletException, IOException, SQLException {
        String bodyParams = "{\"id\":1,\"username\":\"user\",\"password\":\"password\",\"subscribers\":[],\"subscriptions\":[]}";
        String requestPath = "users/1";
        when(request.getPathInfo()).thenReturn(requestPath);
        when(request.getReader()).thenReturn(reader);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(bodyParams)));
        when(response.getWriter()).thenReturn(printWriter);
        when(userService.updateUser(1L, bodyParams)).thenReturn(true);

        userServlet.doPut(request, response);

        verify(userService, times(1)).updateUser(1L, bodyParams);
        verify(response, times(1)).setStatus(HttpServletResponse.SC_OK);
        verify(response, times(1)).setCharacterEncoding("UTF-8");
        verify(printWriter, times(1)).write("Объект обновлен!");
    }

    @Test
    void testDoDelete() throws IOException, ServletException {
        String requestPath = "users/1";
        when(request.getPathInfo()).thenReturn(requestPath);
        when(response.getWriter()).thenReturn(printWriter);
        when(userService.removeUser(1L)).thenReturn(true);

        userServlet.doDelete(request, response);

        verify(userService, times(1)).removeUser(1L);
        verify(response, times(1)).setStatus(HttpServletResponse.SC_OK);
        verify(response, times(1)).setCharacterEncoding("UTF-8");
        verify(printWriter, times(1)).write("Объект удален!");
    }
}