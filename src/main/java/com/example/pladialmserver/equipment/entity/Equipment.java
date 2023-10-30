package com.example.pladialmserver.equipment.entity;

import com.example.pladialmserver.equipment.dto.request.RegisterEquipmentReq;
import com.example.pladialmserver.equipment.dto.request.UpdateEquipmentReq;
import com.example.pladialmserver.global.entity.BaseEntity;
import com.example.pladialmserver.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
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
@SQLDelete(sql = "UPDATE equipment SET is_enable = false, update_at = current_timestamp WHERE equipment_id = ?")
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

  @ManyToOne
  @JoinColumn(nullable = false, name = "equipment_category_Id")
  private EquipmentCategory equipmentCategory;

  @NotNull
  private Integer quantity;

  @Builder
  public Equipment(User user, String name, String location, String description, String imgKey, Integer quantity, EquipmentCategory category) {
    this.user = user;
    this.name = name;
    this.location = location;
    this.description = description;
    this.imgKey = imgKey;
    this.quantity = quantity;
    this.equipmentCategory = category;
  }

  public static Equipment toEntity(RegisterEquipmentReq registerEquipmentReq, EquipmentCategory category, User user) {
    return Equipment.builder()
            .name(registerEquipmentReq.getName())
            .location(registerEquipmentReq.getLocation())
            .quantity(registerEquipmentReq.getQuantity())
            .description(registerEquipmentReq.getDescription())
            .imgKey(registerEquipmentReq.getImgKey())
            .category(category)
            .user(user)
            .build();
  }

  public void toUpdateInfo(UpdateEquipmentReq updateEquipmentReq, User updateKeeper, EquipmentCategory updateCategory) {
    this.name = updateEquipmentReq.getName();
    this.description = updateEquipmentReq.getDescription();
    this.quantity = updateEquipmentReq.getQuantity();
    this.imgKey = updateEquipmentReq.getImgKey();
    this.location = updateEquipmentReq.getLocation();
    this.equipmentCategory = updateCategory;
    this.user = updateKeeper;
  }

  public void delete() {
    this.setIsEnable(false);
  }
}

