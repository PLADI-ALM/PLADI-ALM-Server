package com.example.pladialmserver.resource.service;

import com.example.pladialmserver.booking.entity.ResourceBooking;
import com.example.pladialmserver.booking.repository.resourceBooking.ResourceBookingRepository;
import com.example.pladialmserver.booking.repository.resourceBooking.ResourceBookingRepositoryImpl;
import com.example.pladialmserver.global.Constants;
import com.example.pladialmserver.global.IntegrationTestSupport;
import com.example.pladialmserver.global.entity.BookingStatus;
import com.example.pladialmserver.global.utils.EmailUtil;
import com.example.pladialmserver.notification.service.PushNotificationService;
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
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static com.example.pladialmserver.booking.service.model.TestResourceBookingInfo.setUpResourceBookingByDate;
import static com.example.pladialmserver.global.Constants.EmailNotification.*;
import static com.example.pladialmserver.global.Constants.EmailNotification.BOOKING_TEMPLATE;
import static com.example.pladialmserver.product.resource.entity.QResource.resource;
import static com.example.pladialmserver.user.service.model.TestUserInfo.*;
import static com.example.pladialmserver.user.service.model.TestUserInfo.PASSWORD;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class ResourceServiceTest extends IntegrationTestSupport {
    @Autowired private ResourceService resourceService;
    @Autowired private UserRepository userRepository;
    @Autowired private ResourceRepository resourceRepository;
    @Autowired private ResourceBookingRepository resourceBookingRepository;
    @Autowired private AffiliationRepository affiliationRepository;
    @Autowired private DepartmentRepository departmentRepository;

    @Mock
    private EmailUtil emailUtil;

    @Mock
    private PushNotificationService notificationService;

    @Spy
    BCryptPasswordEncoder passwordEncoder;

    @Mock
    private ResourceService resourceMockService;

    @Mock
    private ResourceBookingRepository resourceBookingMockRepository;

    @Mock
    private ResourceBookingRepositoryImpl resourceBookingRepositoryImpl;

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
        User user = User.builder()
                .id(1L)
                .name("홍길동")
                .email("test@email.com")
                .password("asdf")
                .phone("010-0000-0000")
                .department(department)
                .role(Role.ADMIN)
                .assets("A12345")
                .affiliation(affiliation)
                .assets("1234545")
                .build();
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

    @Test
    @DisplayName("[성공] 해당 날짜의 장비 예약된 시간 조회")
    @Transactional
    void getProductBookedTime_SUCCESS() {
        // given
        User basicUser = setUpUser(1L, Role.BASIC, setUpDepartment(), setUpAffiliation(), passwordEncoder.encode(PASSWORD));
        User adminUser = setUpUser(2L, Role.ADMIN, setUpDepartment(), setUpAffiliation(), passwordEncoder.encode(PASSWORD));

        ResourceBooking resourceBooking = setUpResourceBookingByDate(1L, basicUser, adminUser,
                        LocalDateTime.of(2024, 12, 1, 16, 0),
                        LocalDateTime.of(2024, 12, 1, 19, 0));
        ResourceBooking resourceBooking2 = setUpResourceBookingByDate(2L, basicUser, adminUser,
                LocalDateTime.of(2024, 12, 1, 21, 0),
                LocalDateTime.of(2024, 12, 2, 19, 0));
        resourceBookingMockRepository.saveAndFlush(resourceBooking);
        resourceBookingMockRepository.saveAndFlush(resourceBooking2);

        // when
        when(resourceBookingMockRepository.findById(anyLong())).thenReturn(Optional.of(resourceBooking));
        when(resourceBookingRepositoryImpl.getBookedTime(any(Resource.class), any(LocalDate.class))).thenReturn(anyList());
        Optional<ResourceBooking> all = resourceBookingMockRepository.findById(1L);
        List<String> response = resourceBookingRepositoryImpl.getBookedTime(resourceBooking.getResource(), LocalDate.of(2024, 12, 1));


        // then
        System.out.println(all.get().getResourceBookingId());
        for (String s : response) {
            System.out.println(s);
        }

    }

}
