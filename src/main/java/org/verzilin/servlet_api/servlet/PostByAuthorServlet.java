package org.verzilin.servlet_api.servlet;

import org.verzilin.servlet_api.service.PostService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@WebServlet(name = "PostByAuthorServlet", urlPatterns = "/posts-by-author/*")
public class PostByAuthorServlet extends HttpServlet {
    private static final String CHAR_SET = "UTF-8";

    private final PostService postService;

    public PostByAuthorServlet(PostService postService) {
        this.postService = postService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestPath = req.getPathInfo();
        String[] pathArray = requestPath.split("/");
        Long id = (pathArray.length > 0) ? Long.parseLong(pathArray[1]) : null;

        Optional<String> getResponse = postService.getPostsByAuthorId(id);
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
}
