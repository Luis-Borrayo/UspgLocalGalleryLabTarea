package com.darwinruiz.uspglocalgallerylab.controllers;

import com.darwinruiz.uspglocalgallerylab.repositories.LocalFileRepository;
import com.darwinruiz.uspglocalgallerylab.services.S3Storage;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/list")
public class ListServlet extends HttpServlet {
    private LocalFileRepository repo;
    private S3Storage s3;

    @Override
    public void init() {
        repo = LocalFileRepository.createDefault();
        s3 = new S3Storage();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        String target = req.getParameter("target");
        if (target == null) target = "local";

        int page = 1, size = 12;
        try {
            if (req.getParameter("page") != null)
                page = Math.max(1, Integer.parseInt(req.getParameter("page")));
            if (req.getParameter("size") != null)
                size = Math.max(1, Integer.parseInt(req.getParameter("size")));
        } catch (NumberFormatException ignored) {}

        if ("s3".equalsIgnoreCase(target)) {
            String prefix = "imagenes/";
            List<String> keys = s3.listKeysByPrefixAndExt(prefix, ".png", ".jpg", ".jpeg", ".gif", ".webp");
            List<String> urls = keys.stream()
                    .map(k -> s3.presignedGetUrl(k, 3600))
                    .collect(Collectors.toList());

            int total = urls.size();
            int fromIndex = (page - 1) * size;
            int toIndex = Math.min(fromIndex + size, total);
            if (fromIndex >= total) {
                fromIndex = Math.max(0, total - size);
                toIndex = total;
                page = fromIndex / size + 1;
            }
            List<String> pageItems = urls.subList(fromIndex, toIndex);
            int totalPages = (int) Math.ceil((double) total / size);
            if (totalPages == 0) totalPages = 1;
            req.setAttribute("images", pageItems);
            req.setAttribute("source", "s3");
            req.setAttribute("page", page);
            req.setAttribute("size", size);
            req.setAttribute("total", total);
            req.setAttribute("totalPages", totalPages);
            req.getRequestDispatcher("/gallery.jsp").forward(req, resp);
            return;
        }

        List<String> all = repo.listByExtensionsRecursive(
                "imagenes", ".png", ".jpg", ".jpeg", ".gif", ".webp");

        int total = all.size();
        int fromIndex = (page - 1) * size;
        int toIndex = Math.min(fromIndex + size, total);
        if (fromIndex >= total) {
            fromIndex = Math.max(0, total - size);
            toIndex = total;
            page = fromIndex / size + 1;
        }
        List<String> pageItems = all.subList(fromIndex, toIndex);
        int totalPages = (int) Math.ceil((double) total / size);
        if (totalPages == 0) totalPages = 1;

        req.setAttribute("images", pageItems);
        req.setAttribute("source", "local");
        req.setAttribute("page", page);
        req.setAttribute("size", size);
        req.setAttribute("total", total);
        req.setAttribute("totalPages", totalPages);
        req.getRequestDispatcher("/gallery.jsp").forward(req, resp);
    }
}
