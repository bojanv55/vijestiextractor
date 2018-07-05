package me.vukas;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RijecRepository extends JpaRepository<Rijec, String>, RijecCustomRepository {
    String KEY = "rijec";

    //@Cacheable(KEY)
    Optional<Rijec> findByRijec(String rijec);

    //@CachePut(KEY)
    @Override
    <S extends Rijec> S save(S s);
}
