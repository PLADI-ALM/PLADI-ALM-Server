package com.example.pladialmserver.office.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BookingStateRes {
    private List<BookedTimeRes> bookedTimes;

    public static BookingStateRes toDto(List<BookedTimeRes> bookedTimes){
        return BookingStateRes.builder()
                .bookedTimes(bookedTimes)
                .build();
    }
}
