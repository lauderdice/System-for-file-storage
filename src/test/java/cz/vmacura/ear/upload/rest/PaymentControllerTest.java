package cz.vmacura.ear.upload.rest;

import cz.vmacura.ear.upload.entities.Payment;
import cz.vmacura.ear.upload.environment.Generator;
import cz.vmacura.ear.upload.service.PaymentService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PaymentControllerTest extends BaseControllerTest{


    @Mock
    private PaymentService paymentServiceMock;


    @InjectMocks
    private PaymentController paymentController = new PaymentController(paymentServiceMock);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        super.setUp(paymentController);
    }
    @Test
    public void getPaymentReturnsPaymentWithCorrectId() throws Exception{
        final Payment payment = new Payment();
        payment.setId(124);
        when(paymentServiceMock.findPayment(payment.getId())).thenReturn(payment);
        final MvcResult mvcResult = mockMvc.perform(get("/payment/" + payment.getId())).andReturn();

        final Payment result = readValue(mvcResult, Payment.class);
        assertNotNull(result);
        assertEquals(payment.getId(), result.getId());
    }

}