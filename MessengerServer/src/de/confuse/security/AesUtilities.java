package de.confuse.security;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class AesUtilities
{
	//	private static final String AES_ALGORITHM = "AES/GCM/NoPadding";
	public static final String AES_ALGORITHM = "AES/OFB/NoPadding";
	//	private static final String AES_ALGORITHM = "AES/CBC/PKCS5Padding";

	/**
	 * Generates a new AES-{@link SecretKey}
	 *
	 * @param keySize The size of the key in bits, should be one of 128, 192, 256
	 * @return the secret key.
	 * @throws NoSuchAlgorithmException Thrown if your JVM does not support {@value AES_ALGORITHM}
	 */
	public static SecretKey generateSecretKey(int keySize) throws NoSuchAlgorithmException
	{
		// keySize(128, 192, 256)
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		keyGenerator.init(keySize);
		return keyGenerator.generateKey();
	}

	/**
	 * Generates a {@link SecretKey} from a password using the given salt (should be a random value!)
	 *
	 * @param password The passwort to derive from
	 * @param salt     The random string to salt the password with
	 * @return the secret key.
	 * @throws NoSuchAlgorithmException if your JVM does not support "PBKDF2WithHmacSHA256"
	 */
	public static SecretKey getKeyFromPassword(String password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException
	{
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
		return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
	}

	/**
	 * @return a new and random {@link IvParameterSpec}.
	 */
	public static IvParameterSpec generateIv()
	{
		byte[] iv = new byte[16];
		new SecureRandom().nextBytes(iv);
		return new IvParameterSpec(iv);
	}

	/**
	 * Encrypts the given string with {@value AES_ALGORITHM}
	 *
	 * @param input The string to encrypt
	 * @param key   The secret key to use
	 * @param iv    The initialization vector of the key
	 * @return a {@link Base64} encoded, encrypted string.
	 * @throws InvalidKeyException if the key/iv was invalid
	 */
	public static String encryptAsString(String input, SecretKey key, IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException
	{
		return Base64.getEncoder().encodeToString(encryptAsBytes(input, key, iv));
	}

	/**
	 * Encrypts the given string with {@value AES_ALGORITHM}
	 *
	 * @param input The string to encrypt
	 * @param key   The secret key to use
	 * @param iv    The initialization vector of the key
	 * @return the raw bytes of the input, encrypted.
	 * @throws InvalidKeyException if the key/iv was invalid
	 */
	public static byte[] encryptAsBytes(String input, SecretKey key, IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException
	{
		Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, key, iv);
		return cipher.doFinal(input.getBytes());
	}

	/**
	 * Decrypts the ciphertext
	 *
	 * @param cipherText The string to decrypt
	 * @param key        The private key to use
	 * @param iv         The initialization vector of the key
	 * @return the decrypted data as a new string.
	 * @throws InvalidKeyException if the key/iv was invalid
	 */
	public static String decrypt(String cipherText, SecretKey key, IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException
	{
		Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, key, iv);
		byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText));
		return new String(plainText);
	}

}
