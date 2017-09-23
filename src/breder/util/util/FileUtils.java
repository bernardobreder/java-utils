package breder.util.util;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Classe com métodos de conveniência para manipulação de arquivos, diretórios e
 * caminhos do NFS.
 * 
 * @author Leonardo Barros
 */
public class FileUtils {

  /**
   * Flag que indica se o SO é da família Windows. Testado no Windows: <li>
   * Vista <li>XP
   */
  public static final boolean osIsWindows = System.getProperty("os.name")
    .toLowerCase().startsWith("windows");

  /**
   * Separador da extensão do arquivo.
   */
  private static final char FILE_EXTENSION_SEPARATOR = '.';

  /**
   * Obtém um nome válido de diretório para a área de projeto csbase a partir de
   * um nome com caracteres especiais. As seguintes transformações são feitas:<br>
   * - substituição de todos os caracteres acentuados pelo caractere
   * correspondente sem o acento<br>
   * - substituição de ç por c <br>
   * - substituição de todos os caracteres fora do conjunto [a-zA-Z_0-9] por "_"
   * (com exceção do caractere "-", que é aceito caso não esteja iniciando o
   * nome do diretório).
   * 
   * @param name -> nome potencialmente contendo caracteres especiais.
   * @return nome válido para a área de projeto (sem caracteres especiais).
   */
  public final static String fixDirectoryName(String name) {
    return StringUtils.removeAccents(name).replaceAll("[^-\\w]", "_");
  }

  /**
   * Obtém um nome válido de arquivo para a área de projeto csbase a partir de
   * um nome com caracteres especiais. As seguintes transformações são feitas:<br>
   * - substituição de todos os caracteres acentuados pelo caractere
   * correspondente sem o acento<br>
   * - substituição de ç por c <br>
   * - substituição de todos os caracteres que não são '.' e estão fora do
   * conjunto [a-zA-Z_0-9] por "_" (com exceção do caractere "-", que é aceito).
   * 
   * @param name -> nome potencialmente contendo caracteres especiais.
   * @return nome válido para a área de projeto (sem caracteres especiais).
   */
  public final static String fixFileName(String name) {
    return StringUtils.removeAccents(name).replaceAll("[^-\\w.]", "_");
  }

  /**
   * Remove um diretório (ou arquivo), removendo recursivamente o seu conteúdo
   * (no caso de diretórios).
   * 
   * @param file arquivo/diretório a remover.
   * 
   * @return <code>true</code> se o arquivo/diretório existir e for removido com
   *         sucesso; <code>false</code> caso o arquivo não exista.
   */
  public final static boolean delete(File file) {
    if (file == null) {
      throw new IllegalArgumentException("file == null");
    }
    if (!file.exists()) {
      return false;
    }
    if (file.isDirectory()) {
      File[] children = file.listFiles();
      if (!(children == null) && (children.length > 0)) {
        for (int i = 0; i < children.length; i++) {
          delete(children[i]);
        }
      }
    }
    return file.delete();
  }

  /**
   * Faz o agrupamento de um caminho quebrado em um array de <code>String</code>
   * em uma única <code>String</code> contendo separadores de diretório.
   * 
   * @param splittedPath array de <code>String</code> representando um caminho.
   * @param fileName o nome do arquivo.
   * @param separator separador a ser inserido na <code>String</code> retornada.
   * 
   * @return <code>String</code> contendo separadores de diretório.
   * 
   * @deprecated use {@link #joinPath(String, char, String...)}
   */
  @Deprecated
  public final static String joinPath(String[] splittedPath, String fileName,
    String separator) {
    return joinPath(fileName, separator.charAt(0), splittedPath);
  }

  /**
   * Faz o agrupamento de um caminho quebrado em um array de <code>String</code>
   * em uma única <code>String</code> contendo separadores de diretório.
   * 
   * @param separator separador a ser inserido na <code>String</code> retornada.
   * @param splittedPath array de <code>String</code> representando um caminho.
   * 
   * @return <code>String</code> contendo separadores de diretório.
   * 
   * @see #joinPath(char, String...)
   */
  public static final String joinPath(String separator, String[] splittedPath) {
    return joinPath(separator.charAt(0), splittedPath);
  }

