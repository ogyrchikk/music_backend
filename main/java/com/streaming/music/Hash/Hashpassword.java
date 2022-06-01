package com.streaming.music.Hash;


import net.bytebuddy.implementation.bytecode.ShiftRight;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class Hashpassword {
    public String getSold(String login) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedhash = digest.digest(
                login.getBytes(StandardCharsets.UTF_8));
        return new String(encodedhash);
    }
    public String getHashOfPassword(String sold,String pass) throws NoSuchAlgorithmException {
        String passSold =sold+pass;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedhash = digest.digest(
                passSold.getBytes(StandardCharsets.UTF_8));
        return new String(encodedhash);
    }
}
