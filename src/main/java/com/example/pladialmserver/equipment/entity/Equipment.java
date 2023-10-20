package com.example.pladialmserver.equipment.entity;

import com.example.pladialmserver.global.entity.BaseEntity;
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

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
public class Equipment extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long equipmentId;

  @ManyToOne
  @JoinColumn(nullable = false, name = "user_id")
  private User user;

  @NotNull
  @Size(max = 30)
  private String name;

  @Size(max = 30)
  private String location;

  @Size(max = 30)
  private String description;

  private String imgKey;

  @NotNull
  private Integer quantity;
}