  /**
   * Faz o agrupamento de um caminho quebrado em um array de <code>String</code>
   * em uma única <code>String</code> contendo separadores de diretório.
   * 
   * @param fileName o nome do arquivo.
   * @param separator separador a ser inserido na <code>String</code> retornada.
   * @param splittedPath array de <code>String</code> representando um caminho.
   * 
   * @return <code>String</code> contendo separadores de diretório.
   */
  public static final String joinPath(String fileName, char separator,
    String... splittedPath) {
    if (splittedPath == null) {
      throw new IllegalArgumentException("directoryPath == null");
    }
    if (fileName == null) {
      throw new IllegalArgumentException("O parâmetro fileName está nulo.");
    }
    String path = joinPath(separator, splittedPath);
    path += separator + fileName;
    return path;
  }

  /**
   * Faz o agrupamento de um caminho quebrado em um array de <code>String</code>
   * em uma única <code>String</code> contendo separadores de diretório.
   * 
   * @param splittedPath array de <code>String</code> representando um caminho.
   * @param separator separador a ser inserido na <code>String</code> retornada.
   * 
   * @return <code>String</code> contendo separadores de diretório.
   * 
   * @deprecated use {@link #joinPath(char, String...)}
   */
  @Deprecated
  public final static String joinPath(String[] splittedPath, String separator) {
    return joinPath(separator.charAt(0), splittedPath);
  }

  /**
   * Faz o agrupamento de um caminho quebrado em um array de
   * <code>Strings</code> em uma única <code>String</code> contendo separadores
   * de diretório do sistema operacional corrente.
   * 
   * @param splittedPath array de <code>String</code> representando um caminho.
   * 
   * @return <code>String</code> contendo separadores de diretório.
   */
  public static final String joinPath(String... splittedPath) {
    return joinPath(File.separatorChar, splittedPath);
  }

  /**
   * Faz o agrupamento de um caminho quebrado em um array de <code>String</code>
   * em uma única <code>String</code> contendo separadores de diretório. O path
   * criado pode opcionalmente ser absoluto.
   * 
   * @param absolute <code>true</code> se o path deve ser absoluto
   * @param separator separador a ser inserido na <code>String</code> retornada
   * @param splittedPath array de <code>String</code> representando um caminho
   * 
   * @return <code>String</code> contendo separadores de diretório.
   */
  public static String joinPath(boolean absolute, char separator,
    String... splittedPath) {
    StringBuilder buffer = new StringBuilder();
    if (splittedPath == null) {
      throw new IllegalArgumentException("array de diretórios == null");
    }
    if (absolute) {
      buffer.append(separator);
    }
    for (int i = 0; i < splittedPath.length - 1; i++) {
      buffer.append(splittedPath[i]);
      buffer.append(separator);
    }
    if (splittedPath.length > 0) {
      buffer.append(splittedPath[splittedPath.length - 1]);
    }
    return buffer.toString();
  }

  /**
   * Faz o agrupamento de um caminho quebrado em um array de <code>String</code>
   * em uma única <code>String</code> contendo separadores de diretório.
   * 
   * @param separator separador a ser inserido na <code>String</code> retornada
   * @param splittedPath array de <code>String</code> representando um caminho
   * @return <code>String</code> contendo separadores de diretório.
   */
  public static final String joinPath(char separator, String... splittedPath) {
    return joinPath(false, separator, splittedPath);
  }

  /**
   * Agrupa o nome de um arquivo sem extensão com a sua extensão.
   * 
   * @param fileNameWithoutExtension O nome sem extensão (Não aceita
   *        {@code null}).
   * @param fileExtension A extensão (Não aceita {@code null}).
   * 
   * @return .
   */
  public final static String joinFileName(String fileNameWithoutExtension,
    String fileExtension) {
    if (fileNameWithoutExtension == null) {
      throw new IllegalArgumentException("O parâmetro fileName está nulo.");
    }
    if (fileExtension == null) {
      throw new IllegalArgumentException("O parâmetro fileExtension está nulo.");
    }
    return fileNameWithoutExtension + FILE_EXTENSION_SEPARATOR + fileExtension;
  }

