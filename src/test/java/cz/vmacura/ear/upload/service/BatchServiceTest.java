package cz.vmacura.ear.upload.service;

import cz.vmacura.ear.upload.entities.Account;
import cz.vmacura.ear.upload.entities.Batch;
import cz.vmacura.ear.upload.entities.Url;
import cz.vmacura.ear.upload.environment.Generator;
import cz.vmacura.ear.upload.repositories.UrlRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.BeforeClass;
import static org.junit.Assert.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class BatchServiceTest extends BaseServiceTestRunner {


    @PersistenceContext
    private EntityManager em;

    @Autowired
    private BatchService batchService;

    private final Batch batch = Generator.generateBatch();
    @Before
    public void setUp() {
        em.persist(batch);
    }

    @Test
    public void findReturnsBatchWithValidId()
    {
        final Batch result =  batchService.findBatch(1);
        assertEquals(batch, result);
    }

}