package de.confuse.util;

import de.confuse.confFileV2.ConfFileFieldV2;
import de.confuse.security.AesUtilities;
import de.confuse.security.RsaUtilities;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.Destroyable;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Objects;

public class CryptoCommunication
{

	public static final String CONNECTION_ESTABLISHED = "Connection Established!";

	/**
	 * Generates a crypto packet that encrypts the input string using the {@value AesUtilities#AES_ALGORITHM} algorithm
	 * using the given RSA-{@link PublicKey}, AES-{@link SecretKey} and {@link IvParameterSpec} of the given secret
	 * key.
	 *
	 * @param message         The string to encrypt
	 * @param publicKey       The public key to encrypt with
	 * @param secretKey       The unencrypted secret key
	 * @param ivParameterSpec The unencrypted initialization vector
	 * @return a formatted crypto packet
	 *
	 * @see RsaUtilities#generateRsaKeyPair()
	 * @see AesUtilities#generateSecretKey(int)
	 * @see AesUtilities#generateIv()
	 */
	public static ConfFileFieldV2 generateCryptoPacket(String message, PublicKey publicKey, SecretKey secretKey,
													   IvParameterSpec ivParameterSpec)
	{
		final ConfFileFieldV2 field = new ConfFileFieldV2("CryptoCommunication", true);
		try
		{
			field.put("iv", Base64.getEncoder().encodeToString(RsaUtilities.encryptStringWithRsa(publicKey,
					ivParameterSpec.getIV()))).put("content", AesUtilities.encryptAsString(message, secretKey,
					ivParameterSpec));
			return field;
		}
		catch (NoSuchPaddingException | InvalidKeyException | NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e)
		{
			System.err.println("[CryptoCommunication-Generation Warning] Couldn't package the data, returning null!");
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * Generates a new handshake that is sent by the client to the server to let the server know your public key.
	 *
	 * @param publicKey The public key to send
	 * @return a formatted handshake packet
	 *
	 * @see RsaUtilities#generateRsaKeyPair()
	 */
	public static ConfFileFieldV2 generateCryptoHandshakeStage1(PublicKey publicKey)
	{
		final ConfFileFieldV2 field = new ConfFileFieldV2("CryptoHandshake", true);
		field.put("rsa", Base64.getEncoder().encodeToString(publicKey.getEncoded())).put("stage", "1");
		return field;

	}

	/**
	 * Generates a new handshake that tells the client the servers {@link PublicKey}, as well as the {@link SecretKey}
	 * (AES-Key) and the {@link IvParameterSpec} that will be used.
	 *
	 * @param clientPublicKey The public key of the client
	 * @param serverPublicKey The public key to send
	 * @param secretKey       The unencrypted secret key
	 * @param ivParameterSpec The unencrypted initialization vector
	 * @return a formatted handshake packet
	 *
	 * @see RsaUtilities#generateRsaKeyPair()
	 * @see AesUtilities#generateSecretKey(int)
	 * @see AesUtilities#generateIv()
	 */
	public static ConfFileFieldV2 generateCryptoHandshakeStage2(PublicKey clientPublicKey, PublicKey serverPublicKey,
																SecretKey secretKey, IvParameterSpec ivParameterSpec)
	{
		final ConfFileFieldV2 field = new ConfFileFieldV2("CryptoHandshake", true);
		try
		{
			field.put("rsa", Base64.getEncoder().encodeToString(serverPublicKey.getEncoded())).put("secret",
					Base64.getEncoder().encodeToString(RsaUtilities.encryptStringWithRsa(clientPublicKey,
							secretKey.getEncoded()))).put("iv",
					Base64.getEncoder().encodeToString(RsaUtilities.encryptStringWithRsa(clientPublicKey,
							ivParameterSpec.getIV()))).put("stage", "2");
			return field;
		}
		catch (NoSuchPaddingException | InvalidKeyException | NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException e)
		{
			System.err.println("[CryptoHandshake-Generation Warning] Couldn't package the data, returning null!");
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * Generates a new handshake that tells the server that everything worked out and that the client is now able to
	 * use
	 * the normal communication now.<br> You should use the data you received from the server for this.
	 *
	 * @param publicKey       The public key to send
	 * @param secretKey       The unencrypted secret key
	 * @param ivParameterSpec The unencrypted initialization vector
	 * @return a formatted handshake packet
	 *
	 * @see RsaUtilities#generateRsaKeyPair()
	 * @see AesUtilities#generateSecretKey(int)
	 * @see AesUtilities#generateIv()
	 */
	public static ConfFileFieldV2 generateCryptoHandshakeStage3(PublicKey publicKey, SecretKey secretKey,
																IvParameterSpec ivParameterSpec)
	{
		final ConfFileFieldV2 field = new ConfFileFieldV2("CryptoHandshake", true);
		try
		{
			field.put("iv", Base64.getEncoder().encodeToString(RsaUtilities.encryptStringWithRsa(publicKey,
					ivParameterSpec.getIV()))).put("content", AesUtilities.encryptAsString(CONNECTION_ESTABLISHED,
					secretKey, ivParameterSpec)).put("stage", "3");
			return field;
		}
		catch (NoSuchPaddingException | InvalidKeyException | NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e)
		{
			System.err.println("[CryptoHandshake-Generation Warning] Couldn't package the data, returning null!");
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * Received by the server
	 *
	 * @param cryptoCommunication The received stage 3 packet
	 * @return true if everything worked correctly, false if otherwise.
	 */
	public static PublicKey extractHandshakeDataStage1(ConfFileFieldV2 cryptoCommunication) throws InvalidKeySpecException
	{
		return RsaUtilities.readPublicKeyFromBytes(Base64.getDecoder().decode(cryptoCommunication.getValue("rsa")));
	}

	/**
	 * Decrypts and stores the Stage 2 handshake data from the server.<br> You have to manually make sure that you
	 * actually receive a stage 2 packet.
	 *
	 * @param cryptoPacket A {@link ConfFileFieldV2} with the specific CryptoCommunication layout and content
	 * @param privateKey   Your {@link PrivateKey} counterpart to the {@link PublicKey} that was used to encrypt the
	 *                     CryptoCommunication
	 * @return the extracted handshake data from the server in form of a {@link CryptoStorage} Object or null if the
	 * extraction failed.
	 *
	 * @throws InvalidKeyException Thrown when your {@link PrivateKey} was not the counterpart to the {@link PublicKey}
	 *                             that was used to encrypt the packet.
	 * @see #generateCryptoHandshakeStage2(PublicKey, PublicKey, SecretKey, IvParameterSpec)
	 * @see RsaUtilities#generateRsaKeyPair()
	 */
	public static CryptoStorage extractHandshakeDataStage2(ConfFileFieldV2 cryptoPacket, PrivateKey privateKey) throws InvalidKeyException
	{
		if (!cryptoPacket.getName().equals("CryptoHandshake"))
		{
			System.err.println("[CryptoHandshake-Validation Warning] Given ConfFileField was not a CryptoHandshake, " +
					"returning null!");
			return null;
		}

		// Empty variables, filled below
		byte[] receivedRsaKey;
		byte[] receivedSecretKey;
		byte[] receivedIv;

		try
		{ // Try filling in the variables, if a null pointer occurs it's not a CryptoCommunication
			receivedRsaKey = Base64.getDecoder().decode(cryptoPacket.getValue("rsa").getBytes(StandardCharsets.UTF_8));
			receivedSecretKey =
					Base64.getDecoder().decode(cryptoPacket.getValue("secret").getBytes(StandardCharsets.UTF_8));
			receivedIv = Base64.getDecoder().decode(cryptoPacket.getValue("iv").getBytes(StandardCharsets.UTF_8));
		}
		catch (NullPointerException e)
		{
			System.err.println("[CryptoHandshake-Validation Warning] Given ConfFileField was not a CryptoHandshake, " +
					"returning null!");
			e.printStackTrace();
			return null;
		}

		try
		{
			final byte[] rawSecretKey = RsaUtilities.decryptBytesWithRsaToByteArray(privateKey, receivedSecretKey);
			final byte[] rawIv = RsaUtilities.decryptBytesWithRsaToByteArray(privateKey, receivedIv);
			final SecretKey decodedSecretKey = new SecretKeySpec(rawSecretKey, 0, rawSecretKey.length, "AES");
			// just in case the key is invalid and is therefore not an actual or faulty CryptoPacket
			final IvParameterSpec decodedIvParameterSpec = new IvParameterSpec(rawIv, 0, rawIv.length);

			return new CryptoStorage(decodedSecretKey, RsaUtilities.readPublicKeyFromBytes(receivedRsaKey),
					decodedIvParameterSpec);
		}
		catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeySpecException e)
		{
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * Received by the server and checks, if the client confirms the connection and encryption.
	 *
	 * @param cryptoCommunication The received stage 3 packet
	 * @param cryptoStorage       The stored data from the client
	 * @param privateKey          Your private key to decrypt eve
	 * @return true if everything worked correctly, false if otherwise.
	 */
	public static boolean extractHandshakeDataStage3(ConfFileFieldV2 cryptoCommunication, CryptoStorage cryptoStorage,
													 PrivateKey privateKey)
	{
		return Objects.equals(decryptReceivedMessage(cryptoCommunication, cryptoStorage, privateKey),
				CONNECTION_ESTABLISHED);
	}

	/**
	 * Decrypts the contents of a CryptoPacket using the received data from the handshake in form of the {@link
	 * CryptoStorage}-Object and your {@link PrivateKey}.
	 *
	 * @param cryptoCommunication The received {@link ConfFileFieldV2}
	 * @param cryptoStorage       The handshake data
	 * @param privateKey          Your RSA private key
	 * @return the actual data sent in form of a string.
	 *
	 * @see #generateCryptoPacket(String, PublicKey, SecretKey, IvParameterSpec)
	 * @see #generateCryptoHandshakeStage2(PublicKey, PublicKey, SecretKey, IvParameterSpec)
	 * @see RsaUtilities#generateRsaKeyPair()
	 */
	public static String decryptReceivedMessage(ConfFileFieldV2 cryptoCommunication, CryptoStorage cryptoStorage,
												PrivateKey privateKey)
	{
		try
		{
			byte[] receivedIvBytes =
					Base64.getDecoder().decode(cryptoCommunication.getValue("iv").getBytes(StandardCharsets.UTF_8));
			receivedIvBytes = RsaUtilities.decryptBytesWithRsaToByteArray(privateKey, receivedIvBytes);
			String receivedMessage = cryptoCommunication.getValue("content");

			return AesUtilities.decrypt(receivedMessage, cryptoStorage.secretKey,
					new IvParameterSpec(receivedIvBytes));
		}
		catch (NullPointerException e)
		{
			System.err.println("[CryptoCommunication-Decryption Warning] Given ConfFileField was not a CryptoPacket, " +
					"returning null!");
			e.printStackTrace();
			return null;
		}
		catch (InvalidAlgorithmParameterException | NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e)
		{
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * Stores the keys for later usage.
	 */
	public static class CryptoStorage implements Destroyable
	{
		public SecretKey secretKey;
		public PublicKey serverPublicKey;
		public PrivateKey serverPrivateKey;
		public IvParameterSpec ivParameterSpec;

		public CryptoStorage(SecretKey secretKey, PublicKey serverPublicKey, IvParameterSpec ivParameterSpec)
		{
			this.secretKey = secretKey;
			this.serverPublicKey = serverPublicKey;
			this.serverPrivateKey = null;
			this.ivParameterSpec = ivParameterSpec;
		}

		public CryptoStorage(SecretKey clientSecretKey, PublicKey clientPublicKey, PrivateKey clientPrivateKey,
							 IvParameterSpec clientIvParameterSpec)
		{
			this.secretKey = clientSecretKey;
			this.serverPublicKey = clientPublicKey;
			this.serverPrivateKey = clientPrivateKey;
			this.ivParameterSpec = clientIvParameterSpec;
		}

		@Override
		public String toString()
		{
			return "CryptoStorage{" +
					"secretKey=" + secretKey +
					", serverPublicKey=" + serverPublicKey +
					", serverPrivateKey=" + serverPrivateKey +
					", ivParameterSpec=" + ivParameterSpec +
					'}';
		}
	}

}
