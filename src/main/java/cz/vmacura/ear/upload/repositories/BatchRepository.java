package cz.vmacura.ear.upload.repositories;

import cz.vmacura.ear.upload.entities.Batch;
import cz.vmacura.ear.upload.entities.File;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class BatchRepository extends AbstractRepository<Batch>
{

    public BatchRepository()
    {
        super(Batch.class);
    }

    public Batch findByUrl(String url) {
        return (Batch) em.createQuery("SELECT b FROM Batch b WHERE b.url = ?1").setParameter(1, url).getSingleResult();
    }
}
