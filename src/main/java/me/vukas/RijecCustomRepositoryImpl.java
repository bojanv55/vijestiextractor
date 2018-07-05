package me.vukas;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.transaction.annotation.Transactional;

public class RijecCustomRepositoryImpl implements RijecCustomRepository {

	private final EntityManager entityManager;

	public RijecCustomRepositoryImpl(JpaContext context) {
		this.entityManager = context.getEntityManagerByManagedType(Rijec.class);
	}

	@Transactional
	@Override
	public void zapamtiBatch(Iterable<Rijec> rijeci) {
		for(Rijec rijec : rijeci){
				entityManager.merge(rijec);
		}

		entityManager.flush();
		entityManager.clear();
	}
}
