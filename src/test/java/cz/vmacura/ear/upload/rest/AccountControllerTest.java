package cz.vmacura.ear.upload.rest;

import cz.vmacura.ear.upload.entities.Account;
import cz.vmacura.ear.upload.environment.Generator;
import cz.vmacura.ear.upload.service.AccountService;
import cz.vmacura.ear.upload.service.PaymentService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

public class AccountControllerTest extends BaseControllerTest{


    @Mock
    private AccountService accountServiceMock;


    @InjectMocks
    private AccountController accountController = new AccountController(accountServiceMock);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        super.setUp(accountController);
    }
    @Test
    public void createAccount() throws Exception{
        Account account = Generator.generateAccount();

        final MvcResult mvcResult = mockMvc.perform(post("/account/").content(toJson(account)).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string("")).andReturn();


    }
}