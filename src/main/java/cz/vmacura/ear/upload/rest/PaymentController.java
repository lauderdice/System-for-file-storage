package cz.vmacura.ear.upload.rest;

import cz.vmacura.ear.upload.entities.Payment;
import cz.vmacura.ear.upload.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

@Controller
@RequestMapping(value = "/payment")
public class PaymentController
{
	private PaymentService paymentService;

	@Autowired
	public PaymentController(PaymentService paymentService)
	{
		this.paymentService = paymentService;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Payment getPayment(@PathVariable("id") Integer id)
	{
		Payment payment = this.paymentService.findPayment(id);
		if (payment == null) {
			throw new EntityNotFoundException("Payment with id " + id + " not found");
		}

		return payment;
	}

	@RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void createPayment(@RequestBody Payment payment)
	{
		this.paymentService.createPayment(payment);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public boolean removePayment(@PathVariable("id") Integer id)
	{
		return this.paymentService.deletePayment(id);
	}
}
