package com.example.pladialmserver.product.resource.entity;

import com.example.pladialmserver.global.entity.BaseEntity;
import com.example.pladialmserver.global.utils.AwsS3ImageUrlUtil;
import com.example.pladialmserver.product.dto.request.CreateProductReq;
import com.example.pladialmserver.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE resource SET is_enable = false, update_at = current_timestamp WHERE resource_id = ?")
public class Resource extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long resourceId;

  @NotNull
  @Size(max = 50)
  private String name;

  @Size(max = 30)
  private String manufacturer;

  @Size(max = 30)
  private String location;

  @NotNull
  @Size(max = 255)
  private String description;

  @Size(max = 255)
  private String imgKey;

  @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
  private Boolean isActive = true;

  @ManyToOne
  @JoinColumn(nullable = false, name = "user_id")
  private User user;

  @Builder
  public Resource(String name, String manufacturer, String description, String imgKey, String location, User user,Boolean isActive) {
    this.name = name;
    this.manufacturer = manufacturer;
    this.description = description;
    this.imgKey = imgKey;
    this.location=location;
    this.user=user;
    this.isActive=isActive;
  }

  public Resource(Long resourceId, String name, String manufacturer, String location, String description, String imgKey, User user) {
    this.resourceId = resourceId;
    this.name = name;
    this.manufacturer = manufacturer;
    this.location = location;
    this.description = description;
    this.imgKey = imgKey;
    this.user = user;
  }

  public static Resource toDto(CreateProductReq request, User responsibility) {
    return Resource.builder()
            .name(request.getName())
            .location(request.getLocation())
            .description(request.getDescription())
            .imgKey((request.getImgKey() == null) ? null : request.getImgKey())
            .user(responsibility)
            .build();
  }

  public void updateResource(CreateProductReq request, User responsibility) {
    if (!request.getName().equals(name)) name = request.getName();
    if (!request.getManufacturer().equals(manufacturer)) manufacturer = request.getManufacturer();
    if (!request.getLocation().equals(location)) location = request.getLocation();
    if (!request.getDescription().equals(description)) description = request.getDescription();
    if (!request.getImgKey().equals(AwsS3ImageUrlUtil.toUrl(imgKey))) imgKey = request.getImgKey();
    if (!responsibility.equals(user)) user = responsibility;
  }

  // 활성화/비활성화
  public void activateResource() {
    isActive = !isActive;
  }

  public void setLocation(String location) {
    this.location = location;
  }
}
