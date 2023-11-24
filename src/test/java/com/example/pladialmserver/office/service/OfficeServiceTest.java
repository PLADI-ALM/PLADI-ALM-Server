package com.example.pladialmserver.office.service;

import com.example.pladialmserver.booking.entity.OfficeBooking;
import com.example.pladialmserver.booking.repository.officeBooking.OfficeBookingRepository;
import com.example.pladialmserver.global.IntegrationTestSupport;
import com.example.pladialmserver.office.dto.response.OfficeRes;
import com.example.pladialmserver.office.entity.Facility;
import com.example.pladialmserver.office.entity.Office;
import com.example.pladialmserver.office.entity.OfficeFacility;
import com.example.pladialmserver.office.repository.FacilityRepository;
import com.example.pladialmserver.office.repository.OfficeFacilityRepository;
import com.example.pladialmserver.office.repository.OfficeRepository;
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

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class OfficeServiceTest extends IntegrationTestSupport {
    @Autowired private OfficeService officeService;
    @Autowired private UserRepository userRepository;
    @Autowired private OfficeRepository officeRepository;
    @Autowired private OfficeBookingRepository officeBookingRepository;
    @Autowired private FacilityRepository facilityRepository;
    @Autowired private OfficeFacilityRepository officeFacilityRepository;
    @Autowired private AffiliationRepository affiliationRepository;
    @Autowired private DepartmentRepository departmentRepository;

    @AfterEach
    void tearDown(){
        // 참조하는 엔티티 먼저 삭제
        officeBookingRepository.deleteAllInBatch();
        officeFacilityRepository.deleteAllInBatch();
        // 참조되는 엔티티 삭제
        officeRepository.deleteAllInBatch();
        facilityRepository.deleteAllInBatch();

    }

    @Test
    @DisplayName("회의실 목록 조회 테스트")
    void findAvailableOfficesTest() {
        // given
        LocalDate date = LocalDate.now().plusDays(1);
        LocalTime startTime = LocalTime.of(10, 0);
        LocalTime endTime = LocalTime.of(12, 0);

        Office office = createAndSaveOffice("회의실 A", "1층");
        Facility facility = createAndSaveFacility("빔 프로젝터");
        createAndSaveOfficeFacility(office, facility);

        Affiliation affiliation = createAndSaveAffiliation("플래디");
        Department department = createAndSaveDepartment("개발부서");
        User user = createAndSaveUser("testuser", "testuser@example.com", department, affiliation);

        createAndSaveOfficeBooking(office, date, startTime.minusHours(1), endTime.minusHours(1), user);

        // when
        Pageable pageable = PageRequest.of(0, 10);
        Page<OfficeRes> result = officeService.findAvailableOffices(date, startTime, endTime, facility.getName(), pageable);

        // then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        result.getContent().forEach(officeRes -> {
            assertEquals("회의실 A", officeRes.getName());
            assertTrue(officeRes.getFacilityList().contains("빔 프로젝터"));
        });
    }

    private User createAndSaveUser(String name, String email, Department department, Affiliation affiliation) {
        User user = User.builder()
                .name(name)
                .email(email)
                .password("password")
                .phone("010-1234-5678")
                .role(Role.ADMIN)
                .department(department)
                .affiliation(affiliation)
                .build();
        userRepository.save(user);
        return user;
    }

    private Office createAndSaveOffice(String name, String location) {
        return officeRepository.save(Office.builder()
                .name(name)
                .location(location)
                .capacity(String.valueOf(10))
                .description("테스트 회의실")
                .isActive(true)
                .build());
    }


    private Facility createAndSaveFacility(String name) {
        return facilityRepository.save(Facility.builder()
                .name(name)
                .build());
    }

    private void createAndSaveOfficeFacility(Office office, Facility facility) {
        officeFacilityRepository.save(OfficeFacility.builder()
                .office(office)
                .facility(facility)
                .build());
    }

    private void createAndSaveOfficeBooking(Office office, LocalDate date, LocalTime startTime, LocalTime endTime, User user) {
        officeBookingRepository.save(OfficeBooking.builder()
                .office(office)
                .date(date)
                .startTime(startTime)
                .endTime(endTime)
                .user(user)
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

    @DisplayName("회의실 개별 조회")
    @Test
    void getOffice() throws Exception{
        //given

        //when

        //then
    }

}
