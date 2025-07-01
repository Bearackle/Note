package com.dinhhuan.note.dto;

import lombok.Getter;
import org.springframework.stereotype.Service;

@Getter
@Service
public class VerifyOtpDto {
    String email;
    String otp;
}
