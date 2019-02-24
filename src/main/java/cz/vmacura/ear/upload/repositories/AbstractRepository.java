package cz.vmacura.ear.upload.repositories;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.util.Objects;

abstract class AbstractRepository<T> implements GenericRepository<T>
{

	@PersistenceContext
	protected EntityManager em;

	private final Class<T> type;

	public AbstractRepository(Class<T> type)
	{
		this.type = type;
	}


	@Override
	public T find(Integer id)
	{
		Objects.requireNonNull(id);
		return em.find(type, id);
	}

	@Override
	@Transactional
	public void persist(T entity)
	{
		Objects.requireNonNull(entity);
//		em.merge(entity);
		em.persist(entity);
		em.flush();
	}

	@Override
	public void remove(T entity)
	{
		Objects.requireNonNull(entity);

		try {
			final T toRemove = em.merge(entity);
			if (toRemove != null) {
				em.remove(toRemove);
			}
		} catch (RuntimeException e) {
			throw new PersistenceException(e);
		}
	}

	@Override
	public T update(T entity) {
		Objects.requireNonNull(entity);
		try {
			return em.merge(entity);
		} catch (RuntimeException e) {
			throw new PersistenceException(e);
		}
	}
}
