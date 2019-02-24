package cz.vmacura.ear.upload.rest;


import cz.vmacura.ear.upload.entities.Account;
import cz.vmacura.ear.upload.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/login")
public class LoginController {
    private static final Logger LOG = LoggerFactory.getLogger(FileController.class);
    private final AccountService accountService;


    @Autowired
    public LoginController(AccountService accountService){
        this.accountService=accountService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView loginPage() {
        ModelAndView model = new ModelAndView();
        model.setViewName("login");
        return model;
    }


    @RequestMapping(value = "/process_login", method = RequestMethod.POST, consumes = "multipart/form-data")
    public String processLoginData (@RequestParam("username")String username, @RequestParam("password")String password) { return "";}
}