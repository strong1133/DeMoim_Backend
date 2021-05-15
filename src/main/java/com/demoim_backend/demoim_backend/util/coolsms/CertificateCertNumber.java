package com.demoim_backend.demoim_backend.util.coolsms;


import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
public class CertificateCertNumber {

    private int certNumLength = 6;

    public String excuteGenerate() {
        Random random = new Random(System.currentTimeMillis());

        int range = (int) Math.pow(10, certNumLength);
        int trim = (int) Math.pow(10, certNumLength - 1);
        int result = random.nextInt(range) + trim;

        if (result > range) {
            result = result - trim;
        }

        return String.valueOf(result);
    }

}