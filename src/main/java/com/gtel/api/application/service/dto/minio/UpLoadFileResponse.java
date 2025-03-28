package com.gtel.api.application.service.dto.minio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpLoadFileResponse {
    private String url;
    private String fileName;
    private String mode;
}
