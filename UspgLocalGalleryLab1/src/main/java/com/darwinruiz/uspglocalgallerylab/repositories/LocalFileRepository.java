package com.darwinruiz.uspglocalgallerylab.repositories;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class LocalFileRepository implements IFileRepository {
    private final Path baseDir;

    public LocalFileRepository(Path baseDir) {
        this.baseDir = baseDir;
    }

    public static LocalFileRepository createDefault() {
        Path base = Path.of(System.getProperty("java.io.tmpdir"), "uspg-local-lab-v2");
        try {
            Files.createDirectories(base);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo preparar baseDir: " + base, e);
        }
        return new LocalFileRepository(base);
    }

    @Override
    public String save(String subdir, String originalFilename, InputStream data) throws IOException {
        String clean = Paths.get(originalFilename).getFileName().toString();
        Path dir = (subdir == null || subdir.isBlank()) ? baseDir : baseDir.resolve(subdir);
        Files.createDirectories(dir);
        String safe = UUID.randomUUID() + "_" + clean;
        Path target = dir.resolve(safe);
        try (OutputStream out = Files.newOutputStream(target)) {
            data.transferTo(out);
        }
        return baseDir.relativize(target).toString().replace('\\', '/'); // p.ej. "imagenes/2025/08/29/uuid.png"
    }

    @Override
    public InputStream read(String relativePath) throws IOException {
        return Files.newInputStream(baseDir.resolve(relativePath));
    }

    @Override
    public List<String> listByExtensionsRecursive(String subdir, String... exts) throws IOException {
        Path root = (subdir == null || subdir.isBlank()) ? baseDir : baseDir.resolve(subdir);
        if (!Files.exists(root)) return List.of();
        Set<String> allowed = Arrays.stream(exts).map(String::toLowerCase).collect(Collectors.toSet());
        try (var s = Files.walk(root)) {
            return s.filter(Files::isRegularFile)
                    .map(p -> baseDir.relativize(p).toString().replace('\\', '/'))
                    .filter(name -> {
                        String lower = name.toLowerCase();
                        for (String e : allowed) if (lower.endsWith(e)) return true;
                        return false;
                    })
                    .sorted().collect(Collectors.toList());
        }
    }

    @Override
    public boolean delete(String relativePath) throws IOException {
        return Files.deleteIfExists(baseDir.resolve(relativePath));
    }

    @Override
    public Path baseDir() {
        return baseDir;
    }
}