package com.mvarlamov.Music.data.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(value = { "login", "pwdHash", "authorities", "username", "enabled", "accountNonLocked", "accountNonExpired", "credentialsNonExpired" })
public class User implements UserDetails {
    private String login;
    private String pwdHash;
    private Role role;
    private String name;
    private Integer age;
    private Gender gender;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (role.equals(Role.ADMIN))
            return Stream.of(Role.ADMIN, Role.USER, Role.ANON).collect(Collectors.toList());
        else if (role.equals(Role.USER))
            return Stream.of(Role.USER, Role.ANON).collect(Collectors.toList());
        else
            return Collections.singleton(Role.ANON);
    }

    @Override
    public String getPassword() {
        return pwdHash;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static User anon() {
        return new User()
                .setRole(Role.ANON)
                .setName(Role.ANON.name());
    }
}
