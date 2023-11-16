package com.example.pladialmserver.product.car.entity;

import com.example.pladialmserver.global.entity.BaseEntity;
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

    @NotNull
    @Size(max = 255)
    private String description;

    @Size(max = 255)
    private String imgKey;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
    private Boolean isActive = true;

    @Builder
    public Car(String name, String manufacturer, String description, String imgKey, String location, Boolean isActive, User user) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.description = description;
        this.imgKey = imgKey;
        this.location = location;
        this.isActive = isActive;
        this.user = user;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void activateResource() {
        isActive = !isActive;
    }
}
