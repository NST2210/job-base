package com.gtel.api.interfaces.restful;

import com.gtel.api.application.service.MinioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/minio")
@RequiredArgsConstructor
public class MinioController {
    private final MinioService minioService;

    @GetMapping("/presigned-url")
    public String getPresignedUploadUrl(@RequestParam String fileName) {
        return minioService.generatePresignedUploadUrl(fileName);
    }

    /**
     * API lấy Presigned URL để xem ảnh
     */
    @GetMapping("/view-url")
    public String getPresignedViewUrl(@RequestParam String fileName) {
        return minioService.generatePresignedViewUrl(fileName);
    }
}
