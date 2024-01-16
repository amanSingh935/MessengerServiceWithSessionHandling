package com.rio.MessengerService.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDto {
    String userFrom;
    String userTo;
    String status;
    String message;
    Timestamp timestamp;
}
