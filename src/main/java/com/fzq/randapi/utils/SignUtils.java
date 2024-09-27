package com.fzq.randapi.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

import java.util.Map;

public class SignUtils {

    public static String genSign(Map<String, String> hashMap, String secretKey) {
        Digester sha256 = new Digester(DigestAlgorithm.SHA256);
        String content = hashMap.toString() + "." + secretKey;
        return sha256.digestHex(content);

    }
}
