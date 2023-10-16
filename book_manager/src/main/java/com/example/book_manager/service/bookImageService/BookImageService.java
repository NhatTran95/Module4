package com.example.book_manager.service.bookImageService;

import com.cloudinary.Cloudinary;
import com.example.book_manager.domain.BookImage;
import com.example.book_manager.repository.IBookImageRepossitory;
import com.example.book_manager.util.UploadUtils;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@AllArgsConstructor
@Transactional
public class BookImageService {
    private final Cloudinary cloudinary;

    private final IBookImageRepossitory fileRepository;

    private final UploadUtils uploadUtils;
    public BookImage saveAvatar(MultipartFile avatar) throws IOException {
        var file = new BookImage();
        fileRepository.save(file);

        var uploadResult = cloudinary.uploader().upload(avatar.getBytes(), uploadUtils.buildImageUpLoadParams(file));

        String fileUrl = (String) uploadResult.get("secure_url");
        String fileFormat = (String) uploadResult.get("format");

        file.setFileName(file.getId() + "." + fileFormat);
        file.setFileUrl(fileUrl);
        file.setFileFolder(UploadUtils.IMAGE_UPLOAD_FOLDER);
        file.setCloudId(file.getFileFolder() + "/" + file.getId());

        fileRepository.save(file);
        return file;
    }

    public void delete(String fileUrl) {
        fileRepository.deleteBookImageByFileUrl(fileUrl);
    }

    public void deleteById(String id) {
        fileRepository.deleteById(id);
    }
}
