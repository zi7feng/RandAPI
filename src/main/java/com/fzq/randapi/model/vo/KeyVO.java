package com.fzq.randapi.model.vo;


import lombok.Data;

import java.io.Serializable;
@Data
public class KeyVO implements Serializable {
    private static final long serialVersionUID = 7461791076297812080L;
    private String secretKey;
}
