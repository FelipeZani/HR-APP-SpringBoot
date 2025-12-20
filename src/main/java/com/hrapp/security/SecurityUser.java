package com.hrapp.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.hrapp.domain.User;

public class SecurityUser implements UserDetails {
    private final User user;

    public SecurityUser(User user){
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        String role = user.getRole();
        String r = role.startsWith("ROLE_") ? role : "ROLE_" + role;
        return List.of(new SimpleGrantedAuthority(r));
    }

    @Override
    public String getPassword(){
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    public User getUser() {
    return user;
    }


    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
