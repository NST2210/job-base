package com.gtel.api.application.service;

import com.gtel.api.application.service.dto.minio.PresignFileResponse;
import com.gtel.api.application.service.dto.minio.UpLoadFileResponse;
import com.gtel.api.domains.exceptions.ApplicationException;
import com.gtel.api.interfaces.models.request.minio.UploadFileRequest;
import com.gtel.api.utils.ERROR_CODE;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import io.minio.http.Method;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RefreshScope
@RequiredArgsConstructor
@Log4j2
public class MinioService {

    private static final String RESPONSE_CONTENT_TYPE = "response-content-type";

    private MinioClient minioClient;

    //    private final S3Presigner presigner;

    @Value("${minio.expiry}")
    Integer expiry;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @Value("${minio.url}")
    String endpoint;

    @Value("${minio.access-key}")
    String accessKey;

    @Value("${minio.secret-key}")
    String secretKey;

    @Value("${minio.region}")
    String region;

    @PostConstruct
    public MinioClient getMinioClient() {
        try {
            if (minioClient == null) {
                minioClient = MinioClient.builder()
                        .endpoint(endpoint)
                        .credentials(accessKey, secretKey)
                        .region(region)
                        .build();
            }
            return minioClient;
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    public List<UpLoadFileResponse> generationFileNameUpload(UploadFileRequest request) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        Map<String, String> requestParams = new HashMap<>();
        requestParams.put(RESPONSE_CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        List<UpLoadFileResponse> responses = new ArrayList<>();
        if (request.getFileNames().isEmpty()) {
            throw new ApplicationException(ERROR_CODE.BAD_REQUEST);
        }

        for (String fileName : request.getFileNames()) {
            fileName = this.normalizeFileName(fileName, request.getUploadFolder());
            String url = this.minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.PUT)
                            .bucket(bucketName)
                            .object(fileName)
                            .expiry(expiry, TimeUnit.SECONDS)
                            .extraQueryParams(requestParams)
                            .build()
            );
            UpLoadFileResponse response = UpLoadFileResponse.builder()
                    .url(url)
                    .fileName(fileName)
                    .build();
            responses.add(response);
        }
        return responses;
    }

    public PresignFileResponse getPresignedUrl(String fileName) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String mineType = Files.probeContentType(Paths.get(fileName));
        if (mineType == null) {
            mineType = "application/octet-stream";
        }
        String presignedUrl = minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .method(Method.GET)
                        .expiry(expiry, TimeUnit.SECONDS)
                        .extraQueryParams(Map.of(
                                "response-content-disposition", "inline",
                                "response-content-type", mineType
                        ))
                        .build()
        );
        return new PresignFileResponse(presignedUrl);
    }

    private String normalizeFileName(String fileName, String uploadFolder) {
        LocalDateTime localDateTime = LocalDateTime.now();
        String originalFileName = fileName.substring(0, fileName.lastIndexOf("."))
                .replaceAll("\\s", "") + "_" + localDateTime.getNano();
        String extension = fileName.substring(fileName.lastIndexOf(".") );
        fileName = originalFileName + extension;
        fileName = uploadFolder + "/" + String.format("%04d/%02d/%s", localDateTime.getYear(), localDateTime.getMonthValue(), fileName);
        return fileName;
    }


//    public MinioService(
//            @Value("${minio.url}") String endpoint,
//            @Value("${minio.access-key}") String accessKey,
//            @Value("${minio.secret-key}") String secretKey,
//            @Value("${minio.region}") String region) {
//
//        this.presigner = S3Presigner.builder()
//                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
//                .region(Region.of(region))
//                .endpointOverride(java.net.URI.create(endpoint))
//                .build();
//    }

    /**
     * Tạo Presigned URL để upload ảnh
     */
    public String generatePresignedUploadUrl(String fileName) {
//        PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(req -> req
//                .signatureDuration(Duration.ofSeconds(expiry)) // URL hết hạn sau 10 phút
//                .putObjectRequest(por -> por.bucket(bucketName).key(fileName))
//        );
//
//        return presignedRequest.url().toString();
        return null;
    }

    /**
     * Tạo Presigned URL để xem ảnh
     */
    public String generatePresignedViewUrl(String fileName) {
//        PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(req -> req
//                .signatureDuration(Duration.ofMinutes(10)) // URL hết hạn sau 10 phút
//                .getObjectRequest(gor -> gor.bucket(bucketName).key(fileName))
//        );
//
//        return presignedRequest.url().toString();
        return null;
    }


}
