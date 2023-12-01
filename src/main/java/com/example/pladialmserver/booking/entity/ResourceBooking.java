package com.example.pladialmserver.booking.entity;

import com.example.pladialmserver.global.entity.BaseEntity;
import com.example.pladialmserver.global.entity.BookingStatus;
import com.example.pladialmserver.product.dto.request.ProductReq;
import com.example.pladialmserver.product.resource.entity.Resource;
import com.example.pladialmserver.user.entity.User;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
@Builder
@AllArgsConstructor
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
  private LocalDateTime startDate;

  @NotNull
  private LocalDateTime endDate;

  private LocalDateTime returnDate;

  @NotNull
  @Size(max = 100)
  private String memo;

  @Size(max = 100)
  private String remark;

  @Builder.Default
  @Enumerated(EnumType.STRING)
  private BookingStatus status = BookingStatus.WAITING;

  public static ResourceBooking toDto(User user, Resource resource, ProductReq productReq) {
    return ResourceBooking.builder()
            .user(user)
            .resource(resource)
            .startDate(productReq.getStartDateTime())
            .endDate(productReq.getEndDateTime())
            .memo(productReq.getMemo())
            .build();
  }

  public void returnBookingResource(String remark, LocalDateTime now) {
    changeBookingStatus(BookingStatus.FINISHED);
    this.returnDate = now;
    this.remark = remark;
    this.endDate = LocalDateTime.of(now.toLocalDate(), LocalTime.of(now.getHour(), 0).plusHours(1));
  }

  public void changeBookingStatus(BookingStatus bookingStatus) {
    status = bookingStatus;
  }

  public boolean checkBookingStatus(BookingStatus bookingStatus) {
    return status.equals(bookingStatus);
  }

  public void startResourceBooking() {
    changeBookingStatus(BookingStatus.USING);
  }
}
