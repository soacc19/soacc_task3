package SisuBeta.SisuRS.token;

import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;

import SisuBeta.SisuRS.exceptions.InternalException;

public class JWTService {
    private static RsaJsonWebKey rsaJsonWebKey = null;
    private static final String ISSUER = "SisuBeta";
    private static final String AUDIENCE = "www.jyu.fi";
    
    // Generate an RSA key pair, which will be used for signing and verification of the JWT, wrapped in a JWK
    // Has to be static to keep RSA key pair
    static {
        try {
            rsaJsonWebKey = RsaJwkGenerator.generateJwk(2048);
        } catch (JoseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public JWTService() {}
    
    public static String buildToken(String username) {
        String jwt = null;
        
        if (rsaJsonWebKey == null) {
            throw new InternalException("Internal error during token creation!", "");
        }
        
        System.out.println("DEBUG: rsaJsonWebKey " + rsaJsonWebKey.toJson());
        
        JwtClaims claims = new JwtClaims();
        claims.setSubject(username);
        claims.setIssuer(ISSUER);
        claims.setAudience(AUDIENCE);
        claims.setExpirationTimeMinutesInTheFuture(10);
        claims.setGeneratedJwtId();
        claims.setIssuedAtToNow();
        claims.setNotBeforeMinutesInThePast(2);
        // we are providing only one role per user and it's retrieved from DB
        //claims.setStringListClaim("roles", user.getRolesList());
        
        
//        FIXME hardcoded values for only testing - 
//        NumericDate nd = NumericDate.fromSeconds(1570287089);
//        claims.setIssuedAt(nd);
//        nd = NumericDate.fromSeconds(1601823089);
//        claims.setExpirationTime(nd);

        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setKey(rsaJsonWebKey.getPrivateKey());
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);

        try {
            jwt = jws.getCompactSerialization();
        } 
        catch (JoseException e) {
            throw new InternalException("Internal error during token creation!", e.getMessage());
        }
        
        return jwt;
    }
    
    public static String verifyToken(String jwt) throws InvalidJwtException, MalformedClaimException {
        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setRequireExpirationTime()                 // the JWT must have an expiration time
                .setMaxFutureValidityInMinutes(360)         // but the  expiration time should't be too big
                .setAllowedClockSkewInSeconds(30)           // allow some leeway in validating time based claims to account for clock skew
                .setExpectedIssuer(ISSUER)                  // whom the JWT needs to have been issued by
                .setExpectedAudience(AUDIENCE)              // recipients of JWT
                .setVerificationKey(rsaJsonWebKey.getKey()) // verify the signature with the public key
                .build();
        
        //  Validate the JWT and process it to the Claims
        JwtClaims jwtClaims = jwtConsumer.processToClaims( jwt );
        
        System.out.println( "DEBUG: JWT validation success! " + jwtClaims ); 
        
        return jwtClaims.getSubject();
    }

}
