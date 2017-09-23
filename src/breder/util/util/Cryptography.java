package breder.util.util;

import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * Criptografia de algumas chaves do arquivo de configura��o.
 */
public abstract class Cryptography {

  /** Algoritmo utilizado */
  private static final String ALGORITHM = "DESede";

  /**
   * Criptografa um texto
   * 
   * @param key chave de criptografia
   * @param original texto original que deseja criptografar
   * @return texto criptografado
   */
  public static String encrypt(byte[] key, String original) {
    try {
      Cipher cipher = getCipher(key, true);
      return getCipheredText(cipher, true, original);
    }
    catch (Exception e) {
      return original;
    }
  }

  /**
   * Descriptografa uma propriedade do arquivo de configura��o.
   * 
   * @param key chave de criptografia
   * @param encrypt texto criptografado
   * 
   * @return propriedade descriptografada.
   */
  public static String decrypt(byte[] key, String encrypt) {
    Cipher cipher;
    try {
      cipher = getCipher(key, false);
      return getCipheredText(cipher, false, encrypt);
    }
    catch (Exception e) {
      return encrypt;
    }
  }

  /**
   * Gera a chave de criptogtrafia.
   * 
   * @return chave
   * @throws GeneralSecurityException erro na gera��o da chave.
   * @throws IOException erro ao gerar o arquivo.
   */
  public static byte[] generateKey() throws GeneralSecurityException,
    IOException {
    KeyGenerator keygen = KeyGenerator.getInstance(ALGORITHM);
    SecretKey key = keygen.generateKey();
    SecretKeyFactory keyfactory = SecretKeyFactory.getInstance(ALGORITHM);
    DESedeKeySpec keyspec =
      DESedeKeySpec.class.cast(keyfactory.getKeySpec(key, DESedeKeySpec.class));
    return keyspec.getKey();
  }

  /**
   * Inicia as informa��es para criptografia dos dados.
   * 
   * @param key
   * 
   * @param encrypt indica se o texto deve ser criptografado ou
   *        descriptografado.
   * 
   * @return objeto que faz a criptografia.
   * 
   * @throws IOException erro na leitura de dados.
   * @throws GeneralSecurityException erro na gera��o da senha.
   */
  private static Cipher getCipher(byte[] key, boolean encrypt)
    throws IOException, GeneralSecurityException {
    DESedeKeySpec keyspec = new DESedeKeySpec(key);
    SecretKeyFactory keyfactory = SecretKeyFactory.getInstance(ALGORITHM);
    SecretKey secret = keyfactory.generateSecret(keyspec);
    Cipher cipher = Cipher.getInstance(ALGORITHM);
    cipher.init(encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, secret);
    return cipher;
  }

  /**
   * Obt�m o texto a ser apresentado como resultado da criptografia ou
   * descriptografia.
   * 
   * @param cipher objeto que faz a criptografia.
   * @param encryptKeys indica se o texto deve ser criptografado ou
   *        descriptografado.
   * @param text texto a ser criptografado ou descriptografado.
   * 
   * @return texto a ser apresentado como resultado da criptografia ou
   *         descriptografia.
   * 
   * @throws IOException erro na leitura de dados.
   * @throws GeneralSecurityException erro na gera��o da senha.
   */
  private static String getCipheredText(Cipher cipher, boolean encryptKeys,
    String text) throws GeneralSecurityException, IOException {
    if (encryptKeys) {
      return new BASE64Encoder().encode(cipher.doFinal(text.getBytes()));
    }
    else {
      return new String(cipher.doFinal(new BASE64Decoder().decodeBuffer(text)));
    }
  }

}
