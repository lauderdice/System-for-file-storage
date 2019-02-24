package cz.vmacura.ear.upload.repositories;

import cz.vmacura.ear.upload.entities.Account;
import cz.vmacura.ear.upload.entities.Payment;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PaymentRepository extends AbstractRepository<Payment>
{
	public PaymentRepository()
	{
		super(Payment.class);
	}

	public List<Payment> findByAccount(Account account)
	{
		return this.em.createQuery("SELECT p FROM Payment p WHERE p.account = ?1").setParameter(1, account).getResultList();
	}
}
