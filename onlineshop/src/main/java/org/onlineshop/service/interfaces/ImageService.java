package org.onlineshop.service.interfaces;

import org.onlineshop.model.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {
    List<String> saveImages(List<MultipartFile> files) throws IOException;

    void deleteImagesFromDirectory(List<Image> images) throws IOException;
}
