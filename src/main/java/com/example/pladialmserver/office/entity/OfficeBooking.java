package com.example.pladialmserver.office.entity;

import com.example.pladialmserver.global.entity.BookingStatus;
import com.example.pladialmserver.global.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class OfficeBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long officeBookingId;

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(nullable = false, name = "office_id")
    private Office office;

    @NotNull
    private LocalDate date;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;

    @NotNull
    @Size(max = 30)
    private String memo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20) default 'BOOKED'")
    private BookingStatus status;

}
