package org.olaz.instasprite_be.global.util;


import org.apache.commons.io.FilenameUtils;
import org.olaz.instasprite_be.global.vo.Image;
import org.olaz.instasprite_be.global.vo.ImageType;

/**
 * Utility class for image processing operations
 * Handles conversion of multipart files to Image value objects
 */
public class ImageUtil {

    /**
     * Converts a MultipartFile to an Image value object
     * @param file The multipart file to convert
     * @return Image value object with generated UUID
     * @throws NotSupportedImageTypeException if the file type is not supported
     */
    public static Image convertMultipartToImage(org.springframework.web.multipart.MultipartFile file) {
        final String originalName = file.getOriginalFilename();
        final String name = FilenameUtils.getBaseName(originalName);
        final String type = FilenameUtils.getExtension(originalName).toUpperCase();

        try {
           ImageType.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new org.olaz.instasprite_be.global.error.exception.NotSupportedImageTypeException();
        }

        return Image.builder()
                .imageType(ImageType.valueOf(type))
                .imageName(name)
                .imageUUID(java.util.UUID.randomUUID().toString())
                .build();
    }

    /**
     * Returns a default base image for users without a profile picture
     * @return Base image with predefined URL
     */
//    public static Image getBaseImage() {
//        return Image.builder()
//                .imageName("base")
//                .imageType(ImageType.PNG)
//                .imageUrl("https://instasprite-s3.s3.amazonaws.com/member/base-UUID_base.PNG.png")
//                .imageUUID("base-UUID")
//                .build();
//    }
}

