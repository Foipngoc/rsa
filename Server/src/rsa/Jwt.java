package rsa;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

/**
 * Created by Administrator on 2018/1/16.
 */
public class Jwt {
    final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    final String ISSUER="BeiChenServer";
    final String guid="19c8dae9-5ff0-4346-ad3c-28cac4867bb9";
    final SecretKey secretKey =new SecretKeySpec((guid).getBytes(), 0, (guid).getBytes().length, "AES");
    public String generateToken(String username)
    {
        JwtBuilder builder= Jwts.builder()
                .setAudience(username)
                .setIssuedAt(new Date())
                .setIssuer(ISSUER)
                .signWith(signatureAlgorithm,secretKey);
        return builder.compact();
    }

    public Claims parseToken(String token)
    {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token).getBody();
    }
}
