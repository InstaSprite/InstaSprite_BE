package org.olaz.instasprite_be.infra.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.olaz.instasprite_be.global.error.exception.FileConvertFailException;
import org.olaz.instasprite_be.global.util.ImageUtil;
import org.olaz.instasprite_be.global.vo.Image;

@Slf4j
@RequiredArgsConstructor
@Component
public class ImageUploader {

	@Value("${app.upload.base-dir}")
	private String baseUploadDir;
	
	@Value("${server.base-url}")
	private String serverBaseUrl;

	public Image uploadImage(MultipartFile multipartFile, String dirName) {
		final Image image = ImageUtil.convertMultipartToImage(multipartFile);
		final String filename = convertToFilename(dirName, image);
		final String url = saveToLocal(multipartFile, filename);
		image.setUrl(url);
		return image;
	}

	public void deleteImage(Image image, String dirName) {
		if (image.getImageUUID().equals("base-UUID")) {
			return;
		}
		final String filename = convertToFilename(dirName, image);
		deleteLocal(filename);
	}

	private String convertToFilename(String dirName, Image image) {
		return dirName + "/" + image.getImageUUID() + "_" + image.getImageName() + "." + image.getImageType();
	}

	private String saveToLocal(MultipartFile file, String filename) {
		try {
			final Path dir = Paths.get(baseUploadDir, filename).getParent();
			if (dir != null && !Files.exists(dir)) {
				Files.createDirectories(dir);
			}
			final Path target = Paths.get(baseUploadDir, filename);
			Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
			log.info("Saved image locally at {}", target);
			return filename;
		} catch (IOException e) {
			throw new FileConvertFailException();
		}
	}

	private void deleteLocal(String filename) {
		try {
			final Path target = Paths.get(baseUploadDir, filename);
			Files.deleteIfExists(target);
			log.info("Deleted local image at {}", target);
		} catch (IOException e) {
			log.warn("Failed to delete local image {}", filename, e);
		}
	}
}


