
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class HMACTools {

	private static String HMAC_ALGORITHM = "HmacSHA1";
	
	private static SecretKey getMacKey(String secret) {
		try {	
			return new SecretKeySpec( secret.getBytes("ASCII"), HMAC_ALGORITHM);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String getBase64EncodedSignature(String secret, String signingData) {
		
		SecretKey key = getMacKey(secret);
		try {
			Mac mac = Mac.getInstance(key.getAlgorithm());
			mac.init(key);
		    byte[] digest = mac.doFinal(signingData.getBytes("UTF8"));
		    return new String(Base64.encodeBase64(digest),"ASCII");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (InvalidKeyException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	public static boolean verifyBase64EncodedSignature(String secret, String sig, String signedData) {
		
		if(secret == null || sig == null || signedData == null) return false;
		
		SecretKey key = getMacKey(secret);
		try {
			Mac mac = Mac.getInstance(key.getAlgorithm());
			mac.init(getMacKey(secret));
		    byte[] digest = mac.doFinal(signedData.getBytes("UTF8"));
		    return sig.equals(new String(Base64.encodeBase64(digest),"ASCII"));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (InvalidKeyException e) {
			throw new RuntimeException(e);
		}
	}
}
