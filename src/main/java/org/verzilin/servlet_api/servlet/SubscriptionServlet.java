package org.verzilin.servlet_api.servlet;


import org.verzilin.servlet_api.service.SubscriptionService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@WebServlet(name = "SubscriptionServlet", urlPatterns = "/subscriptions")
public class SubscriptionServlet extends HttpServlet {
    private static final String CHAR_SET = "UTF-8";

    private final SubscriptionService subscriptionService;

    public SubscriptionServlet(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long ownerId = Long.parseLong(req.getParameter("owner_id"));

        String getData = req.getParameter("getData");

        Optional<String> getResponse = subscriptionService.getData(ownerId, getData);
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
        Long ownerId = Long.parseLong(req.getParameter("owner_id"));
        Long subscriberId = Long.parseLong(req.getParameter("subscriber_id"));

        if (subscriptionService.subscribe(ownerId, subscriberId)) {
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
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long ownerId = Long.parseLong(req.getParameter("owner_id"));
        Long subscriberId = Long.parseLong(req.getParameter("subscriber_id"));

        if (subscriptionService.unsubscribe(ownerId, subscriberId)) {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setCharacterEncoding(CHAR_SET);
            resp.getWriter().write("Объект удален!");
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setCharacterEncoding(CHAR_SET);
            resp.getWriter().write("Не удалось удалить объект!");
        }
    }
}
