package breder.util.sql;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

import junit.framework.Assert;

import org.junit.Test;

import breder.util.sql.driver.IDBDriver;

public class DefaultConnectionPoolTest {

	@Test(expected = Error.class)
	public void testClassNotExist() throws Exception {
		FakeConnectionPool pool = new FakeConnectionPool(new FakeDBDriver(""), 2);
		Connection c = pool.get();
		Assert.assertTrue(c == pool.get());
		Assert.assertNotNull(c);
		pool.free();
	}

	@Test
	public void testGet() throws Exception {
		FakeConnectionPool pool = new FakeConnectionPool(2);
		Connection c = pool.get();
		Assert.assertTrue(c == pool.get());
		Assert.assertNotNull(c);
		pool.free();
	}

	@Test
	public void testInterrupt() throws Exception {
		Thread thread = new Thread() {
			public void run() {
				try {
					FakeConnectionPool pool = new FakeConnectionPool(0);
					Thread.currentThread().interrupt();
					Connection c = pool.get();
					Assert.assertNull(c);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		};
		thread.start();
		thread.join();
	}

	@Test
	public void testFree() throws Exception {
		FakeConnectionPool pool = new FakeConnectionPool(2);
		pool.get();
		pool.free();
		pool.get();
		pool.free();
	}

	@Test
	public void testTimeout() throws Exception {
		FakeConnectionPool pool = new FakeConnectionPool(2);
		pool.setTimeout(100);
		Assert.assertEquals(100, pool.getTimeout());
		pool.get();
		pool.free();
		Thread.sleep(100);
		pool.get();
		pool.free();
	}

	@Test
	public void test3Threads() throws Exception {
		final FakeConnectionPool pool = new FakeConnectionPool(2);
		final Connection[] cs = new Connection[3];
		Thread thread1 = new Thread() {
			public void run() {
				try {
					Connection c = pool.get();
					Assert.assertTrue(c == pool.get());
					Assert.assertNotNull(c);
					cs[0] = c;
					Thread.sleep(100);
					pool.free();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		};
		Thread thread2 = new Thread() {
			public void run() {
				try {
					Connection c = pool.get();
					Assert.assertTrue(c == pool.get());
					Assert.assertNotNull(c);
					cs[1] = c;
					Thread.sleep(100);
					pool.free();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		};
		Thread thread3 = new Thread() {
			public void run() {
				try {
					Connection c = pool.get();
					Assert.assertTrue(c == pool.get());
					Assert.assertNotNull(c);
					cs[2] = c;
					Thread.sleep(100);
					pool.free();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		};
		thread1.start();
		thread2.start();
		thread3.start();
		thread1.join();
		thread2.join();
		thread3.join();
		Assert.assertNotNull(cs[0]);
		Assert.assertNotNull(cs[1]);
		Assert.assertNotNull(cs[2]);
	}

	@Test
	public void test2Threads() throws Exception {
		final FakeConnectionPool pool = new FakeConnectionPool(2);
		final Connection[] cs = new Connection[2];
		Thread thread1 = new Thread() {
			public void run() {
				try {
					Connection c = pool.get();
					Assert.assertTrue(c == pool.get());
					cs[0] = c;
					pool.free();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		};
		Thread thread2 = new Thread() {
			public void run() {
				try {
					Connection c = pool.get();
					Assert.assertTrue(c == pool.get());
					cs[1] = c;
					pool.free();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		};
		thread1.start();
		thread1.join();
		thread2.start();
		thread2.join();
		Assert.assertNotNull(cs[0]);
		Assert.assertNotNull(cs[1]);
		Assert.assertTrue(cs[0] == cs[1]);
	}

	private static class FakeConnectionPool extends DefaultConnectionPool {

		public FakeConnectionPool(int maxConnection) {
			this(new FakeDBDriver(), maxConnection);
		}

		public FakeConnectionPool(IDBDriver driver, int maxConnection) {
			super(driver, maxConnection);
		}

		@Override
		protected Connection connect() throws SQLException {
			return new FakeConnection();
		}

	}

	private static class FakeDBDriver implements IDBDriver {

		private String classname;

		public FakeDBDriver() {
			this(FakeDBDriver.class.getName());
		}

		public FakeDBDriver(String classname) {
			this.classname = classname;
		}

		@Override
		public String getClassDriver() {
			return this.classname;
		}

		@Override
		public String getPassword() {
			return null;
		}

		@Override
		public String ping() {
			return null;
		}

		@Override
		public String lastId(String table) {
			return null;
		}

		@Override
		public boolean isDebug() {
			return false;
		}

		@Override
		public String getUsername() {
			return null;
		}

		@Override
		public String getUrl() {
			// TODO Auto-generated method stub
			return null;
		}

	}

	private static class FakeConnection implements Connection {

		@Override
		public <T> T unwrap(Class<T> iface) throws SQLException {
			return null;
		}

		@Override
		public boolean isWrapperFor(Class<?> iface) throws SQLException {
			return false;
		}

		@Override
		public Statement createStatement() throws SQLException {
			return null;
		}

		@Override
		public PreparedStatement prepareStatement(String sql) throws SQLException {
			return null;
		}

		@Override
		public CallableStatement prepareCall(String sql) throws SQLException {
			return null;
		}

		@Override
		public String nativeSQL(String sql) throws SQLException {
			return null;
		}

		@Override
		public void setAutoCommit(boolean autoCommit) throws SQLException {
		}

		@Override
		public boolean getAutoCommit() throws SQLException {
			return false;
		}

		@Override
		public void commit() throws SQLException {
		}

		@Override
		public void rollback() throws SQLException {
		}

		@Override
		public void close() throws SQLException {
		}

		@Override
		public boolean isClosed() throws SQLException {
			return false;
		}

		@Override
		public DatabaseMetaData getMetaData() throws SQLException {
			return null;
		}

		@Override
		public void setReadOnly(boolean readOnly) throws SQLException {
		}

		@Override
		public boolean isReadOnly() throws SQLException {
			return false;
		}

		@Override
		public void setCatalog(String catalog) throws SQLException {
		}

		@Override
		public String getCatalog() throws SQLException {
			return null;
		}

		@Override
		public void setTransactionIsolation(int level) throws SQLException {
		}

		@Override
		public int getTransactionIsolation() throws SQLException {
			return 0;
		}

		@Override
		public SQLWarning getWarnings() throws SQLException {
			return null;
		}

		@Override
		public void clearWarnings() throws SQLException {
		}

		@Override
		public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
			return null;
		}

		@Override
		public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
				throws SQLException {
			return null;
		}

		@Override
		public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency)
				throws SQLException {
			return null;
		}

		@Override
		public Map<String, Class<?>> getTypeMap() throws SQLException {
			return null;
		}

		@Override
		public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		}

		@Override
		public void setHoldability(int holdability) throws SQLException {
		}

		@Override
		public int getHoldability() throws SQLException {
			return 0;
		}

		@Override
		public Savepoint setSavepoint() throws SQLException {
			return null;
		}

		@Override
		public Savepoint setSavepoint(String name) throws SQLException {
			return null;
		}

		@Override
		public void rollback(Savepoint savepoint) throws SQLException {
		}

		@Override
		public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		}

		@Override
		public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
				throws SQLException {
			return null;
		}

		@Override
		public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
				int resultSetHoldability) throws SQLException {
			return null;
		}

		@Override
		public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
				int resultSetHoldability) throws SQLException {
			return null;
		}

		@Override
		public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
			return null;
		}

		@Override
		public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
			return null;
		}

		@Override
		public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
			return null;
		}

		@Override
		public Clob createClob() throws SQLException {
			return null;
		}

		@Override
		public Blob createBlob() throws SQLException {
			return null;
		}

		@Override
		public NClob createNClob() throws SQLException {
			return null;
		}

		@Override
		public SQLXML createSQLXML() throws SQLException {
			return null;
		}

		@Override
		public boolean isValid(int timeout) throws SQLException {
			return false;
		}

		@Override
		public void setClientInfo(String name, String value) throws SQLClientInfoException {
		}

		@Override
		public void setClientInfo(Properties properties) throws SQLClientInfoException {
		}

		@Override
		public String getClientInfo(String name) throws SQLException {
			return null;
		}

		@Override
		public Properties getClientInfo() throws SQLException {
			return null;
		}

		@Override
		public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
			return null;
		}

		@Override
		public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
			return null;
		}

		@Override
		public void setSchema(String schema) throws SQLException {
			// TODO Auto-generated method stub

		}

		@Override
		public String getSchema() throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void abort(Executor executor) throws SQLException {
			// TODO Auto-generated method stub

		}

		@Override
		public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
			// TODO Auto-generated method stub

		}

		@Override
		public int getNetworkTimeout() throws SQLException {
			// TODO Auto-generated method stub
			return 0;
		}

	}

}
