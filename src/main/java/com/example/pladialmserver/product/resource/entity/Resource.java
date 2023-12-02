package com.example.pladialmserver.product.resource.entity;

import com.example.pladialmserver.global.entity.BaseEntity;
import com.example.pladialmserver.global.utils.AwsS3ImageUrlUtil;
import com.example.pladialmserver.product.dto.request.CreateProductReq;
import com.example.pladialmserver.user.entity.User;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE resource SET is_enable = false, update_at = current_timestamp WHERE resource_id = ?")
@Builder
@AllArgsConstructor
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

  @Size(max = 255)
  private String description;

  @Size(max = 255)
  private String imgKey;

  @Builder.Default
  @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
  private Boolean isActive = true;

  @ManyToOne
  @JoinColumn(nullable = false, name = "user_id")
  private User user;

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
            .manufacturer(request.getManufacturer() == null ? null : request.getDescription())
            .location(request.getLocation() == null ? null : request.getDescription())
            .description(request.getDescription() == null ? null : request.getDescription())
            .imgKey((request.getImgKey() == null) ? null : request.getImgKey())
            .user(responsibility)
            .build();
  }

  public void updateResource(CreateProductReq request, User responsibility) {
    if (!Objects.equals(name, request.getName())) name = request.getName();
    if (!Objects.equals(manufacturer, request.getManufacturer())) manufacturer = request.getManufacturer();
    if (!Objects.equals(location, request.getLocation())) location = request.getLocation();
    if (!Objects.equals(description, request.getDescription())) description = request.getDescription();
    if (!Objects.equals(AwsS3ImageUrlUtil.toUrl(imgKey), request.getImgKey())) imgKey = request.getImgKey();
    if (!Objects.equals(user, responsibility)) user = responsibility;
  }

  // 활성화/비활성화
  public void activateResource() {
    isActive = !isActive;
  }

  public void setLocation(String location) {
    this.location = location;
  }
}
