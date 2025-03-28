package com.gtel.api.interfaces.models.request.minio;

import lombok.Data;

import java.util.List;

@Data
public class UploadFileRequest {
    private List<String> fileNames; //file name after saved
    private String contentType; //Docs, Images, v.v
    private String uploadFolder;
}
