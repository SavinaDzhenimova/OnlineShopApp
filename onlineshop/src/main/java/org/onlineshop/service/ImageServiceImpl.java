package org.onlineshop.service;

import org.onlineshop.model.entity.Image;
import org.onlineshop.repository.ImageRepository;
import org.onlineshop.service.interfaces.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {

    private static final String UPLOAD_DIR = "uploads/products/";
    private final ImageRepository imageRepository;

    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public List<String> saveImages(List<MultipartFile> files) throws IOException {
        List<String> imageUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path path = Paths.get(UPLOAD_DIR + fileName);

                Files.createDirectories(path.getParent());
                Files.write(path, file.getBytes());

                imageUrls.add("/uploads/products/" + fileName);
            }
        }

        return imageUrls;
    }

    @Override
    public void deleteImagesFromDirectory(List<Image> images) throws IOException {
        for (Image image : images) {
            String imageUrl = image.getImageUrl();

            if (imageUrl != null && !imageUrl.isEmpty()) {
                String fileName = imageUrl.replace("/uploads/products/", "");
                Path path = Paths.get(UPLOAD_DIR + fileName);

                if (Files.exists(path)) {
                    Files.delete(path);
                    System.out.println("Deleted image: " + path.toString());
                } else {
                    System.out.println("Image not found: " + path.toString());
                }
            }
        }
    }
}