  /**
   * Quebra um caminho em forma de <code>String</code> com separadores de
   * diretório do sistema operacional corrente em um array de
   * <code>String</code> contendo seus componentes.
   * 
   * @param joinedPath caminho a ser quebrado.
   * 
   * @return caminho quebrado em um array de seus componentes.
   */
  public final static String[] splitPath(String joinedPath) {
    return splitPath(joinedPath, File.separator);
  }

  /**
   * Quebra um caminho em forma de <code>String</code> com separadores de
   * diretório em um array de <code>String</code> contendo seus componentes.
   * 
   * @param joinedPath caminho a ser quebrado.
   * @param separator separador a ser utilizado.
   * 
   * @return caminho quebrado em um array de seus componentes.
   * 
   * @see #splitPath(String, char)
   */
  public final static String[] splitPath(String joinedPath, String separator) {
    if (joinedPath == null) {
      throw new IllegalArgumentException("joinedPath == null");
    }
    if (separator == null) {
      throw new IllegalArgumentException("separator == null");
    }
    return splitPath(joinedPath, separator.charAt(0));
  }

  /**
   * Quebra um caminho em forma de <code>String</code> com separadores de
   * diretório em um array de <code>String</code> contendo seus componentes.
   * 
   * @param path caminho a ser quebrado.
   * @param separator separador a ser utilizado.
   * 
   * @return caminho quebrado em um array de seus componentes.
   */
  public static final String[] splitPath(String path, char separator) {
    Scanner scanner =
      new Scanner(path).useDelimiter(Pattern.quote(String.valueOf(separator)));
    List<String> pathAsList = new ArrayList<String>();
    while (scanner.hasNext()) {
      String dir = scanner.next();
      /*
       * FIXME o 'if' abaixo existe apenas para compatibilizar o comportamento
       * com a implementação anterior (que não usava o Scanner). Ele faz com que
       * paths vazios ("//") sejam ignorados, quando na talvez devessem retornar
       * "" (que é o comportamento natural do Scanner para este caso).
       */
      if (!dir.isEmpty()) {
        pathAsList.add(dir);
      }
    }
    return pathAsList.toArray(new String[pathAsList.size()]);
  }

  /**
   * Extrai a extensão do arquivo especificado.
   * 
   * @param path caminho (ou o próprio nome) para o arquivo do qual pretende-se
   *        extrair a extensão.
   * 
   * @return extensão do arquivo ou <code>null</code> se o arquivo não possuir
   *         extensão.
   */
  public final static String getFileExtension(String path) {
    if (path == null) {
      throw new IllegalArgumentException("path == null");
    }
    if (path.isEmpty()) {
      throw new IllegalArgumentException("path está vazia");
    }
    int periodIndex = path.lastIndexOf(FILE_EXTENSION_SEPARATOR);
    if (periodIndex == -1 || periodIndex == path.length() - 1) {
      return null;
    }
    return path.substring(periodIndex + 1).toLowerCase();
  }

  /**
   * Substitui a extensão do arquivo especificado. Se não há extensão, 'newExt'
   * é adicionada como extensão de qualquer forma.
   * 
   * @param path - caminho (ou só nome de arquivo) onde aplicar a substituição.
   * @param newExt - extensão a ser adicionada, que pode ou não já iniciar com
   *        um ponto.
   * 
   * @return arquivo com a extensão substituída
   */
  public final static String replaceFileExtension(String path, String newExt) {
    if (path == null) {
      throw new IllegalArgumentException("path == null");
    }

    if (path.isEmpty()) {
      throw new IllegalArgumentException("path está vazio");
    }

    int periodIndex = path.lastIndexOf('.');
    if (periodIndex == -1) {
      periodIndex = path.length();
    }

    String withoutExt = path.substring(0, periodIndex); // não inclui o '.'

    if (newExt.startsWith(".")) {
      return withoutExt + newExt;
    }
    return withoutExt + "." + newExt;
  }

