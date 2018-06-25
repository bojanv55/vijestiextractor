package me.vukas;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RijecRepository extends JpaRepository<Rijec, Integer> {
    Optional<Rijec> findByRijec(String rijec);
}
