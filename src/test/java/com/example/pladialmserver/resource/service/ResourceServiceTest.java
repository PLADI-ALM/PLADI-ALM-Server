package com.example.pladialmserver.resource.service;

import com.example.pladialmserver.global.IntegrationTestSupport;
import com.example.pladialmserver.global.utils.EmailUtil;
import com.example.pladialmserver.product.resource.repository.ResourceRepository;
import com.example.pladialmserver.product.resource.service.ResourceService;
import com.example.pladialmserver.user.repository.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;

public class ResourceServiceTest extends IntegrationTestSupport {
    @Autowired private ResourceService resourceService;
    @Autowired private ResourceRepository resourceRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private EmailUtil emailUtil;

    @AfterEach()
    void tearDown(){
        resourceRepository.deleteAllInBatch();;
        userRepository.deleteAllInBatch();;
    }
}
