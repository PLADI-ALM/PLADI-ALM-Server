package com.example.pladialmserver.resouce.entity;

import com.example.pladialmserver.equipment.entity.Category;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "is_enable = true")
public class Resource {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long resourceId;

  @NotNull
  @Size(max = 50)
  private String name;

  @NotNull
  @Enumerated(EnumType.STRING)
  private Category category;

  @NotNull
  @Size(max = 255)
  private String description;

  @Size(max = 255)
  private String imgUrl;
}
