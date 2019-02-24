package cz.vmacura.ear.upload.service;

import cz.vmacura.ear.upload.entities.Account;
import cz.vmacura.ear.upload.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class AccountService
{

	private final AccountRepository accountRepository;
	private PasswordEncoder encoder;

	@Autowired
	public AccountService(AccountRepository accountRepository,PasswordEncoder encoder)
	{
		this.accountRepository = accountRepository;
		this.encoder = encoder;
	}

	public Account find(Integer id)
	{
		return this.accountRepository.find(id);
	}

	public Account findByUsername(String username)
	{
		return this.accountRepository.findByUsername(username);
	}

	public void persist(Account account)
	{
		this.accountRepository.persist(account);
	}

	public boolean removeAccount(int id){
	    Account account = find(id);
	    if(account!=null){
	        accountRepository.remove(account);

        }
        return true;
    }
    public String createAccount(Account passed_account){
		System.out.println("were here");
	    if(accountRepository.findByUsername(passed_account.getUsername())==null){
            Account account = new Account();
            account.setPassword(encoder.encode(passed_account.getPassword()));
            account.setUsername(passed_account.getUsername());
            accountRepository.persist(account);
            return "Created account";
        }else{
	        return "Account with this username already exists";
        }

    }

    public String isAuthorizedRequestForAccount(int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUser = authentication.getName();
        String accountOwner = "";
        try {
            accountOwner = find(id).getUsername();
        } catch (NullPointerException e) {
            return "UNAVAILABLE RESOURCES";
        }
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("1")) || loggedInUser.equals(accountOwner)) {
            return "VALID";
        } else {
            return "INVALID";
        }
    }


	public void update(Account original, Account account)
	{
		//if(account.getRole() != n){
		//	original.setRole(account.getRole());

		//}
		if (account.getUsername() != null)
			original.setUsername(account.getUsername());
		if (account.getPassword() != null)
			original.setPassword(account.getPassword());
		if (account.getUsername() != null)
			original.setUsername(account.getUsername());

		original.setRole(account.getRole());

		this.persist(original);
	}

	public List<Map<String, String>> getUploadStats()
	{
		List<Object[]> result = this.accountRepository.getUploadedCountByAccount();
		return result.stream()
				.map(account -> {
					Map<String, String> map = new HashMap<>();
					map.put("id", String.valueOf(account[0]));
					map.put("username", String.valueOf(account[1]));
					map.put("fileCount", String.valueOf(account[2]));
					return map;
				}).collect(Collectors.toList());
	}
}
