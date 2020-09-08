package sosholic.andrejnemec.calendar.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import sosholic.andrejnemec.calendar.repository.UserRepository;
import sosholic.andrejnemec.calendar.security.filters.AuthenticationFilter;
import sosholic.andrejnemec.calendar.security.filters.AuthorizationFilter;
import sosholic.andrejnemec.calendar.security.user.UserPrincipalService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private UserPrincipalService userPrincipalService;
    private UserRepository userRepository;

    public SecurityConfig(UserPrincipalService userPrincipalService, UserRepository userRepository) {
        this.userPrincipalService = userPrincipalService;
        this.userRepository = userRepository;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //Basic settings
                .csrf()
                .disable()

                //Session settings
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()

                //Add JWT filters
                .addFilter(new AuthenticationFilter(authenticationManager()))
                .addFilter(new AuthorizationFilter(authenticationManager(), this.userRepository))

                //SETUP AUTHORIZATION
                .authorizeRequests()

                //ALLOWED REQUESTS
                .antMatchers("/").permitAll()
                .antMatchers("/register").permitAll()
                .antMatchers("/index.html").permitAll()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/img/**").permitAll()
                .antMatchers("/favicon.ico").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/api/user/register").permitAll()

                .anyRequest().authenticated();
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(this.userPrincipalService);

        return daoAuthenticationProvider;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}