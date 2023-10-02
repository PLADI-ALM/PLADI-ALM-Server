package com.example.pladialmserver.booking.entity;

import com.example.pladialmserver.equipment.entity.Category;
import com.example.pladialmserver.global.entity.BookingStatus;
import com.example.pladialmserver.office.entity.Office;
import com.example.pladialmserver.resouce.entity.Resource;
import com.example.pladialmserver.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
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
public class ResourceBooking {

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

  @NotNull
  private LocalDateTime returnDate;

  @NotNull
  @Size(max = 100)
  private String memo;

  @NotNull
  private BookingStatus status = BookingStatus.WAITING;
}
