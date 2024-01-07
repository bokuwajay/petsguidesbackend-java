package live.codeland.petsguidesbackend.config;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private static final PrivateKey privateKey = loadPrivateKey("src/main/resources/private_key.der");
    private static final PublicKey publicKey = loadPublicKey("src/main/resources/public_key.der");

//    private static final String SECRET_KEY = "1db849ae1d1ebcb897fc7b6a7afcc50f1e3aa42471ca7741eace0c4ae0b93621";

    private static PrivateKey loadPrivateKey(String filePath) {
        try {

            byte[] keyBytes = Files.readAllBytes(Paths.get(filePath));
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            throw new RuntimeException("Error loading private key", e);
        }
    }

    private static PublicKey loadPublicKey(String filePath) {
        try {
            byte[] keyBytes = Files.readAllBytes(Paths.get(filePath));
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            throw new RuntimeException("Error loading public key", e);
        }
    }


    // Claims::getSubject is a shorthand lambda expression of
    // token -> { return token.getSubject() }
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // extracting 1 single claim (payload data) from a JWT
    public <T> T extractClaim(String token, Function<Claims, T> claimResolver){
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }

    // the extractClaims method responsible for extracting the claims (payload data) from a JWT and returning them as a Map<String, Object>
    //Each claim in the JWT typically corresponds to a key-value pair in the Map
    public String generateToken(Map<String, Object> extractClaims, UserDetails userDetails){
        return Jwts.builder().setClaims(extractClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24 ))
                .signWith(privateKey)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    // extracting all claims (payload data) from JWT
    // this is also a process of verifying the token
    private Claims extractAllClaims(String token){
//        try{
//            return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return null;

        return Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(token).getBody();
    }

//    private Key getSignInKey() {
//
//        // the SECRET_KEY is base64 format, this is to convert it to raw bytes form (which is binary data of SECRET_KEY)
//        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
//        // use the keyBytes with HMAC algorithm to create a digital signature
//        return Keys.hmacShaKeyFor(keyBytes);
//
//    }

}
