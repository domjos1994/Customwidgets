/*
 * Copyright (C) 2017-2020  Dominic Joas
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 3
 *  of the License, or (at your option) any later version.
 */

package de.domjos.customwidgets.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Crypto {
    private byte[] salt;
    private AlgorithmParameterSpec parameterSpec;
    private Key keySpec;

    public Crypto(Context context, String password) throws InvalidKeySpecException, NoSuchAlgorithmException {
        SharedPreferences preferences = context.getSharedPreferences("dBall", Context.MODE_PRIVATE);
        String content = preferences.getString("salt", "");
        if (content.isEmpty()) {
            this.salt = this.generateNewSalt();
            preferences.edit().putString("salt", Base64.encodeToString(this.salt, Base64.NO_WRAP)).apply();
        } else {
            this.salt = Base64.decode(content, Base64.NO_WRAP);
        }
        this.keySpec = this.generateKey(password);

        String ivString = preferences.getString("iv", "");
        byte[] iv;
        if (ivString.isEmpty()) {
            iv = this.generateNewIv();
            preferences.edit().putString("iv", Base64.encodeToString(iv, Base64.NO_WRAP)).apply();
        } else {
            iv = Base64.decode(ivString, Base64.NO_WRAP);
        }
        this.parameterSpec = new IvParameterSpec(iv);
    }

    public String encryptString(String message) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        cipher.init(Cipher.ENCRYPT_MODE, this.keySpec, this.parameterSpec);
        return Base64.encodeToString(cipher.doFinal(message.getBytes()), Base64.NO_WRAP);
    }

    public String decryptString(String message) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, this.parameterSpec);
        return new String(cipher.doFinal(Base64.decode(message, Base64.NO_WRAP)), StandardCharsets.UTF_8);
    }

    private Key generateKey(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(), this.salt, 1324, 256);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] keyBytes = secretKeyFactory.generateSecret(pbeKeySpec).getEncoded();
        return new SecretKeySpec(keyBytes, "AES");
    }

    private byte[] generateNewIv() {
        SecureRandom random = new SecureRandom();
        byte[] iv = new byte[16];
        random.nextBytes(iv);
        return iv;
    }

    private byte[] generateNewSalt() {
        byte[] salt = new byte[256];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        return salt;
    }
}
