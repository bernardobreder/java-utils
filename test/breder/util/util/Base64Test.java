package breder.util.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import breder.util.util.input.InputStreamUtil;

public class Base64Test {

	@Test
	public void test() throws IOException {
		byte[] bytes = new byte[1024 * 1024];
		new Random(System.currentTimeMillis()).nextBytes(bytes);
		String encode = Base64.encode(bytes);
		Assert.assertArrayEquals(bytes, Base64.decode(encode));
	}

	/**
	 * Testador
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		System.out.println(Base64.encode(InputStreamUtil.getBytes(new FileInputStream("/Users/bernardobreder/breder/eclipse.master/project/ofm_root/images/empty.png"))));
	}

}
