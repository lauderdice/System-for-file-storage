package cz.vmacura.ear.upload.security;


import cz.vmacura.ear.upload.entities.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

public class UserDetails implements org.springframework.security.core.userdetails.UserDetails {

    private Account account;

    protected final Set<GrantedAuthority> authorities;

    public UserDetails(Account account) {
        Objects.requireNonNull(account);
        this.account = account;
        this.authorities = new HashSet<>();
        addUserRole();
    }

    public UserDetails(Account account, Collection<GrantedAuthority> authorities) {
        Objects.requireNonNull(account);
        Objects.requireNonNull(authorities);
        this.account = account;
        this.authorities = new HashSet<>();
        addUserRole();
        this.authorities.addAll(authorities);
    }

    private void addUserRole() {
        System.out.println("adding role: "+String.valueOf(account.getRole()));
        authorities.add(new SimpleGrantedAuthority(String.valueOf(account.getRole())));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.unmodifiableCollection(authorities);
    }
    public void eraseCredentials() {
        account.erasePassword();
    }

    @Override
    public String getPassword() {
        return account.getPassword();
    }

    @Override
    public String getUsername() {
        return account.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Account getAccount() {
        return account;
    }

}
