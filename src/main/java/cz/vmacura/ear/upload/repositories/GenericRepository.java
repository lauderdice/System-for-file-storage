package cz.vmacura.ear.upload.repositories;

/**
 * Base interface for data access objects.
 *
 * @param <T>
 */
public interface GenericRepository<T>
{
    void persist(T entity);
    T find(Integer id);
    void remove(T entity);
    T update(T entity);

}
