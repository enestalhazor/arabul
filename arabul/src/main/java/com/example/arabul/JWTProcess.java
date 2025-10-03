package com.example.arabul;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

public class JWTProcess {

    private static String SECRET = "al39593vknskcksof02qrkgm";

    public static String jwtCreate(String email, Integer id) {

        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        String token = JWT.create()
                .withIssuer("user")
                .withClaim("email", email)
                .withClaim("id", id)
                .sign(algorithm);

        return token;
    }

}
