package sosholic.andrejnemec.calendar.security.filters;

import com.auth0.jwt.JWT;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import sosholic.andrejnemec.calendar.entities.User;
import sosholic.andrejnemec.calendar.repository.UserRepository;
import sosholic.andrejnemec.calendar.security.SecurityProperties;
import sosholic.andrejnemec.calendar.security.user.UserPrincipal;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

public class AuthorizationFilter extends BasicAuthenticationFilter {

    private UserRepository userRepository;

    public AuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(SecurityProperties.HEADER_STRING);

        if (header == null || !header.startsWith(SecurityProperties.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        SecurityContextHolder.getContext().setAuthentication(getUsernamePasswordAuthentication(request));

        chain.doFilter(request, response);
    }

    private Authentication getUsernamePasswordAuthentication(HttpServletRequest request) {
        String token = request.getHeader(SecurityProperties.HEADER_STRING).replace(SecurityProperties.TOKEN_PREFIX, "");
        String userName = JWT.require(HMAC512(SecurityProperties.SECRET_KEY.getBytes()))
                .build()
                .verify(token)
                .getSubject();

        if (userName == null) return null;

        User user = userRepository.findByUsername(userName);

        UserPrincipal principal = new UserPrincipal(user);

        return new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

    }
}