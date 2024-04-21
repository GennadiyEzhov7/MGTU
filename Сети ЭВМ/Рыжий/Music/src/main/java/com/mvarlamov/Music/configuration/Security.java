package com.mvarlamov.Music.configuration;

import com.mvarlamov.Music.data.dao.SessionDAO;
import com.mvarlamov.Music.data.dao.UserDAO;
import com.mvarlamov.Music.data.model.Role;
import com.mvarlamov.Music.data.model.Session;
import com.mvarlamov.Music.data.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.security.auth.login.FailedLoginException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class Security{
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/register").permitAll()
                .anyRequest().authenticated();
        http.addFilterBefore(getFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Autowired
    private UserDAO userDAO;
    @Autowired
    private SessionDAO sessionDAO;
    @Bean
    public OncePerRequestFilter getFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
                User user;

                System.err.println(Arrays.toString(request.getCookies()));

                String token = request.getHeader("Authorization");
                if (!tokenIsValid(token))
                    user = User.anon();
                else {
                    token = token.substring(7);
                    Session session = sessionDAO.getByToken(token);
                    if (session == null)
                        user = User.anon();
                    else
                        user = userDAO.getByLogin(session.getUserLogin());
                }

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        user == null ? Collections.singleton(Role.ANON) : user.getAuthorities()
                );

                if (user == null)
                    auth.setAuthenticated(false);

                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
                filterChain.doFilter(request, response);
            }
        };
    }

    private boolean tokenIsValid(String token) {
        return token != null
                && token.startsWith("Bearer ")
                && token.length() > 8;
    }

    @Bean
    public UserDetailsService getUserDetails() {
        return username -> {
            User user = userDAO.getByLogin(username);
            if (user == null)
                throw new UsernameNotFoundException("User not found with login '" + username + "'");
            return user;
        };
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return authentication -> {
            UserDetails user = getUserDetails().loadUserByUsername((String) authentication.getPrincipal());
            if (passwordEncoder().matches(user.getPassword(), (String) authentication.getCredentials())) {
                return new UsernamePasswordAuthenticationToken(user, user, user.getAuthorities());
            }
            else {
                authentication.setAuthenticated(false);
                return authentication;
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return rawPassword.toString();
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                System.err.println(rawPassword + " - " + encodedPassword);
                return rawPassword.toString().equals(encodedPassword);
            }
        };
    }
}
