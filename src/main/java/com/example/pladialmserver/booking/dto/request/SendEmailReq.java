package com.example.pladialmserver.booking.dto.request;

import com.example.pladialmserver.booking.entity.CarBooking;
import com.example.pladialmserver.booking.entity.OfficeBooking;
import com.example.pladialmserver.booking.entity.ResourceBooking;
import com.example.pladialmserver.global.utils.DateTimeUtil;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

import static com.example.pladialmserver.global.Constants.EmailNotification.OFFICE;
import static com.example.pladialmserver.global.Constants.EmailNotification.PRODUCT;

@Getter
@Builder
public class SendEmailReq {
    private String text;
    private String reservatorName;
    private String productName;
    private String office_product;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;


    public static SendEmailReq toDto(ResourceBooking resourceBooking, String text) {
        return SendEmailReq.builder()
                .text(text)
                .reservatorName(resourceBooking.getUser().getName())
                .productName(resourceBooking.getResource().getName())
                .office_product(PRODUCT)
                .startDateTime(resourceBooking.getStartDate())
                .endDateTime(resourceBooking.getEndDate())
                .build();
    }

    public static SendEmailReq toDto(CarBooking carBooking, String text) {
        return SendEmailReq.builder()
                .text(text)
                .reservatorName(carBooking.getUser().getName())
                .productName(carBooking.getCar().getName())
                .office_product(PRODUCT)
                .startDateTime(carBooking.getStartDate())
                .endDateTime(carBooking.getEndDate())
                .build();
    }

    public static SendEmailReq toDto(OfficeBooking officeBooking, String text) {
        return SendEmailReq.builder()
                .text(text)
                .reservatorName(officeBooking.getUser().getName())
                .productName(officeBooking.getOffice().getName())
                .office_product(OFFICE)
                .startDateTime(DateTimeUtil.localDateAndTimeToLocalDateTime(officeBooking.getDate(), officeBooking.getStartTime()))
                .endDateTime(DateTimeUtil.localDateAndTimeToLocalDateTime(officeBooking.getDate(), officeBooking.getEndTime()))
                .build();
    }
}
