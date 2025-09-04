package com.darwinruiz.uspglocalgallerylab.dto;

import java.util.List;

public class UploadResult {
    public final int uploaded;
    public final int rejected;
    public final List<String> saved;

    public UploadResult(int uploaded, int rejected, List<String> saved) {
        this.uploaded = uploaded;
        this.rejected = rejected;
        this.saved = saved;
    }
}
