package com.example.pladialmserver.office.service;

import com.example.pladialmserver.office.repository.OfficeBookingRepository;
import com.example.pladialmserver.office.repository.OfficeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OfficeService {

    private final OfficeRepository officeRepository;
    private final OfficeBookingRepository officeBookingRepository;

}
