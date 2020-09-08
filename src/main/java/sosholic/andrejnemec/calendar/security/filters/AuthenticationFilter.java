package sosholic.andrejnemec.calendar.security.filters;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import sosholic.andrejnemec.calendar.security.SecurityProperties;
import sosholic.andrejnemec.calendar.security.credentials.UserAuthenticationCredentials;
import sosholic.andrejnemec.calendar.security.user.UserPrincipal;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

@AllArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        UserAuthenticationCredentials credentials = null;

        try {
            credentials = new ObjectMapper().readValue(request.getInputStream(), UserAuthenticationCredentials.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                credentials.getUsername(),
                credentials.getPassword(),
                new ArrayList<>()));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        UserPrincipal principal = (UserPrincipal) authResult.getPrincipal();

        String token = JWT.create()
                .withSubject(principal.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityProperties.EXPIRATION_TIME))
                .sign(HMAC512(SecurityProperties.SECRET_KEY.getBytes()));

        response.addHeader(SecurityProperties.HEADER_STRING, SecurityProperties.TOKEN_PREFIX + token);
    }
}