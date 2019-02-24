package cz.vmacura.ear.upload.repositories;

import cz.vmacura.ear.upload.entities.Batch;
import cz.vmacura.ear.upload.entities.Url;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class BatchRepositoryTest extends BaseRepositoryTestRunner{


    @Autowired
    BatchRepository batchRepository;

    @Autowired
    UrlRepository urlRepository;
    @PersistenceContext
    private EntityManager em;

    @Autowired

    @Test
    public void findByUrlReturnsFileWithCorrectUrl() {
        Batch batch = new Batch();
        batch.setId(500);
        Url url = new Url();
        String urlString = "dvsndsjvdsjvbjsdhbvvmcnv";
        url.setString(urlString);
        List<Url> urls = new ArrayList<>();
        urls.add(url);
        batch.setUrls(urls);

        batchRepository.persist(batch);
        em.merge(batch);
        urlRepository.persist(url);
        em.merge(url);
        final Batch result = batchRepository.findByUrl(urlString);
        assertEquals(result.getId(),batch.getId());


    }
}