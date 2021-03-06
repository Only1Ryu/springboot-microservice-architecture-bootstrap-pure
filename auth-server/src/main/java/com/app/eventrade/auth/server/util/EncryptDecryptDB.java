package com.app.eventrade.auth.server.util;

import java.security.Key;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.AttributeConverter;


public class EncryptDecryptDB implements AttributeConverter<String, String> {

	private static final String AES = "AES";
	private static final byte[] encryptionKey = "qa".getBytes();

	private final Cipher encryptCipher;
	private final Cipher decryptCipher;

	public EncryptDecryptDB() throws Exception {
		Key key = new SecretKeySpec(encryptionKey, AES);
		encryptCipher = Cipher.getInstance(AES);
		encryptCipher.init(Cipher.ENCRYPT_MODE, key);
		decryptCipher = Cipher.getInstance(AES);
		decryptCipher.init(Cipher.DECRYPT_MODE, key);
	}

	@Override
	public String convertToDatabaseColumn(String attribute) {
		try {
			if(attribute==null)
			{
				return null;
			}
			return Base64.getEncoder().encodeToString(encryptCipher.doFinal(attribute.getBytes()));
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			throw new IllegalArgumentException(e);
		}
	}

	@Override
	public String convertToEntityAttribute(String dbData) {
		try {
			if(dbData!=null)
			{
				return new String(decryptCipher.doFinal(Base64.getDecoder().decode(dbData)));
			}			
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			throw new IllegalArgumentException(e);
		}
		return dbData;
	}
}