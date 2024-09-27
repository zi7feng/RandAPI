package com.fzq.randapi.model.dto.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class KeyGenerateDTO implements Serializable {
    private static final long serialVersionUID = 240293699497915171L;
    private String accessKey;
}
