package cz.vmacura.ear.upload.service;

import cz.vmacura.ear.upload.entities.Account;
import cz.vmacura.ear.upload.environment.Generator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.BeforeClass;
import static org.junit.Assert.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class AccountServiceTest extends BaseServiceTestRunner
{


	@PersistenceContext
	private EntityManager em;

	@Autowired
	private AccountService accountService;

	private Account account = Generator.generateAccount();

	@Before
	public void setUp() {
		em.persist(account);
		em.flush();
	}

	@Test
	public void findReturnsAccountWithValidId()
	{
		final Account result =  accountService.find(1);
		assertEquals(account, result);
		assertEquals(account.getUsername(), "Username1");
	}

	@Test
	public void findByUsernameReturnsCorrectAccount()
	{
		final Account result = accountService.findByUsername("Username1");
		assertEquals(account.getId(), result.getId());
	}
	@After
	public void tearDown()
	{
		em.remove(account);
	}
}
