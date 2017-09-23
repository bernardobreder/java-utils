package breder.util;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import breder.util.io.storage.standard.BStorageTest;
import breder.util.util.Base64Test;

/**
 * Testador
 * 
 * @author bbreder
 */
@RunWith(Suite.class)
@SuiteClasses({ CryptographyTest.class, Base64Test.class, BStorageTest.class })
public class AllTest {

}
