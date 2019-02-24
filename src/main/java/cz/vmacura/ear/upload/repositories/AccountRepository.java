package cz.vmacura.ear.upload.repositories;

import cz.vmacura.ear.upload.entities.Account;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;

@SqlResultSetMapping(
	name = "AccountUploadCountMapping",
	entities = @EntityResult(
		entityClass = Account.class,
		fields = {
			@FieldResult(name = "id", column = "a.id"),
			@FieldResult(name = "username", column = "a.username"),
			@FieldResult(name = "role", column = "a.role")}),
	columns = {
		@ColumnResult(name = "fileCount", type = Long.class)
	})
@Repository
public class AccountRepository extends AbstractRepository<Account>
{

	public AccountRepository()
	{
		super(Account.class);
	}

	public Account findByUsername(String username)
	{
		try{
			return (Account) em.createQuery("SELECT a FROM Account a WHERE a.username = ?1").setParameter(1, username).getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}


	public List<Object[]> getUploadedCountByAccount()
	{
		List<Object[]> result = this.em.createNativeQuery("SELECT a.id, a.username, a.role, count(f.id) as fileCount FROM Account a LEFT JOIN File f ON f.account_id = a.id GROUP BY a.id, a.username, a.role", "AccountUploadCountMapping").getResultList();
		return result;
	}
}
