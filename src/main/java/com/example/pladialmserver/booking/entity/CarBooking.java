package com.example.pladialmserver.booking.entity;

import com.example.pladialmserver.car.entity.Car;
import com.example.pladialmserver.global.entity.BaseEntity;
import com.example.pladialmserver.global.entity.BookingStatus;
import com.example.pladialmserver.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
public class CarBooking extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carBookingId;

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(nullable = false, name = "car_id")
    private Car car;

    @NotNull
    private LocalDateTime startDate;

    @NotNull
    private LocalDateTime endDate;

    private LocalDateTime returnDate;

    @NotNull
    @Size(max = 100)
    private String memo;

    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.WAITING;

    @Builder
    public CarBooking(User user, Car car, LocalDateTime startDate, LocalDateTime endDate, LocalDateTime returnDate,String memo) {
        this.user = user;
        this.car = car;
        this.startDate = startDate;
        this.endDate = endDate;
        this.returnDate=returnDate;
        this.memo = memo;
    }
}
