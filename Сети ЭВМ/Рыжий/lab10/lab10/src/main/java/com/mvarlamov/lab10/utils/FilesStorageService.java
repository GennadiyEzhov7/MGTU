package com.mvarlamov.lab10.utils;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface FilesStorageService {
    void init();
    void save(MultipartFile file, String name);
    Resource load(String filename);
    void deleteAll();
    Stream<Path> loadAll();
}