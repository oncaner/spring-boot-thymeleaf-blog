package caner.blog.common.util;

import caner.blog.exception.ImageExtensionException;

import java.util.Arrays;
import java.util.List;

public class ImageValidatorUtil {

    public static void imageExtensionValidator(String fileName) {
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);

        List<String> supportedExtensions = Arrays.asList("jpg", "jpeg", "png");

        if (!supportedExtensions.contains(fileExtension.toLowerCase())) {
            throw new ImageExtensionException("Lütfen geçerli bir resim seçiniz.");
        }
    }
}
