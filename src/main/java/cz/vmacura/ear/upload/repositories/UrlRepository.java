package cz.vmacura.ear.upload.repositories;

import cz.vmacura.ear.upload.entities.Batch;
import cz.vmacura.ear.upload.entities.File;
import cz.vmacura.ear.upload.entities.Url;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;

@Repository
public class UrlRepository extends AbstractRepository<Url>
{
	public UrlRepository()
	{
		super(Url.class);
	}

	public List<Url> getShareableLink(File file){
        return (List<Url>) em.createQuery("SELECT f FROM Url f WHERE f.file = ?1").setParameter(1, file).getResultList();

    }

	public List<Url> getShareableLink(Batch batch){
		return (List<Url>) em.createQuery("SELECT f FROM Url f WHERE f.batch = ?1").setParameter(1, batch).getResultList();
	}

    public Url findByCode(String code){
		try {
			System.out.println(code);
			return (Url) em.createQuery("SELECT u FROM Url u WHERE u.string LIKE :code").setParameter("code", code).getSingleResult();
		} catch (NoResultException e) {
			e.printStackTrace();
		}
		return null;
    }
}
