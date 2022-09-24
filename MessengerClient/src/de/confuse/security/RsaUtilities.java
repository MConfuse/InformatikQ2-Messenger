package de.confuse.security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

/**
 * Offers basically everything for de- and encryption using the RSA Algorithm. Also allows you to save keys to and read
 * them from files.
 *
 * @author Confuse
 * @version 1, 30.08.2022
 */
public final class RsaUtilities
{
	/**
	 * Generates a {@link KeyPair} using the RSA encryption algorithm. Uses a default of 2048 bits for the keys'
	 * length.
	 *
	 * @return A new keypair.
	 */
	public static KeyPair generateRsaKeyPair()
	{
		return generateRsaKeyPair(2048);
	}

	/**
	 * Generates a {@link KeyPair} using the RSA encryption algorithm.
	 *
	 * @param keySize The key-size, an algorithm-specific metric which determines things such as modulus length,
	 *                specified in number of bits.
	 * @return A new keypair.
	 */
	public static KeyPair generateRsaKeyPair(int keySize)
	{
		try
		{
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
			generator.initialize(keySize);
			return generator.generateKeyPair();
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * Encrypts a given {@link String} using the RSA-Algorithm and the {@link PublicKey}.
	 *
	 * @param publicKey The public key to use for encryption
	 * @param message   The message to encrypt
	 * @return A byte array with somewhat unknown size, containing the encrypted Message
	 * @throws NoSuchAlgorithmException If thrown, you do not support RSA encryption on your machine.
	 * @throws InvalidKeyException      Thrown if the given key is wrong for any reason.
	 * @see #decryptBytesWithRsaToString(PrivateKey, byte[])
	 */
	public static byte[] encryptStringWithRsa(PublicKey publicKey, final String message) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
	{
		Cipher encryptCipher = Cipher.getInstance("RSA");
		encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);

		return encryptCipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * Encrypts a given {@link String} using the RSA-Algorithm and the {@link PublicKey}.
	 *
	 * @param publicKey The public key to use for encryption
	 * @param message   The message to encrypt
	 * @return A byte array with somewhat unknown size, containing the encrypted Message
	 * @throws NoSuchAlgorithmException If thrown, you do not support RSA encryption on your machine.
	 * @throws InvalidKeyException      Thrown if the given key is wrong for any reason.
	 * @see #decryptBytesWithRsaToString(PrivateKey, byte[])
	 */
	public static byte[] encryptStringWithRsa(PublicKey publicKey, final byte[] message) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
	{
		Cipher encryptCipher = Cipher.getInstance("RSA");
		encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);

		return encryptCipher.doFinal(message);
	}

	/**
	 * Decrypts the given byte array to a String using the matching {@link PrivateKey} .
	 *
	 * @param privateKey The private key to use for decryption
	 * @param bytes      The byte array representing the encrypted String
	 * @return The decrypted String
	 * @throws NoSuchAlgorithmException If thrown, you do not support RSA encryption on your machine.
	 * @throws InvalidKeyException      Thrown if the given key is wrong for any reason.
	 * @see #encryptStringWithRsa(PublicKey, String)
	 */
	public static String decryptBytesWithRsaToString(PrivateKey privateKey, final byte[] bytes) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
	{
		Cipher decryptCipher = Cipher.getInstance("RSA");
		decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);

		return new String(decryptCipher.doFinal(bytes), StandardCharsets.UTF_8);
	}

	/**
	 * Decrypts the given byte array to a String using the matching {@link PrivateKey} .
	 *
	 * @param privateKey The private key to use for decryption
	 * @param bytes      The byte array representing the encrypted String
	 * @return The decrypted String
	 * @throws NoSuchAlgorithmException If thrown, you do not support RSA encryption on your machine.
	 * @throws InvalidKeyException      Thrown if the given key is wrong for any reason.
	 * @see #encryptStringWithRsa(PublicKey, String)
	 */
	public static byte[] decryptBytesWithRsaToByteArray(PrivateKey privateKey, final byte[] bytes) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
	{
		Cipher decryptCipher = Cipher.getInstance("RSA");
		decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);

		return decryptCipher.doFinal(bytes);
	}

	/**
	 * Saves the given key, private or public, to the given file.<br> WARNING: Overrides the file to only contain the
	 * key!
	 *
	 * @param key  The key to save
	 * @param file The file to save the key to
	 * @throws IOException Thrown when a common IO Exception occurs, f. ex. unreachable path, missing permission etc.
	 * @see #readPublicKeyFromFile(File)
	 * @see #readPrivateKeyFromFile(File)
	 */
	public static void saveKeyToFile(Key key, File file) throws IOException
	{
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(key.getEncoded());
	}

	/**
	 * Reads a {@link PublicKey} from a file.<br> Requires the {@link File} to only contain the key binaries!
	 *
	 * @param file The file to read from
	 * @return The extracted public key
	 * @throws InvalidKeySpecException If the given key specification is inappropriate for this RSA key factory to
	 *                                 produce a public key.
	 * @see #saveKeyToFile(Key, File)
	 * @see #readPrivateKeyFromFile(File)
	 */
	public static PublicKey readPublicKeyFromFile(File file) throws IOException, InvalidKeySpecException
	{
		try
		{
			byte[] publicKeyBytes = Files.readAllBytes(file.toPath());
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
			return keyFactory.generatePublic(publicKeySpec);
		}
		catch (NoSuchAlgorithmException e)
		{ // Incredibly unlikely to occur
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * Reads a {@link PublicKey} from a file.<br> Requires the {@link File} to only contain the key binaries!
	 *
	 * @param bytes The bytes to read the key from
	 * @return The extracted public key
	 * @throws InvalidKeySpecException If the given key specification is inappropriate for this RSA key factory to
	 *                                 produce a public key.
	 * @see #readPrivateKeyFromFile(File)
	 */
	public static PublicKey readPublicKeyFromBytes(byte[] bytes) throws InvalidKeySpecException
	{
		try
		{
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(bytes);
			return keyFactory.generatePublic(publicKeySpec);
		}
		catch (NoSuchAlgorithmException e)
		{ // Incredibly unlikely to occur
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * Reads a {@link PrivateKey} from a file.<br> Requires the {@link File} to only contain the key binaries!
	 *
	 * @param file The file to read from
	 * @return The extracted private key
	 * @throws InvalidKeySpecException If the given key specification is inappropriate for this RSA key factory to
	 *                                 produce a private key.
	 */
	public static PrivateKey readPrivateKeyFromFile(File file) throws IOException, InvalidKeySpecException
	{
		try
		{
			byte[] privateKeyBytes = Files.readAllBytes(file.toPath());
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			EncodedKeySpec privateKeySpec = new X509EncodedKeySpec(privateKeyBytes);
			return keyFactory.generatePrivate(privateKeySpec);
		}
		catch (NoSuchAlgorithmException e)
		{ // Incredibly unlikely to occur
			e.printStackTrace();
			return null;
		}

	}
}
