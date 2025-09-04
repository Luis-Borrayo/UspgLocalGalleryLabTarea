package com.darwinruiz.uspglocalgallerylab.util;

import jakarta.servlet.http.Part;

import java.util.Set;

public class ImageValidator {
    public static final long MAX_BYTES = 3L * 1024 * 1024;
    public static final Set<String> ALLOWED_EXT = Set.of(".png",".jpg",".jpeg",".gif",".webp");

    /** TODO-1:
     *  - tamaño <= MAX_BYTES
     *  - contentType empieza con "image/"
     *  - extensión ∈ ALLOWED_EXT (en minúsculas)
     */
    public static boolean isValid(Part part, String fileName) {
        if (part.getSize() > MAX_BYTES) {
            return false;
        }
        
        String contentType = part.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return false;
        }
        
        String lowerFileName = fileName.toLowerCase();
        return ALLOWED_EXT.stream().anyMatch(lowerFileName::endsWith);
    }
}