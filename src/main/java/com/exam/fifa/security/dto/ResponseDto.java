package com.exam.fifa.security.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ResponseDto {
    String code;
    String message;
    HttpStatus status;
}
