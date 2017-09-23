package breder.util.sql;

/**
 * Classe que faz backup do banco de dados
 * 
 * 
 * @author Bernardo Breder
 */
public class BackupDatabase {

  //	/**
  //	 * Realiza o backup de uma conexão
  //	 * 
  //	 * @param driver
  //	 * @return backup em stream
  //	 * @throws IOException
  //	 * @throws InterruptedException
  //	 */
  //	public static InputStream backup(IDBDriver driver) throws IOException, InterruptedException {
  //		List<String> cmd = new ArrayList<String>();
  //		cmd.addAll(Arrays.asList("/usr/local/mysql/bin/mysqldump", driver.getDatabase(), "-h", driver.getHost(), "-u", driver.getDatabase()));
  //		if (driver.getPassword() != null && driver.getPassword().trim().length() > 0) {
  //			cmd.addAll(Arrays.asList("-p", driver.getPassword()));
  //		}
  //		Process p = Runtime.getRuntime().exec(cmd.toArray(new String[cmd.size()]));
  //		p.waitFor();
  //		byte[] bytes = InputStreamUtil.getBytes(p.getInputStream());
  //		return new ByteArrayInputStream(bytes);
  //	}

  //  /**
  //   * Testador
  //   * 
  //   * @param args
  //   * @throws InterruptedException
  //   * @throws IOException
  //   */
  //  public static void main(String[] args) throws IOException,
  //    InterruptedException {
  //    backup(new MySqlDriver("localhost", "regproj", "regproj", ""));
  //  }
}
