package com.spai.service;

import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

@Service
public interface ImageService {

    String imageToText(String filePath);

    String TextToImage(String text);

}
