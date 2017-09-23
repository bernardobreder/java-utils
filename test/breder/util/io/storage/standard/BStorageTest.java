package breder.util.io.storage.standard;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import breder.util.io.storage.IStorage;
import breder.util.io.storage.IStorageSession;

/**
 * Testador do Storage
 * 
 * 
 * @author Bernardo Breder
 */
public class BStorageTest {

	/**
	 * Teste
	 * 
	 * @throws IOException
	 */
	@Test
	public void test() throws IOException {
		IStorage storage = IStorage.STANDARD;
		IStorageSession session = storage.getStorage(new File("test/breder/util/io/storage/standard"));
		session.start();
	}

}
