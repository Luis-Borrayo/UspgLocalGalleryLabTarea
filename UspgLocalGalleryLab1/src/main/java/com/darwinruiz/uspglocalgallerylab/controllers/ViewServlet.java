package com.darwinruiz.uspglocalgallerylab.controllers;

import com.darwinruiz.uspglocalgallerylab.repositories.LocalFileRepository;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;

@WebServlet("/view")
public class ViewServlet extends HttpServlet {
    private LocalFileRepository repo;

    @Override
    public void init() {
        repo = LocalFileRepository.createDefault();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String rel = req.getParameter("path");
        if (rel == null || rel.isBlank()) {
            resp.sendError(400, "path requerido");
            return;
        }


        if (rel.contains("..")) {
            resp.sendError(400, "path inválido");
            return;
        }


        String lower = rel.toLowerCase();
        if (lower.endsWith(".png")) resp.setContentType("image/png");
        else if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) resp.setContentType("image/jpeg");
        else if (lower.endsWith(".gif")) resp.setContentType("image/gif");
        else if (lower.endsWith(".webp")) resp.setContentType("image/webp");
        else resp.setContentType("application/octet-stream"); // TODO: puedes mejorar con probeContentType

        try (InputStream in = repo.read(rel)) {
            in.transferTo(resp.getOutputStream());
        }
    }
}