package org.verzilin.servlet_api.servlet;

import org.testcontainers.shaded.org.apache.commons.lang3.StringUtils;
import org.verzilin.servlet_api.dao.impl.PostDaoImpl;
import org.verzilin.servlet_api.service.PostService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.stream.Collectors;

@WebServlet(name = "PostServlet", urlPatterns = "/posts/*")
public class PostServlet extends HttpServlet {
    private static final String CHAR_SET = "UTF-8";

    private final PostService postService;

    public PostServlet(PostService postService) {
        this.postService = postService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestPath = req.getPathInfo();
        String[] pathArray = requestPath.split("/");
        Long id = (pathArray.length > 0) ? Long.parseLong(pathArray[1]) : null;

        Optional<String> getResponse = postService.getPosts(id);
        if (getResponse.isPresent()) {
            String response = getResponse.get();
            resp.setContentType("application/json; charset=UTF-8");
            resp.setStatus(HttpServletResponse.SC_OK);
            PrintWriter printWriter = resp.getWriter();
            printWriter.write(response);
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.setCharacterEncoding(CHAR_SET);
            resp.getWriter().write("По запросу ничего не найдено!");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String bodyParams = req.getReader().lines().collect(Collectors.joining());
        if (postService.createPost(bodyParams)) {
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.setCharacterEncoding(CHAR_SET);
            resp.getWriter().write("Объект создан!");
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.setCharacterEncoding(CHAR_SET);
            resp.getWriter().write("Ошибка создания объекта!");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = getId(req);
        String bodyParams = req.getReader().lines().collect(Collectors.joining());

        if (postService.updatePost(id, bodyParams)) {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setCharacterEncoding(CHAR_SET);
            resp.getWriter().write("Объект обновлен!");
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setCharacterEncoding(CHAR_SET);
            resp.getWriter().write("Не удалось обновить объект!");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestPath = req.getParameter("id");
        Long id = getId(req);

        if (postService.removePost(id)) {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setCharacterEncoding(CHAR_SET);
            resp.getWriter().write("Объект удален!");
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setCharacterEncoding(CHAR_SET);
            resp.getWriter().write("Не удалось удалить объект!");
        }
    }

    private Long getId(HttpServletRequest req) {
        String requestPath = req.getPathInfo();
        String[] pathArray = requestPath.split("/");

        return Long.parseLong(pathArray[1]);
    }
}
