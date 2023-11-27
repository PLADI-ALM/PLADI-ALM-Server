package com.example.pladialmserver.office.service;

import com.example.pladialmserver.booking.entity.OfficeBooking;
import com.example.pladialmserver.booking.repository.officeBooking.OfficeBookingRepository;
import com.example.pladialmserver.global.exception.BaseException;
import com.example.pladialmserver.office.dto.request.OfficeReq;
import com.example.pladialmserver.office.entity.Office;
import com.example.pladialmserver.office.repository.office.OfficeRepository;
import com.example.pladialmserver.office.service.model.TestOfficeInfo;
import com.example.pladialmserver.user.entity.Role;
import com.example.pladialmserver.user.entity.User;
import com.example.pladialmserver.user.service.model.TestUserInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static com.example.pladialmserver.global.exception.BaseResponseCode.ALREADY_BOOKED_TIME;
import static com.example.pladialmserver.global.exception.BaseResponseCode.OFFICE_NOT_FOUND;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class OfficeServiceBookingTest {
    @InjectMocks
    private OfficeService officeService;
    @Mock
    private OfficeRepository officeRepository;
    @Mock
    private OfficeBookingRepository officeBookingRepository;
    @Spy
    BCryptPasswordEncoder passwordEncoder;

    @Test
    @DisplayName("[성공] 회의실 예약")
    void bookOffice(){
        // given
        OfficeReq officeReq = TestOfficeInfo.setUpOfficeReq();
        User user = TestUserInfo.setUpUser(1L, Role.ADMIN, TestUserInfo.setUpDepartment(), TestUserInfo.setUpAffiliation(), passwordEncoder.encode("asdf1234!"));
        Office office = TestOfficeInfo.setUpOffice();

        doReturn(Optional.of(office)).when(officeRepository).findByOfficeIdAndIsEnableAndIsActive(office.getOfficeId(), true, true);
        doReturn(false).when(officeBookingRepository).existsByDateAndTime(office, officeReq.getDate(), officeReq.getStartTime(), officeReq.getEndTime());
        when(officeBookingRepository.save(any(OfficeBooking.class))).then(AdditionalAnswers.returnsFirstArg());

        // when
        officeService.bookOffice(user, 1L, officeReq);

        // verify -> because of void method
        verify(officeRepository, times(1)).findByOfficeIdAndIsEnableAndIsActive(any(Long.class), any(Boolean.class), any(Boolean.class));
        verify(officeBookingRepository, times(1)).existsByDateAndTime(any(Office.class), any(LocalDate.class), any(LocalTime.class), any(LocalTime.class));
        verify(officeBookingRepository, times(1)).save(any(OfficeBooking.class));
    }

    @Test
    @DisplayName("[실패] 회의실 예약 - 회의실이 없는 경우")
    void bookOfficeFail(){
        // given
        OfficeReq officeReq = TestOfficeInfo.setUpOfficeReq();
        User user = TestUserInfo.setUpUser(1L, Role.ADMIN, TestUserInfo.setUpDepartment(), TestUserInfo.setUpAffiliation(), passwordEncoder.encode("asdf1234!"));
        doThrow(new BaseException(OFFICE_NOT_FOUND)).when(officeRepository).findByOfficeIdAndIsEnableAndIsActive(2L, true, true);

        // when
        BaseException exception = assertThrows(BaseException.class, () -> {
            officeService.bookOffice(user, 2L, officeReq);
        });

        // then
        assertThat(exception.getBaseResponseCode()).isEqualTo(OFFICE_NOT_FOUND);
    }

    @Test
    @DisplayName("[실패] 회의실 예약 - 이미 존재하는 예약인 경우")
    void bookOfficeFail2(){
        // given
        OfficeReq officeReq = TestOfficeInfo.setUpOfficeReq();
        User user = TestUserInfo.setUpUser(1L, Role.ADMIN, TestUserInfo.setUpDepartment(), TestUserInfo.setUpAffiliation(), passwordEncoder.encode("asdf1234!"));
        Office office = TestOfficeInfo.setUpOffice();
        doReturn(true).when(officeBookingRepository).existsByDateAndTime(office, officeReq.getDate(), officeReq.getStartTime(), officeReq.getEndTime());
        doReturn(Optional.of(office)).when(officeRepository).findByOfficeIdAndIsEnableAndIsActive(office.getOfficeId(), true, true);

        // when
        BaseException exception = assertThrows(BaseException.class, () -> {
            officeService.bookOffice(user, 1L, officeReq);
        });
        // then
        assertThat(exception.getBaseResponseCode()).isEqualTo(ALREADY_BOOKED_TIME);
    }
}
