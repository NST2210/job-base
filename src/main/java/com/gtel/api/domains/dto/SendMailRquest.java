package com.gtel.api.domains.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendMailRquest implements Serializable {
    private String appRequestSend; // email username
    private List<String> recipients; // dia chi nguoi nhan
    private String emailType; // loai mail
    private Map<String, Object> params;
}
