package com.example.pladialmserver.user.service;

import com.example.pladialmserver.global.feign.dto.UserReq;
import com.example.pladialmserver.global.feign.feignClient.ArchivingServerClient;
import com.example.pladialmserver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ArchivingServerClient archivingServerClient;

    public void addUser(UserReq userReq) {
        archivingServerClient.addUser(userReq);
    }
}
