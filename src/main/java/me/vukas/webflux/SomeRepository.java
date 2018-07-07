package me.vukas.webflux;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SomeRepository extends ReactiveCrudRepository<Osoba, Integer> {
}
