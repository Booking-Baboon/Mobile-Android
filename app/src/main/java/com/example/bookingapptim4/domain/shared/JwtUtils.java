package com.example.bookingapptim4.domain.shared;

import android.util.Base64;
import android.util.Log;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.UnsupportedEncodingException;



public class JwtUtils {


    public static String decoded(String JWTEncoded) throws Exception {
        try {
            String[] split = JWTEncoded.split("\\.");
            Log.d("JWT_DECODED", "Header: " + getJson(split[0]));
            Log.d("JWT_DECODED", "Body: " + getJson(split[1]));
            return getJson(split[1]);
        } catch (UnsupportedEncodingException e) {
            //Error
            return null;
        }
    }

    private static String getJson(String strEncoded) throws UnsupportedEncodingException{
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, "UTF-8");
    }



    public static String getRole(String jwtToken) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(decoded(jwtToken));

            JsonNode roleNode = rootNode.path("role");

            if (roleNode.isArray() && roleNode.size() > 0) {
                JsonNode authorityNode = roleNode.get(0).path("authority");

                if (authorityNode.isTextual()) {
                    String authorityValue = authorityNode.asText();

                    System.out.println("Authority: " + authorityValue);
                    return authorityValue;


                } else {
                    System.out.println("No 'authority' field found or it's not a text node.");
                }
            } else {
                System.out.println("No 'role' array found or it's empty.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
