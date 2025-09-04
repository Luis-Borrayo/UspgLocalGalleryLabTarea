package com.darwinruiz.uspglocalgallerylab.repositories;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

public interface IFileRepository {
    String save(String subdir, String originalFilename, InputStream data) throws IOException;
    InputStream read(String relativePath) throws IOException;
    List<String> listByExtensionsRecursive(String subdir, String... exts) throws IOException;
    boolean delete(String relativePath) throws IOException;
    Path baseDir();
}
