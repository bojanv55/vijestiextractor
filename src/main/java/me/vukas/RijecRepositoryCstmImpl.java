package me.vukas;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

public class RijecRepositoryCstmImpl implements RijecRepositoryCstm {


	private final EntityManager entityManager;

	RijecRepositoryCstmImpl(
			EntityManagerFactory entityManagerFactory) {

		// Keep the EntityManager around to used from the newly introduced methods.
		this.entityManager = entityManagerFactory.createEntityManager();
	}

	@Override
	public Optional<Rijec> findByRijec(String rijec) {
		return entityManager.createQuery("select u from Rijec u where u.rijec = :rijec", Rijec.class)
				.setParameter("rijec", rijec)
				.setMaxResults(1)
				.getResultList()
				.stream()
				.findFirst();
	}

	@Override
	public Rijec saveN(Rijec rijec) {
		EntityTransaction t = entityManager.getTransaction();
		t.begin();
		entityManager.merge(rijec);
		t.commit();
		return rijec;
	}
}
