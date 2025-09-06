package com.darwinruiz.uspglocalgallerylab.controllers;

import com.darwinruiz.uspglocalgallerylab.repositories.LocalFileRepository;
import com.darwinruiz.uspglocalgallerylab.services.ImageService;
import com.darwinruiz.uspglocalgallerylab.services.S3Storage;
import com.darwinruiz.uspglocalgallerylab.util.ImageValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

@WebServlet("/upload")
@MultipartConfig(fileSizeThreshold = 2 * 1024 * 1024, maxFileSize = 5L * 1024 * 1024, maxRequestSize = 30L * 1024 * 1024)
public class UploadServlet extends HttpServlet {
    private ImageService local;
    private S3Storage S3;

    @Override
    public void init() throws ServletException {
        Path base = Path.of(System.getProperty("java.io.tmpdir"),"uspg");
        local = new ImageService(LocalFileRepository.createDefault());
        S3 = new S3Storage();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        Part part = req.getPart("file");
        if (part == null || part.getSize() == 0) {
            resp.sendError(400, "No file");
            return;
        }
        String target = req.getParameter("target");
        if (target == null) target = "local";

        String submitted = part.getSubmittedFileName();
        String fileName = Paths.get(submitted).getFileName().toString();
        String contentType = part.getContentType();
        String prefix = req.getParameter("prefix");
        if (prefix == null) prefix = "";

        if ("s3".equalsIgnoreCase(target)) {
            // Validar antes de subir
            if (!ImageValidator.isValid(part, fileName)) {
                resp.sendError(400, "Archivo no válido: verifique formato, tamaño (≤3MB) y tipo MIME");
                return;
            }
            
            java.time.LocalDateTime now = java.time.LocalDateTime.now();
            String datePath = String.format("imagenes/%04d/%02d/%02d/", now.getYear(), now.getMonthValue(), now.getDayOfMonth());
            String basename = fileName.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
            String clave = datePath + java.util.UUID.randomUUID() + "_" + basename;

            try (InputStream is = part.getInputStream()) {
                S3.put(clave, is, part.getSize(),  contentType);
            }
            resp.sendRedirect(req.getContextPath() + "/upload.jsp?ok=s3&key=" + clave);
            return;
        }
        try (InputStream is = part.getInputStream()) {
            local.save(prefix, fileName, is);
        }
        resp.sendRedirect(req.getContextPath() + "/upload.jsp?ok=local");
    }
}