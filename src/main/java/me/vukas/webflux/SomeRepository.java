package me.vukas.webflux;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

//@Repository
public interface SomeRepository extends ReactiveCrudRepository<Osoba, Integer> {
}
