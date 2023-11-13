package com.example.pladialmserver.user.entity;

import com.example.pladialmserver.global.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
public class Affiliation extends BaseEntity {
  // 소속 (플래디 / 스튜디오아이 / 피디룸)
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long affiliationId;

  @NotNull
  @Size(max = 30)
  private String name;
}
