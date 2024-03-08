package live.codeland.petsguidesbackend.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import live.codeland.petsguidesbackend.dto.ApiResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import java.io.PrintWriter;


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

//            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//                if (request.getRequestURI().startsWith("/api/v1/auth/")) {
//                    filterChain.doFilter(request, response);
//                } else {
//                    response.setCharacterEncoding("UTF-8");
//                    response.setContentType("application/json; charset=utf-8");
//                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    String jsonRspBody = objectMapper.writeValueAsString(new ApiResponseDto(HttpStatus.resolve(403), 403, null, "Missing Authorization Bearer Token", null));
//                    PrintWriter printWriter = response.getWriter();
//                    printWriter.append(jsonRspBody);
//                    printWriter.close();
//                }
//                return;
//            }
            if (!request.getRequestURI().startsWith("/api/v1/auth/") && (authHeader == null || !authHeader.startsWith("Bearer "))) {
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json; charset=utf-8");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonRspBody = objectMapper.writeValueAsString(new ApiResponseDto(HttpStatus.resolve(403), 403, null, "Missing Authorization Bearer Token", null));
                PrintWriter printWriter = response.getWriter();
                printWriter.append(jsonRspBody);
                printWriter.close();
                return;
            }

            if (authHeader != null && authHeader.startsWith("Bearer ")){
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
            }


            // after above , always need to pass to next filter
            filterChain.doFilter(request, response);

        } catch (MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException exception) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonRspBody = objectMapper.writeValueAsString(new ApiResponseDto(HttpStatus.resolve(401), 401, null, "JWT token exception: " + exception.getMessage(), null));
            PrintWriter printWriter = response.getWriter();
            printWriter.append(jsonRspBody);
            printWriter.close();
        } catch (UsernameNotFoundException exception) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonRspBody = objectMapper.writeValueAsString(new ApiResponseDto(HttpStatus.resolve(401), 401, null, "Unauthorized: User not found", null));
            PrintWriter printWriter = response.getWriter();
            printWriter.append(jsonRspBody);
            printWriter.close();
        } catch (Exception exception) {
            // Handle other unexpected exceptions
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonRspBody = objectMapper.writeValueAsString(new ApiResponseDto(HttpStatus.resolve(500), 500, null, "Internal Server Error: " + exception.getMessage(), null));
            PrintWriter printWriter = response.getWriter();
            printWriter.append(jsonRspBody);
            printWriter.close();
        }
    }
}
