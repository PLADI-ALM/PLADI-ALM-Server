package com.example.pladialmserver.user.repository;

import com.example.pladialmserver.user.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PositionRepository extends JpaRepository<Position, Long> {
    Optional<Position> findByName(String name);
}