  /**
   * Adiciona a extensão indicada, se o caminho não a tiver.
   * 
   * @param path - caminho (ou só nome de arquivo) onde incluir a extensão.
   * @param ext - extensão a ser adicionada, que pode ou não já iniciar com um
   *        ponto.
   * 
   * @return arquivo com a extensão adicionada.
   */
  public final static String addFileExtension(final String path,
    final String ext) {

    if (path == null) {
      throw new IllegalArgumentException("path == null");
    }

    if (path.isEmpty()) {
      throw new IllegalArgumentException("path está vazio");
    }

    if (ext == null) {
      throw new IllegalArgumentException("extensão == null");
    }

    if (ext.isEmpty()) {
      throw new IllegalArgumentException("extensão está vazia");
    }

    // se extensão começava com ponto, removê-lo
    String newExt = ext;
    if (newExt.startsWith(".")) {
      newExt = newExt.substring(1, newExt.length());
    }

    // será que o caminho já tem a extensão requerida ?
    final String currentExt = FileUtils.getFileExtension(path);
    if (currentExt != null && currentExt.equalsIgnoreCase(newExt)) {
      return path;
    }

    // se caminho terminava com ponto, utilizá-lo:
    if (path.endsWith(".")) {
      return path + newExt;
    }
    return path + "." + newExt;
  }

  /**
   * Extrai o último item de um caminho de diretórios (não faz distinção se o
   * último item for um arquivo ou diretório). </p> Exemplo:<br>
   * <code>
   * System.out.println(FileUtils.getFileName("dir1/dir2/dir3/arq.txt"));
   * </code><br>
   * <i>Output: arq.txt</i>
   * 
   * 
   * @param path caminho contendo separadores de diretórios.
   * 
   * @return nome de arquivo ou diretório (último item do caminho). Retorna uma
   *         <code>String</code> vazia se o caminho estiver vazio.
   */
  public final static String getFileName(String path) {
    return getFileName(path, File.separator);
  }

  /**
   * Extrai o último item de um caminho de diretórios (não faz distinção se o
   * último item for um arquivo ou diretório). </p> Exemplo:<br>
   * <code>
   * System.out.println(FileUtils.getFileName("dir1/dir2/dir3/arq.txt"));
   * </code><br>
   * <i>Output: arq.txt</i>
   * 
   * 
   * @param path caminho contendo separadores de diretórios.
   * @param separator separador a ser utilizado.
   * 
   * @return nome de arquivo ou diretório (último item do caminho). Retorna uma
   *         <code>String</code> vazia se o caminho estiver vazio. Retorna o
   *         próprio caminho passado se este não contiver os separadores de
   *         diretório.
   */
  public final static String getFileName(String path, String separator) {
    if (path == null) {
      throw new IllegalArgumentException("path == null");
    }
    if (separator == null) {
      throw new IllegalArgumentException("path == null");
    }
    int index = path.lastIndexOf(separator);
    if (index == -1) {
      return path;
    }
    return path.substring(index + 1);
  }

  /**
   * <p>
   * Extrai o caminho em que se encontra um arquivo a partir de um caminho de
   * diretórios completo (retorna o caminho com exceção do último item). Não faz
   * distinção se o último item for um arquivo ou diretório.
   * </p>
   * Exemplo:<br>
   * <code>
   * System.out.println(FileUtils.getFilePath("dir1/dir2/dir3/arq.txt"));
   * </code><br>
   * <i>Output: dir1/dir2/dir3</i>
   * 
   * @param path caminho contendo separadores de diretórios.
   * 
   * @return nome de arquivo ou diretório (último item do caminho). Retorna uma
   *         <code>String</code> vazia se o caminho estiver vazio.
   */
  public final static String getFilePath(String path) {
    return getFilePath(path, File.separator);
  }

