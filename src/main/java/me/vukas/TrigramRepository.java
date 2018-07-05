package me.vukas;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TrigramRepository extends JpaRepository<Trigram, TrigramKey> {
	String KEY = "trigram";

	//@Cacheable(KEY)
	Optional<Trigram> findByKey(TrigramKey key);

	//@CachePut(KEY)
	@Override
	<S extends Trigram> S save(S s);
}
