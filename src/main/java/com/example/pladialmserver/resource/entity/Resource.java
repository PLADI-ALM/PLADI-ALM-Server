package com.example.pladialmserver.resource.entity;

import com.example.pladialmserver.global.entity.BaseEntity;
import com.example.pladialmserver.resource.dto.request.CreateResourceReq;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "is_enable = true")
@SQLDelete(sql = "UPDATE resource SET is_enable = false, update_at = current_timestamp WHERE resource_id = ?")
public class Resource extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long resourceId;

  @NotNull
  @Size(max = 50)
  private String name;

  @ManyToOne
  @JoinColumn(nullable = false, name = "resource_category_id")
  private ResourceCategory resourceCategory;

  @NotNull
  @Size(max = 255)
  private String description;

  @Size(max = 255)
  private String imgUrl;

  @Builder
  public Resource(String name, ResourceCategory resourceCategory, String description, String imgUrl) {
    this.name = name;
    this.resourceCategory = resourceCategory;
    this.description = description;
    this.imgUrl = imgUrl;
  }

  public static Resource toDto(CreateResourceReq request, ResourceCategory category) {
    return Resource.builder()
            .name(request.getName())
            .resourceCategory(category)
            .description(request.getDescription())
            .imgUrl((request.getImgUrl()==null) ? null : request.getImgUrl())
            .build();
  }
}