  /**
   * <p>
   * Extrai o caminho em que se encontra um arquivo a partir de um caminho de
   * diretórios completo (retorna o caminho com exceção do último item). Não faz
   * distinção se o último item for um arquivo ou diretório.
   * </p>
   * Exemplo:<br>
   * <code>
   * System.out.println(FileUtils.getFilePath("dir1/dir2/dir3/arq.txt"));
   * </code><br>
   * <i>Output: dir1/dir2/dir3</i>
   * 
   * @param path caminho contendo separadores de diretórios.
   * @param separator separador a ser utilizado.
   * 
   * @return nome de arquivo ou diretório (último item do caminho). Retorna uma
   *         <code>String</code> vazia se o caminho estiver vazio ou se contiver
   *         apenas o nome do arquivo.
   */
  public final static String getFilePath(String path, String separator) {
    if (path == null) {
      throw new IllegalArgumentException("path == null");
    }
    if (separator == null) {
      throw new IllegalArgumentException("path == null");
    }
    path = path.trim();
    if (path.isEmpty()) {
      return "";
    }
    int index = path.lastIndexOf(separator);
    if (index == -1) {
      return "";
    }
    return path.substring(0, index);
  }

  /**
   * Verifica se uma string começa com <code>[protocolo]://</code> . Os
   * protocolos aceitos são:
   * <ul>
   * <li>http
   * <li>https
   * <li>ftp
   * <li>file
   * </ul>
   * 
   * @param url string a ser verificada
   * @return <code>true</code> se a string começa com
   *         <code>[protocolo]://</code>
   */
  public static boolean startsAsURL(String url) {
    return url.matches("^(https?|file|ftp):.*");
  }

  /**
   * Prepara a URL indicada, inserindo o protocolo ou o caminho absoluto se
   * necessário. A URL pode ter o protocolo (http://, file:// ou ftp://)
   * definido, e deve terminar com '/'. Se o usuário não definiu o protocolo,
   * assumimos 'file://'.
   * 
   * @param rawUrl - URL fornecida pelo usuário
   * @return URL corrigida, com protocolo 'file://' caso este não tenha sido
   *         definido, e terminando em '/'. Quaisquer caracteres '\' foram
   *         convertidos para '/'
   */
  public static String prepareURL(String rawUrl) {
    /*
     * precisamos garantir que os separadores são '/' e não '\'
     */
    String url = rawUrl.replaceAll("\\\\", "/");
    StringBuilder fixedURL = new StringBuilder(url);
    if (!startsAsURL(url)) {
      if (!FileUtils.isAbsolutePath(url)) {
        /*
         * usuário forneceu um path local _relativo_, temos que prefixá-lo com o
         * path absoluto do diretório corrente para que a URL file://... fique
         * correta. Temos também que garantir que paths no formato do Windows
         * (p.ex. C:\dir1\dir2) tenham os chars '\' trocados por '/'
         */
        String currDir = System.getProperty("user.dir").replaceAll("\\\\", "/");
        fixedURL.insert(0, currDir + '/');
      }
      if (osIsWindows) {
        /*
         * precisamos prefixar o path na forma C:/... com '/'. Referência:
         * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4867640
         */
        fixedURL.insert(0, '/');
      }
      fixedURL.insert(0, "file://");
    }
    if (fixedURL.charAt(fixedURL.length() - 1) != '/') {
      fixedURL.append('/');
    }
    return fixedURL.toString();
  }

  /**
   * Verifica se um path é absoluto para qualquer SO, ou seja, não é necessário
   * que o path seja absoluto para o mesmo SO de quem fez esta verificação.
   * 
   * @param path
   * @return true se o path começa com '/' ou se o path começa com letra + ':\'
   *         (p.ex. "C:\")
   */
  public static boolean isAbsolutePath(String path) {
    return path.startsWith("/") || path.matches("^\\w:\\\\.*");
  }

  /**
   * Fecha um {@link Closeable} (útil para fechar {@link Reader readers} e
   * {@link Writer writers} em blocos <code>finally</code>).
   * 
   * @param closeable objeto a ser fechado. Pode ser <code>null</code>.
   * @return <code>true</code> se a operação foi bem-sucedida,
   *         <code>false</code> em caso de erro ou se
   *         <code>closeable == null</code>
   */
  public static boolean close(Closeable closeable) {
    if (closeable == null) {
      return false;
    }
    try {
      closeable.close();
      return true;
    }
    catch (IOException e) {
      return false;
    }
  }

