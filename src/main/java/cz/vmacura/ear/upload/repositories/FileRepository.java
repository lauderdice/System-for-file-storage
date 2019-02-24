package cz.vmacura.ear.upload.repositories;

import cz.vmacura.ear.upload.entities.Account;
import cz.vmacura.ear.upload.entities.File;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Metamodel;
import java.util.List;

@Repository
public class FileRepository extends AbstractRepository<File>
{

	public FileRepository()
	{
		super(File.class);
	}

	public File findByName(String name)
	{
		return (File) this.em.createQuery("SELECT f FROM File f WHERE f.name = ?1").setParameter(1, name).getSingleResult();
	}

	public List<File> findByAccount(Account account)
	{
		return (List<File>) this.em.createQuery("SELECT f FROM File f WHERE f.account. = ?1").setParameter(1, account).getResultList();
	}

	public List<File> findAll()
	{
		return (List<File>) this.em.createQuery("SELECT f FROM File f").getResultList();
	}

	public List<File> findAllAdminFiles() {
		CriteriaBuilder cb = this.em.getCriteriaBuilder();
		CriteriaQuery query = cb.createQuery(File.class);
		Root<File> r = query.from(File.class);
		Join<File, Account> join = r.join("account");
		query.where(
			cb.equal(
				join.get("role"), Account.ROLE_ADMIN
			)
		).distinct(true);
		return em.createQuery(query).getResultList();
	}
}
