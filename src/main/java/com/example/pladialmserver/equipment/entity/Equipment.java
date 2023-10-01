package com.example.pladialmserver.equipment.entity;

import com.example.pladialmserver.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "is_enable = true")
public class Equipment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long equipmentId;

  @NotNull
  @Size(max = 30)
  private String name;

  @NotNull
  private Category category;

  @NotNull
  private Integer price;

  @NotNull
  private Integer quantity;

  @NotNull
  private LocalDate purchaseDate;

  @ManyToOne
  @JoinColumn(nullable = false, name = "user_id")
  private User user;


}
