package com.example.pladialmserver.product.car.entity;

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
@SQLDelete(sql = "UPDATE car SET is_enable = false, update_at = current_timestamp WHERE car_id = ?")
public class Car extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carId;

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

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

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
    private Boolean isActive = true;

    @Builder
    public Car(String name, String manufacturer, String description, String imgKey, String location, User user) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.description = description;
        this.imgKey = imgKey;
        this.location = location;
        this.user = user;
    }

    public static Car toDto(CreateProductReq request, User responsibility) {
        return Car.builder()
                .name(request.getName())
                .manufacturer(request.getManufacturer() == null ? null : request.getManufacturer())
                .location(request.getLocation() == null ? null : request.getLocation())
                .description(request.getDescription() == null ? null : request.getDescription())
                .imgKey((request.getImgKey() == null) ? null : request.getImgKey())
                .user(responsibility)
                .build();
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void activateResource() {
        isActive = !isActive;
    }

    public void updateCar(CreateProductReq request, User responsibility) {
        if (!name.equals(request.getName())) name = request.getName();
        if (!manufacturer.equals(request.getManufacturer())) manufacturer = request.getManufacturer();
        if (!location.equals(request.getLocation())) location = request.getLocation();
        if (!description.equals(request.getDescription())) description = request.getDescription();
        if (!AwsS3ImageUrlUtil.toUrl(imgKey).equals(request.getImgKey())) imgKey = request.getImgKey();
        if (!user.equals(responsibility)) user = responsibility;
    }
}
