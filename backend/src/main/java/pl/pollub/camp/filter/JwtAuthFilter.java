package pl.pollub.camp.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.pollub.camp.Models.Users;
import pl.pollub.camp.Repositories.UserRepository;
import pl.pollub.camp.Services.JwtService;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;

    private static final String[] WHITELIST = {
            "/reservation/vehicle",
            "/auth/login",
            "/location/all"
    };


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("[" + request.getMethod() + "]: " + request.getServletPath());

        if (request.getServletPath().contains("/auth/login") ||
                request.getServletPath().contains("/reservation/vehicle/") ||
                request.getServletPath().contains("/location/all"))
        {
            filterChain.doFilter(request, response);
            return;
        } // nie weryfikuje JWT dla /auth/login


        final String authorizationHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String email;


        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            System.out.println("no token");
            System.out.println(authorizationHeader);
            return;
        }

        jwtToken = authorizationHeader.substring(7);
        email = jwtService.extractEmail(jwtToken);

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            System.out.println(userDetails.getUsername());
            if (jwtService.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        ///find user and add role to request
        Users u = userRepository.findByEmail(email).orElse(null);

        if (u != null) {
            request.setAttribute("Role", u.getRole());
            request.setAttribute("Username", u.getName());
            request.setAttribute("Email", u.getEmail());
        }else{
            System.out.println("nie znaleziono uzytkownika: "+email);
        }


        filterChain.doFilter(request, response);
    }
}
