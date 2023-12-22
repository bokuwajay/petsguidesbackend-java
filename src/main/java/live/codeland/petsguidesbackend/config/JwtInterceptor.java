package live.codeland.petsguidesbackend.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JwtInterceptor implements HandlerInterceptor{
    private static final String PUBLIC_KEY_FILE = "src/main/resources/public.key";
    private String publicKey;
    private static final String COOKIE_NAME = "jwt_token";

    public JwtInterceptor(){
        try{
            Path publicKeyPath = Paths.get(PUBLIC_KEY_FILE);
            publicKey = new String(Files.readAllBytes(publicKeyPath));
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception{
        Cookie[] cookies =  request.getCookies();
        String jwtToken = null;

        if(cookies != null){
            for(Cookie cookie : cookies){
                if(COOKIE_NAME.equals(cookie.getName())){
                    jwtToken = cookie.getValue();
                    break;
                }
            }
        }

        try{

            if(jwtToken != null){
                Claims claims = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(jwtToken).getBody();
                return true;
            }
        } catch (ExpiredJwtException e){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token has expired");
            return false;
        } catch (Exception e){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
            return false;
        }
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No valid token found");
        return false;
      }
    }


