package me.vukas;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RijecRepository extends JpaRepository<Rijec, Integer>, RijecRepositoryCstm {
    //Optional<Rijec> findByRijec(String rijec);
}
