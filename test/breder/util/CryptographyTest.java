package breder.util;

import junit.framework.Assert;

import org.junit.Test;

import breder.util.util.Cryptography;

/**
 * Testa a classe Chryptography
 * 
 * 
 * @author bbreder
 */
public class CryptographyTest {

	/**
	 * Testa uma vez
	 * 
	 * @throws Exception
	 */
	@Test
	public void run() throws Exception {
		byte[] key = Cryptography.generateKey();
		Assert.assertEquals("abc", Cryptography.decrypt(key, Cryptography.encrypt(key, "abc")));
	}

	/**
	 * Testa uma vez
	 * 
	 * @throws Exception
	 */
	@Test
	public void runDifKey() throws Exception {
		byte[] key = Cryptography.generateKey();
		String encrypt1 = Cryptography.encrypt(key, "abc");
		key = Cryptography.generateKey();
		String encrypt2 = Cryptography.encrypt(key, "abc");
		Assert.assertFalse(encrypt1.equals(encrypt2));
	}

	/**
	 * Testa uma vez
	 * 
	 * @throws Exception
	 */
	@Test
	public void runWrong() throws Exception {
		byte[] key = Cryptography.generateKey();
		Assert.assertFalse("cba".equals(Cryptography.decrypt(key, Cryptography.encrypt(key, "abc"))));
		Assert.assertFalse("bac".equals(Cryptography.decrypt(key, Cryptography.encrypt(key, "abc"))));
		Assert.assertEquals(3, Cryptography.decrypt(key, Cryptography.encrypt(key, "abc")).length());
		Assert.assertFalse(Cryptography.decrypt(key, Cryptography.encrypt(key, "cba")).equals(Cryptography.decrypt(key, Cryptography.encrypt(key, "abc"))));
		Assert.assertFalse(Cryptography.decrypt(key, Cryptography.encrypt(key, "bac")).equals(Cryptography.decrypt(key, Cryptography.encrypt(key, "abc"))));
	}

	/**
	 * Testa varias vezes
	 * 
	 * @throws Exception
	 */
	@Test
	public void runManyTimes() throws Exception {
		byte[] key = Cryptography.generateKey();
		for (int n = 0; n < 8 * 1024; n++) {
			Assert.assertEquals("abc", Cryptography.decrypt(key, Cryptography.encrypt(key, "abc")));
		}
	}

	/**
	 * Testa varias vezes com chaves diferentes
	 * 
	 * @throws Exception
	 */
	@Test
	public void runManyTimesDifKey() throws Exception {
		for (int n = 0; n < 1024; n++) {
			byte[] key = Cryptography.generateKey();
			Assert.assertEquals("abc", Cryptography.decrypt(key, Cryptography.encrypt(key, "abc")));
		}
	}

}
