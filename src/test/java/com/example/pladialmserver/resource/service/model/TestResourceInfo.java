package com.example.pladialmserver.resource.service.model;

import com.example.pladialmserver.product.resource.entity.Resource;
import com.example.pladialmserver.user.entity.User;

public class TestResourceInfo {

    public static Resource setUpResource(User adminUser) {
        return new Resource(
                1L,
                "ENG 카메라",
                "소니",
                "401호",
                "파손 주의",
                "sample.jpg",
                adminUser);
    }
}
