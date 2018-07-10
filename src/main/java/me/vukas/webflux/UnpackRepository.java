package me.vukas.webflux;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnpackRepository extends ReactiveCrudRepository<Unpack, Integer> {
}
