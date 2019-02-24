package cz.vmacura.ear.upload.repositories;

import cz.vmacura.ear.upload.entities.Account;
import cz.vmacura.ear.upload.entities.File;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.Assert.*;

public class FileRepositoryTest extends BaseRepositoryTestRunner{


    @PersistenceContext
    private EntityManager em;

    @Autowired
    private FileRepository fileRepository;
    @Test
    public void findByNameReturnsFileWithGivenName() {
        File file = new File();
        file.setId(8888);
        file.setName("pes");
        file.setLocation("/home/living_room");
        fileRepository.persist(file);
        em.merge(file);


        final File foundResult = fileRepository.findByName(file.getName());
        assertNotNull(foundResult);
        assertEquals(file.getId(), foundResult.getId());
        assertEquals(file.getLocation(), foundResult.getLocation());
    }

}