package project.environment.service;

import net.coobird.thumbnailator.Thumbnailator;
import project.environment.dto.UploadDTO;
import project.environment.entity.Upload;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class UploadService {

    @Value("${project.environment.upload.path}")
    private String uploadPath;

    public UploadDTO uploads(MultipartFile file) throws IOException {
        String originalName = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String saveName = uuid + "_" + originalName;
        Path savePath = Paths.get(uploadPath, saveName);

        file.transferTo(savePath);

        String contentType = Files.probeContentType(savePath);
        boolean isImage = contentType != null && contentType.startsWith("image");

        if (isImage) {
            File thumbnailFile = new File(uploadPath, "s_" + saveName);
            Thumbnailator.createThumbnail(savePath.toFile(), thumbnailFile, 200, 200);
        }

        UploadDTO uploadDTO = new UploadDTO();
        uploadDTO.setUuid(uuid);
        uploadDTO.setFileName(saveName);
        uploadDTO.setImg(isImage);

        return uploadDTO;
    }

    public UploadDTO updateUploads(MultipartFile file, String existingFileName) throws IOException {

        String originalName = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String saveName = uuid + "_" + originalName;
        Path savePath = Paths.get(uploadPath, saveName);

        file.transferTo(savePath);

        String contentType = Files.probeContentType(savePath);
        boolean isImage = contentType != null && contentType.startsWith("image");

        if (isImage) {
            File thumbnailFile = new File(uploadPath, "s_" + saveName);
            Thumbnailator.createThumbnail(savePath.toFile(), thumbnailFile, 200, 200);
        }

        UploadDTO uploadDTO = new UploadDTO();
        uploadDTO.setUuid(uuid);
        uploadDTO.setFileName(saveName);
        uploadDTO.setImg(isImage);

        if (existingFileName != null) {
            removeImage(existingFileName);
        }

        return uploadDTO;
    }
    

    public Upload save(Upload upload) {
        return upload;
    }

    public Resource getImage(String fileName) {
        return new FileSystemResource(Paths.get(uploadPath, fileName).toFile());
    }


    public boolean removeImage(String fileName) throws IOException {
        File file = new File(uploadPath, fileName);
        boolean deleted = file.delete();

        if (deleted && Files.probeContentType(file.toPath()).startsWith("image")) {
            File thumbnail = new File(uploadPath, "s_" + fileName);
            thumbnail.delete();
        }

        return deleted;
    }
}