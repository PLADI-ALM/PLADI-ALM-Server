package com.example.pladialmserver.office.repository.facility;


import java.util.List;

public interface FacilityCustom {

    List<String> findByNameContainingAndIsActive(String name);

}
