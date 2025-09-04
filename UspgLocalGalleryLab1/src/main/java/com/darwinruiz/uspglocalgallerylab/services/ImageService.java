package com.darwinruiz.uspglocalgallerylab.services;

import com.darwinruiz.uspglocalgallerylab.dto.UploadResult;
import com.darwinruiz.uspglocalgallerylab.repositories.IFileRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ImageService {
    private final IFileRepository repo;
    public ImageService(IFileRepository repo) { this.repo = repo; }
    public String save(String subdir, String fileName, InputStream stream) throws IOException {
        return repo.save(subdir, fileName, stream);
    }
    public UploadResult uploadLocalImages(Collection<Part> parts) throws IOException, ServletException {
        if (parts == null || parts.isEmpty()) {
            return new UploadResult(0, 0, List.of());
        }

        int ok = 0, bad = 0;
        List<String> saved = new ArrayList<>();
        
        for (Part part : parts) {
            try {
                if (part == null || !"file".equals(part.getName()) || part.getSize() == 0) {
                    continue;
                }
                
                String submittedFileName = part.getSubmittedFileName();
                if (submittedFileName == null || submittedFileName.isBlank()) {
                    bad++;
                    continue;
                }
                
                String fileName = com.darwinruiz.uspglocalgallerylab.util.NamePolicy.normalize(submittedFileName);
                
                if (!com.darwinruiz.uspglocalgallerylab.util.ImageValidator.isValid(part, fileName)) {
                    bad++;
                    continue;
                }
                
                String subdir = com.darwinruiz.uspglocalgallerylab.util.NamePolicy.datedSubdir(LocalDate.now());
                
                try (InputStream in = part.getInputStream()) {
                    String savedPath = repo.save(subdir, fileName, in);
                    if (savedPath != null && !savedPath.isBlank()) {
                        saved.add(savedPath);
                        ok++;
                    } else {
                        bad++;
                    }
                }
            } catch (Exception e) {
                bad++;
                e.printStackTrace();
            }
        }
        
        return new UploadResult(ok, bad, saved);
    }
}
