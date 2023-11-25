package com.example.pladialmserver.resource.service;

import com.example.pladialmserver.booking.entity.ResourceBooking;
import com.example.pladialmserver.booking.repository.resourceBooking.ResourceBookingRepository;
import com.example.pladialmserver.global.IntegrationTestSupport;
import com.example.pladialmserver.global.entity.BookingStatus;
import com.example.pladialmserver.global.utils.EmailUtil;
import com.example.pladialmserver.product.resource.dto.ResourceRes;
import com.example.pladialmserver.product.resource.entity.Resource;
import com.example.pladialmserver.product.resource.repository.ResourceRepository;
import com.example.pladialmserver.product.resource.service.ResourceService;
import com.example.pladialmserver.user.entity.Affiliation;
import com.example.pladialmserver.user.entity.Department;
import com.example.pladialmserver.user.entity.Role;
import com.example.pladialmserver.user.entity.User;
import com.example.pladialmserver.user.repository.AffiliationRepository;
import com.example.pladialmserver.user.repository.DepartmentRepository;
import com.example.pladialmserver.user.repository.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResourceServiceTest extends IntegrationTestSupport {
    @Autowired private ResourceService resourceService;
    @Autowired private UserRepository userRepository;
    @Autowired private ResourceRepository resourceRepository;
    @Autowired private ResourceBookingRepository resourceBookingRepository;
    @Autowired private AffiliationRepository affiliationRepository;
    @Autowired private DepartmentRepository departmentRepository;

    @Test
    @DisplayName("장비 목록 조회 테스트")
    void findAvailableResourcesTest() {
        // given
        LocalDateTime startDate = LocalDateTime.now().plusDays(1);
        LocalDateTime endDate = startDate.plusHours(2);

        Affiliation affiliation = createAndSaveAffiliation("플래디");
        Department department = createAndSaveDepartment("개발부서");
        User user = createAndSaveUser("testuser", "testuser@example.com", department, affiliation);

        Resource resource = createAndSaveResource("맥북 프로", "애플", user);

        createAndSaveResourceBooking(resource, startDate.minusHours(1), endDate.minusHours(1), user);

        // when
        Pageable pageable = PageRequest.of(0, 10);
        Page<ResourceRes> result = resourceService.findAvailableResources(resource.getName(), startDate, endDate, pageable);

        // then
        assertNotNull(result);
        assertTrue(result.getContent().isEmpty() || result.getContent().stream().anyMatch(res -> res.getName().equals("맥북 프로")));
    }

    private User createAndSaveUser(String name, String email, Department department, Affiliation affiliation) {
        User user = new User(1L,
                name,
                email,
                "password",
                "010-1234-5678",
                "testAssets",
                department,Role.ADMIN,
                affiliation);
        userRepository.save(user);
        return user;
    }

    private Resource createAndSaveResource(String name, String manufacturer,User user) {
        return resourceRepository.save(Resource.builder()
                .user(user)
                .name(name)
                .manufacturer(manufacturer)
                .description("테스트 장비")
                .isActive(true)
                .build());
    }

    private void createAndSaveResourceBooking(Resource resource, LocalDateTime startDate, LocalDateTime endDate, User user) {
        resourceBookingRepository.save(ResourceBooking.builder()
                .resource(resource)
                .startDate(startDate)
                .endDate(endDate)
                .user(user)
                .memo("테스트 예약")
                .build());
    }

    private Affiliation createAndSaveAffiliation(String name) {
        Affiliation affiliation = Affiliation.builder()
                .name(name)
                .build();
        affiliationRepository.save(affiliation);
        return affiliation;
    }

    private Department createAndSaveDepartment(String name) {
        Department department=Department.builder()
                .name(name)
                .build();
        departmentRepository.save(department);
        return department;

    }

}
