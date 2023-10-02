package com.example.pladialmserver.booking.entity;

import com.example.pladialmserver.global.entity.BaseEntity;
import com.example.pladialmserver.global.entity.BookingStatus;
import com.example.pladialmserver.resouce.entity.Resource;
import com.example.pladialmserver.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
@Where(clause = "is_enable = true")
public class ResourceBooking extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long resourceBookingId;

  @ManyToOne
  @JoinColumn(nullable = false, name = "user_id")
  private User user;

  @ManyToOne
  @JoinColumn(nullable = false, name = "resource_id")
  private Resource resource;

  @NotNull
  private LocalDate startDate;

  @NotNull
  private LocalDate endDate;

  private LocalDateTime returnDate;

  @NotNull
  @Size(max = 100)
  private String memo;

  @Enumerated(EnumType.STRING)
  private BookingStatus status = BookingStatus.WAITING;

  public void cancelBookingResource() {
    status = BookingStatus.CANCELED;
  }
}
