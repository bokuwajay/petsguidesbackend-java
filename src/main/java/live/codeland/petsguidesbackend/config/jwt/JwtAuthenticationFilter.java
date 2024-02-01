package live.codeland.petsguidesbackend.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.ExpiredJwtException;

import java.io.IOException;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(
           @NonNull HttpServletRequest request,
           @NonNull HttpServletResponse response,
           @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            final String authHeader = request.getHeader("Authorization");
            final String jwt;
            final String userEmail;

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                if (request.getRequestURI().startsWith("/api/v1/auth/")) {
                    filterChain.doFilter(request, response);
                } else {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write("Missing Authorization Bearer Token");
                }
                return;
            }

            jwt = authHeader.substring(7);

            userEmail = jwtService.extractUsername(jwt);
            // we can extract email from JWT, but the user still not yet authenticated (null) , so need to check whether this email exist in our DB
            if( userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
                // get uer details from our DB
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                if(jwtService.isTokenValid(jwt, userDetails)){
                    // this object is needed by Spring security to update Security Context
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    // all these steps to update Security Context
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            // after above , always need to pass to next filter
            filterChain.doFilter(request, response);

        } catch (MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException exception) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("JWT token exception: " + exception.getMessage());
        } catch (UsernameNotFoundException exception) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: User not found");
        } catch (Exception exception) {
            // Handle other unexpected exceptions
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Internal Server Error: " + exception.getMessage());
        }


    }
}
