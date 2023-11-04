package com.example.pladialmserver.booking.dto.request;

import com.example.pladialmserver.booking.entity.CarBooking;
import com.example.pladialmserver.booking.entity.ResourceBooking;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SendEmailReq {
    private String text;
    private String productName;
    private String office_product;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;


    public static SendEmailReq toDto(ResourceBooking resourceBooking, String text, String office_product) {
        return SendEmailReq.builder()
                .text(text)
                .productName(resourceBooking.getResource().getName())
                .office_product(office_product)
                .startDateTime(resourceBooking.getStartDate())
                .endDateTime(resourceBooking.getEndDate())
                .build();
    }

    public static SendEmailReq toDto(CarBooking carBooking, String text, String office_product) {
        return SendEmailReq.builder()
                .text(text)
                .productName(carBooking.getCar().getName())
                .office_product(office_product)
                .startDateTime(carBooking.getStartDate())
                .endDateTime(carBooking.getEndDate())
                .build();
    }
}
