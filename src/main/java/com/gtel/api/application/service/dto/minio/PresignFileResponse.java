package com.gtel.api.application.service.dto.minio;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PresignFileResponse {
    private String presignUrl;
}