  /**
   * Obtém o path relativo entre dois arquivos.
   * <p>
   * OBS.: o JDK 7 possuirá esta funcionalidade; remover daqui quando
   * atualizarmos
   * 
   * @param from arquivo de referência
   * @param to arquivo para o qual queremos calcular o path relativo
   * @param separator separador do path
   * @return path relativo de <code>from</code> para <code>to</code>
   * @throws IOException se um dos paths não puder ser obtido via
   *         {@link File#getCanonicalPath()}
   * 
   * @see #getRelativePath(String, String, char)
   */
  public static String getRelativePath(File from, File to, char separator)
    throws IOException {
    String pathFrom = from.getCanonicalPath();
    String pathTo = to.getCanonicalPath();
    if (pathFrom.equals(pathTo)) {
      return ".";
    }
    List<String> componentsFrom = pathToList(pathFrom, separator);
    List<String> componentsTo = pathToList(pathTo, separator);
    /*
     * procuramos o índice do último componente em comum do path
     */
    int lastMatch = findPrefixEnd(componentsFrom, componentsTo);
    int goUp = componentsFrom.size() - 1;
    if (lastMatch > -1) {
      goUp -= lastMatch;
    }
    else if (componentsTo.isEmpty()) {
      /*
       * não achamos prefixo comum porque o path de destino é "/", temos que
       * subir um nível a mais
       */
      goUp++;
    }
    StringBuilder relativePath = new StringBuilder();
    /*
     * primeiro "subimos" no path até o primeiro ponto em comum
     */
    for (int i = goUp; i > 0;) {
      relativePath.append("..");
      i--;
      if (i > 0) {
        relativePath.append(separator);
      }
    }
    /*
     * agora percorremos o restante do caminho
     */
    int sizeTo = componentsTo.size();
    boolean addSeparator = goUp > 0;
    for (int i = lastMatch + 1; i < sizeTo; i++) {
      if (addSeparator) {
        relativePath.append(separator);
      }
      addSeparator = true;
      relativePath.append(componentsTo.get(i));
    }
    return relativePath.toString();
  }

  /**
   * Obtém o path relativo entre dois paths.
   * <p>
   * OBS.: o JDK 7 possuirá esta funcionalidade; remover daqui quando
   * atualizarmos
   * 
   * @param from path de referência
   * @param to path para o qual queremos calcular o path relativo
   * @param separator separador do path
   * @return path relativo de <code>from</code> para <code>to</code>
   * @throws IOException se um dos paths não puder ser obtido via
   *         {@link File#getCanonicalPath()}
   * 
   * @see #getRelativePath(File, File, char)
   */
  public static String getRelativePath(String from, String to, char separator)
    throws IOException {
    return getRelativePath(new File(from), new File(to), separator);
  }

  /**
   * Converte um path para uma lista de strings representando seus componentes.
   * 
   * @param path path
   * @param separator separador do path
   * @return lista com os componentes do path
   */
  private static List<String> pathToList(String path, char separator) {
    String regex = Pattern.quote("" + separator);
    List<String> list = new ArrayList<String>();
    for (String string : path.split(regex)) {
      if (!string.isEmpty()) {
        list.add(string);
      }
    }
    return list;
  }

  /**
   * Encontra o índice do último elemento em comum entre duas listas que começam
   * com os mesmos elementos.
   * 
   * @param <T> tipo dos elementos da lista
   * @param list1 primeira lista
   * @param list2 segunda lista
   * @return índice do último elemento em comum entre as duas listas, ou -1 caso
   *         elas não comecem com os mesmos elementos
   */
  private static <T> int findPrefixEnd(List<T> list1, List<T> list2) {
    if (list1.isEmpty() || list2.isEmpty()) {
      return -1;
    }
    if (!list1.get(0).equals(list2.get(0))) {
      return -1;
    }
    int size1 = list1.size();
    int size2 = list2.size();
    int i = 1;
    for (; i < size1; i++) {
      if (i >= size2 || !list1.get(i).equals(list2.get(i))) {
        break;
      }
    }
    return i - 1;
  }
}
