package cz.vmacura.ear.upload.service;

import cz.vmacura.ear.upload.entities.Payment;
import cz.vmacura.ear.upload.repositories.PaymentRepository;
import cz.vmacura.ear.upload.security.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class PaymentService
{

	private PaymentRepository paymentRepository;

	@Autowired
	public PaymentService(PaymentRepository paymentRepository)
	{
		this.paymentRepository = paymentRepository;
	}

	public void persist(Payment payment) {
		this.paymentRepository.persist(payment);
	}

	public boolean createPayment(Payment payment) {
		payment.setAccount(((UserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails()).getAccount());
		if (payment.getDate() == null)
			payment.setDate(new Date());

		persist(payment);
		return true;
	}

	public boolean deletePayment(int id){
		Payment payment = paymentRepository.find(id);
		if (payment != null) {
			paymentRepository.remove(payment);
			return true;
		}

		return false;
	}

	public Payment findPayment(int id) {
		return paymentRepository.find(id);
	}

}
