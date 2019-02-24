package cz.vmacura.ear.upload.repositories;

import cz.vmacura.ear.upload.entities.Account;
import cz.vmacura.ear.upload.service.BaseServiceTestRunner;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.Assert.*;

public class AccountRepositoryTest extends BaseRepositoryTestRunner
{

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void findByUsernameReturnsAccountWithGivenUsername() {
        Account account = new Account();
        account.setUsername("BULHAR");
        account.setPassword("BULHAR_PASSWORD");
        accountRepository.persist(account);
        account = em.merge(account);


        final Account foundResult = accountRepository.findByUsername(account.getUsername());
        assertNotNull(foundResult);
        assertEquals(account.getId(), foundResult.getId());
    }
}