package cz.vmacura.ear.upload.rest;

import cz.vmacura.ear.upload.entities.Account;
import cz.vmacura.ear.upload.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/account")
public class AccountController
{
    private final AccountService accountService;
    private static final Logger LOG = LoggerFactory.getLogger(FileController.class);

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Account getAccount(@PathVariable("id") Integer id)
    {
        switch (accountService.isAuthorizedRequestForAccount(id)) {
            case "UNAVAILABLE RESOURCES":
                System.out.println("You are requesting unavailable resources.");
                return null;
            case "INVALID":
                System.out.println("You are neither admin nor the account owner, sorry.");
                return null;
            default:
                final Account account = accountService.find(id);
                if (account == null)
                    LOG.debug("could not find account:  {}.");

                return account;
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean removeAccount(@PathVariable("id") Integer id) {
        switch (accountService.isAuthorizedRequestForAccount(id)) {
            case "UNAVAILABLE RESOURCES":
                System.out.println("You are requesting unavailable resources.");
                return false;
            case "INVALID":
                System.out.println("You are neither admin nor the account owner, sorry.");
                return false;
            default:
                return accountService.removeAccount(id);
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public String createAccount(@RequestBody Account account)
    {
        return accountService.createAccount(account);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public boolean editAccount(@PathVariable int id, @RequestBody Account account)
    {
        Account original = this.accountService.find(id);
        accountService.update(original, account);
        return true;
    }
    @PreAuthorize("hasAnyAuthority('1')")
    @RequestMapping(value = "/stats", method = RequestMethod.GET)
    public @ResponseBody List<Map<String, String>> getAccountStats()
    {
        return accountService.getUploadStats();
    }
}
