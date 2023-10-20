package com.example.pladialmserver.resource.entity;

import com.example.pladialmserver.global.entity.BaseEntity;
import com.example.pladialmserver.resource.dto.request.CreateResourceReq;
import com.example.pladialmserver.user.entity.User;
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
@SQLDelete(sql = "UPDATE resource SET is_enable = false, update_at = current_timestamp WHERE resource_id = ?")
public class Resource extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long resourceId;

  @NotNull
  @Size(max = 50)
  private String name;

  @Size(max = 30)
  private String location;

  @NotNull
  @Size(max = 255)
  private String description;

  @Size(max = 255)
  private String imgUrl;

  private Boolean isActive;

  @ManyToOne
  @JoinColumn(nullable = false, name = "user_id")
  private User user;

  @Builder
  public Resource(String name, String description, String imgUrl, String location, Boolean isActive, User user) {
    this.name = name;
    this.description = description;
    this.imgUrl = imgUrl;
    this.location=location;
    this.isActive=isActive;
    this.user=user;
  }

  public static Resource toDto(CreateResourceReq request) {
    return Resource.builder()
            .name(request.getName())
            .description(request.getDescription())
            .imgUrl((request.getImgUrl()==null) ? null : request.getImgUrl())
            .build();
  }

  public void updateResource(CreateResourceReq request) {
    if(!request.getName().equals(name)) name = request.getName();
    if(!request.getDescription().equals(description)) description = request.getDescription();
    if(!request.getImgUrl().equals(imgUrl)) imgUrl = request.getImgUrl();
  }

}
