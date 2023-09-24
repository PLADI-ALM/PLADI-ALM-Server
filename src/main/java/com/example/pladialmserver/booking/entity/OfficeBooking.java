package com.example.pladialmserver.booking.entity;

import com.example.pladialmserver.global.entity.BaseEntity;
import com.example.pladialmserver.global.entity.BookingStatus;
import com.example.pladialmserver.user.entity.User;
import com.example.pladialmserver.office.dto.request.OfficeReq;
import com.example.pladialmserver.office.entity.Office;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@Where(clause = "is_enable = true")
public class OfficeBooking extends BaseEntity {

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

    @Size(max = 30)
    private String memo;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20) default 'BOOKED'")
    private BookingStatus status;

    @Builder
    public OfficeBooking(User user, Office office, LocalDate date, LocalTime startTime, LocalTime endTime, String memo, BookingStatus status) {
        this.user = user;
        this.office = office;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.memo = memo;
        this.status = status;
    }

    public static OfficeBooking toDto(User user, Office office, OfficeReq req){
        return OfficeBooking.builder()
                .user(user)
                .office(office)
                .date(req.getDate())
                .startTime(req.getStartTime())
                .endTime(req.getEndTime())
                .memo(req.getMemo())
                .build();
    }

    public void cancelOffice(){
        this.status = BookingStatus.CANCELED;
    }
}
