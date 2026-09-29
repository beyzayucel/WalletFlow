package com.walletflow.auth.service.impl;

import com.walletflow.auth.service.OtpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpServiceImpl implements OtpService {

    private final SecureRandom secureRandom = new SecureRandom();

    public void generateOtp(String email) {
//        validateEmailInput(email);
//        log.info("OTP generation request received: email={}", MaskType.EMAIL.mask(email));
//
//        checkCooldown(email);
//        clearAllOtpKeys(email);
//
//        String otpCode = generateOtpCode();
//        saveOtpToRedis(email, otpCode);
//
//        try {
//            sendOtpNotification(email, otpCode);
//        } catch (NotificationPublishException e) {
//            log.error("Failed to send OTP notification: email={}", MaskType.EMAIL.mask(email));
//            invalidateOtp(email);
//            throw new OtpException(OtpErrorType.OTP_SEND_FAILED, e);
//        }

//        log.info("OTP generated and dispatched: email={}", MaskType.EMAIL.mask(email));
    }

    @Override
    public String generateOtpCode() {
        int otpInt = 100000 + secureRandom.nextInt(900000);
        return String.valueOf(otpInt);
    }

}
