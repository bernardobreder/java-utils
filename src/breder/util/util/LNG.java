/*
 * $Id: LNG.java 104826 2010-04-28 23:05:46Z karla $
 */

package breder.util.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * <p>
 * Classe respons�vel pela internacionaliza��o de textos do sistema.
 * </p>
 * 
 * <p>
 * A classe estende a funcionalidade da <code>ResourceBundle</code>, permitindo
 * ao usu�rio criar uma hierarquia com v�rios n�veis de pacotes de propriedades.
 * </p>
 * 
 * <p>
 * A chave a ser traduzida � buscada desde o n�vel mais baixo da hierarquia at�
 * o n�vel mais superior, passando pelos arquivos representando os
 * <code>Locales</code> mais espec�ficos at� os mais gen�ricos (mais detalhes
 * abaixo).
 * </p>
 * 
 * <br>
 * Estrat�gia de busca:<br>
 * 
 * <ol>
 * <li>A chave primeiro � buscada no �ltimo pacote carregado, com o
 * <code>Locale</code> mais espec�fico existente;</li>
 * <li>N�o encontrando, passa-se aos <code>Locales</code> mais gen�ricos
 * (estrat�gia padr�o do <code>ResourceBundle</code>);</li>
 * <li>Se a chave continuar n�o sendo encontrada, passa-se ao n�vel superior na
 * hierarquia de pacotes (pen�ltimo pacote carregado) e reinicia-se a estrat�gia
 * dos passos anteriores (busca desde o <code>Locale</code> mais espec�fico at�
 * o mais gen�rico);</li>
 * <li>Terminando a hierarquia, se mesmo assim a chave n�o for encontrada, uma
 * <code>String</code> padr�o de erro � retornada.</li>
 * </ol>
 * 
 * <p>
 * Exemplo:<br>
 * Suponhamos que tenhamos 3 n�veis de pacotes de propriedades: um para uma
 * biblioteca, outro para um sistema e um terceiro para uma aplica��o. Vamos
 * supor ainda que os seguintes arquivos de propriedades tenham sido
 * disponibilizados:
 * 
 * <ul>
 * <li>biblioteca_pt.properties (Portugu�s Portugal/Brasil)</li>
 * <li>sistema_pt_BR.properties (Brasil)</li>
 * <li>sistema_pt_BR_BA.properties (Bahia)</li>
 * <li>aplicacao_pt_BR.properties (Brasil)</li>
 * <li>aplicacao_pt_BR_BA.properties (Bahia)</li>
 * </ul>
 * </p>
 * Se os pacotes tiverem sido carregados na ordem: 1) biblioteca; 2) sistema e
 * 3) aplica��o, uma chave qualquer ser� buscada na seguinte ordem:<br>
 * 
 * <ol>
 * <li>aplicacao_pt_BR_BA.properties (n�vel 2, <code>Locale</code> mais
 * espec�fico);</li>
 * <li>aplicacao_pt_BR.properties (n�vel 2, <code>Locale</code> mais gen�rico);</li>
 * <li>sistema_pt_BR_BA.properties (n�vel 1, <code>Locale</code> mais
 * espec�fico);</li>
 * <li>sistema_pt_BR.properties (n�vel 1, <code>Locale</code> mais gen�rico);</li>
 * <li>biblioteca_pt.properties (n�vel 0, �nico <code>Locale</code> carregado).</li>
 * </ol>
 * 
 * <br>
 * Se a chave em quest�o n�o for encontrada em nenhum dos arquivos, uma
 * <code>String</code> padr�o de erro � retornada.
 * 
 * <p>
 * A classe, ao receber uma solicita��o de carregamento de arquivo de
 * propriedades (m�todo <code>load</code>), ir� primeiro carregar o arquivo de
 * propriedades da biblioteca, caso isto j� n�o tenha sido feito.
 * </p>
 * 
 * <br>
 * INSTRU��ES DE USO:<br>
 * 
 * <ol>
 * <li>Carregue a classe com um ou mais pacotes de arquivos de propriedades,
 * invocando o m�todo <code>load</code>.</li>
 * <li>Traduza os textos no c�digo utilizando o m�todo <code>get</code>.</li>
 * </ol>
 * 
 * Exemplo:<br>
 * LNG.load("idiom", new Locale("pt","BR"));<br>
 * String welcome = LNG.get("welcome");
 * 
 * <p>
 * Se n�o for necess�rio criar um novo arquivo de propriedades, mas se voc�
 * deseja que as propriedades da biblioteca sejam carregadas com um
 * <code>Locale</code> diferente do usado pela JVM, pode-se usar a vers�o
 * sobrecarregada do m�todo <code>load(Locale)</code>.
 * 
 * @author Leonardo Barros
 */
public class LNG {
  /** Hierarquia de <code>ResourceBundles</code> */
  private static HierarchicalResourceBundle bundleHierarchy;

  /**
   * Carrega um <code>ResourceBundle</code> de propriedades na classe.<br>
   * Na primeira vez em que o m�todo for chamado, carrega-se o <i>bundle</i> da
   * biblioteca (n�vel 0). O <i>bundle</i> especificado ser� ent�o carregado no
   * n�vel 1. As demais chamadas ir�o simplesmente acrescentar n�veis �
   * hierarquia de <i>bundles</i>: n�vel 2, n�vel 3, e assim por diante.<br>
   * N�o � permitida a carga de um <i>bundle</i> com um <code>Locale</code>
   * diferente do atualmente carregado.
   * 
   * @param baseName nome de base para os arquivos comuns ao
   *        <code>ResourceBundle</code>.
   * @param locale <code>Locale</code> a ser usado na tradu��o de propriedades.
   * 
   */
  public static void load(String baseName, Locale locale) {
    preLoadValidations(baseName, locale);
    ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale);
    addResourceBundle(bundle, locale);
  }

  /**
   * Carrega um <code>ResourceBundle</code> de propriedades na classe, a partir
   * da URL fornecida. Semelhante ao load(baseName, locale). Na primeira vez em
   * que o m�todo for chamado, carrega-se o <i>bundle</i> da biblioteca (n�vel
   * 0). O <i>bundle</i> especificado ser� ent�o carregado no n�vel 1. As demais
   * chamadas ir�o simplesmente acrescentar n�veis � hierarquia de
   * <i>bundles</i>: n�vel 2, n�vel 3, e assim por diante.<br>
   * N�o � permitida a carga de um <i>bundle</i> com um <code>Locale</code>
   * diferente do atualmente carregado.
   * 
   * @param pathPrefix URL-base para carga do bundle. Deve conter o protocolo
   *        (http://, file:// etc.)
   * @param baseName nome de base para os arquivos comuns ao
   *        <code>ResourceBundle</code>.
   * @param locale <code>Locale</code> a ser usado na tradu��o de propriedades.
   * 
   * @throws IOException Exce��o na leitura do arquivo de propriedades
   * 
   */
  public static void loadURL(String pathPrefix, String baseName, Locale locale)
    throws IOException {
    preLoadValidations(baseName, locale);
    String addr = pathPrefix;
    if (!pathPrefix.endsWith("/")) {
      addr += "/";
    }
    addr += baseName + "_" + locale + ".properties";
    URL url = new URL(addr);
    InputStream in = url.openStream();
    ResourceBundle bundle = new PropertyResourceBundle(in);
    addResourceBundle(bundle, locale);
  }

  /**
   * Realiza as valida��es necess�rias antes de fazer a carga de um bundle.
   * 
   * @param baseName nome de base para os arquivos comuns ao
   *        <code>ResourceBundle</code>.
   * @param locale <code>Locale</code> a ser usado na tradu��o de propriedades.
   */
  private static void preLoadValidations(String baseName, Locale locale) {
    if (baseName == null) {
      throw new IllegalArgumentException("baseName == null");
    }
    if (locale == null) {
      throw new IllegalArgumentException("locale == null");
    }
    if (bundleHierarchy != null) {
      if (!isLocaleAllowed(locale)) {
        throw new IllegalArgumentException("locale atual ("
          + bundleHierarchy.getLocale() + ") � incompat�vel com " + locale);
      }
    }
    else {
      LNG.load(locale);
    }
  }

  /**
   * Adiciona o bundle fornecido � hierarquia de bundles j� existente. Antes da
   * adi��o, supomos que uma valida��o j� foi realizada. <br>
   * 
   * @see LNG#preLoadValidations(String baseName, Locale locale)
   * 
   * @param bundle bundle que ser� adicionado � hierarquia
   * @param locale <code>Locale</code> usado na tradu��o de propriedades
   */
  private static void addResourceBundle(ResourceBundle bundle, Locale locale) {
    HierarchicalResourceBundle newHierarchy =
      new HierarchicalResourceBundle(bundle, locale, bundleHierarchy);
    bundleHierarchy = newHierarchy;
  }

  /**
   * Informa o <code>Locale<code> a ser usado pela biblioteca. Evita a
   * necessidade de criar um arquivo de propriedades quando deseja-se somente
   * usar as propriedades da biblioteca.
   * Esse m�todo deve ser chamado antes do uso de qualquer m�todo acessor
   * (<code>get</code>) da classe, caso contr�rio as propriedades da biblioteca
   * ser�o carregadas usando o <code>Locale</code> da JVM.
   * 
   * @param locale <code>Locale</code> a ser usado na tradu��o de mensagens da
   *        biblioteca.
   */
  public static void load(Locale locale) {
    if (locale == null) {
      throw new IllegalArgumentException("locale == null");
    }
    if (bundleHierarchy != null) {
      throw new IllegalStateException(
        "As propriedades da biblioteca j� foram carregadas com o Locale "
          + bundleHierarchy.getLocale() + ".");
    }
    loadLibraryProperties(locale);
  }

  /**
   * Carrega as propriedades da biblioteca.
   * 
   * @param locale <code>Locale</code> a ser usado na escolha do <i>bundle</i>
   *        de propriedades.
   */
  private static void loadLibraryProperties(Locale locale) {
    ResourceBundle libBundle =
      ResourceBundle.getBundle(LNGKeys.IDIOM_BASE_NAME, locale);
    bundleHierarchy = new HierarchicalResourceBundle(libBundle);
  }

  /**
   * Indica se � permitida a superposi��o do <code>Locale</code> atualmente
   * usado na classe pelo <code>Locale</code> especificado (acr�scimo de n�vel
   * na hierarquia).
   * 
   * @param locale <code>Locale</code> a ser comparado com o atual.
   * 
   * @return <code>true</code> se o <code>Locale</code> atual n�o tiver uma
   *         l�ngua definida (default) ou se o <code>Locale</code> passado
   *         representar a mesma l�ngua, mesmo que sejam diferentes dialetos, do
   *         <code>Locale</code> atualmente carregado. Retorna
   *         <code>false</code> se e somente se existir um <code>Locale</code>
   *         carregado de uma l�ngua diferente do <code>Locale</code> passado.
   */
  private static boolean isLocaleAllowed(Locale locale) {
    /*
     * Este m�todo foi comentado por n�o comportar-se como esperado: ao carregar
     * um Locale n�o existente, o ResourceBundle carrega o pt_BR em vez de
     * carregar o arquivo default (sem Locale). Esse comportamento deve ser
     * verificado antes de descomentar esse c�digo. O teste associado a ele
     * LNGTest#testLoadStringLocale2() tamb�m deve ser descomentado ap�s
     * resolver-se o problema.
     */
    /*
     * String currentLanguage = bundleHierarchy.getLocale().getLanguage(); if
     * (currentLanguage.equals("")) { return true; } if
     * (!currentLanguage.equals(locale.getLanguage())) { return false; }
     */
    return true;
  }

  /**
   * Verifica se a chave indicada est� definida em algum arquivo de idiomas,
   * independente da exist�ncia de um valor.
   * 
   * @param key chave a ser buscada.
   * @return TRUE se a chave indicada est� definida, FALSE caso contr�rio.
   */
  public static boolean hasKey(String key) {
    if (key == null) {
      throw new IllegalArgumentException("key == null");
    }
    if (bundleHierarchy == null) {
      load(Locale.getDefault());
    }
    return bundleHierarchy.has(key);
  }

  /**
   * <p>
   * Obt�m a tradu��o de uma propriedade para o <code>Locale</code> atualmente
   * em uso.
   * </p>
   * 
   * @param key chave a ser buscada.
   * 
   * @return tradu��o da chave no <code>Locale</code> atual.
   */
  public static String get(String key) {
    if (key == null) {
      throw new IllegalArgumentException("key == null");
    }
    if (bundleHierarchy == null) {
      load(Locale.getDefault());
    }
    return bundleHierarchy.get(key);
  }

  /**
   * Consulta hierarquicamente os bundles por um match para qualquer uma das
   * strings presentes em um array. O array � percorrido na ordem em que os
   * elementos s�o declarados, para cada bundle da hierarquia. A ordem de busca
   * �:
   * <ul>
   * <li>todas as chaves do array s�o buscadas no array corrente, em ordem. Se
   * algum resultado for encontrado, a busca � interrompida
   * <li>se nenhum resultado foi encontrado no bundle corrente, a busca �
   * repassada para o restante da hierarquia
   * </ul>
   * 
   * @param keys - chaves
   * @return o texto associado � primeira das chaves presentes em algum dos
   *         bundles; caso nenhuma das strings esteja presente em nenhum dos
   *         bundles, retorna missingKeys(keys[0])
   */
  public static String getAnyOf(String... keys) {
    if (keys == null || keys.length == 0) {
      throw new IllegalArgumentException(
        "pelo menos uma chave deve ser fornecida");
    }
    if (bundleHierarchy == null) {
      load(Locale.getDefault());
    }
    final String text = bundleHierarchy.getAnyOf(keys);
    return text != null ? text : LNG.missingKeyMsg(keys[0]);
  }

  /**
   * Descarrega todos os pacotes de propriedades previamente carregados. Esse
   * m�todo existe para garantir a independ�ncia de testes sucessivos da classe,
   * revertendo a mesma ao seu estado original.
   */
  static void unload() {
    bundleHierarchy = null;
  }

  /**
   * Obt�m o <code>java.util.Locale</code> sendo utilizado.
   * 
   * @return <code>java.util.Locale</code> carregado.
   * 
   */
  public static Locale getLocale() {
    if (bundleHierarchy == null) {
      load(Locale.getDefault());
    }
    return bundleHierarchy.getLocale();
  }

  /**
   * <p>
   * Obt�m a tradu��o de uma propriedade que permite par�metros para o
   * <code>Locale</code> atualmente em uso.
   * </p>
   * O m�todo faz uso da classe {@link java.text.MessageFormat} para formatar os
   * par�metros.
   * 
   * @param key chave para a propriedade parametriz�vel.
   * @param args argumentos para a propriedade.
   * 
   * @return tradu��o da chave no <code>Locale</code> atual, com os argumentos
   *         passados.
   */
  public static String get(String key, Object[] args) {
    if (args == null) {
      throw new IllegalArgumentException("args == null");
    }
    if (args.length == 0) {
      throw new IllegalArgumentException("args.length == 0");
    }
    String pattern = get(key);
    return MessageFormat.format(pattern, args);
  }

  /**
   * Retorna a hierarquia de bundles utilizada internamente. Este m�todo n�o
   * deve ser usado na maior parte das aplica��es: foi inclu�do para uma
   * situa��o espec�fica, em que um framework (Swing Java Builder) j� possui um
   * mecanismo interno semelhante ao LNG e portanto precisa ter acesso aos
   * bundles internos desta classe, para n�o replicar todas as chaves j�
   * cadastradas nela.
   * 
   * @return hierarquia de <code>ResourceBundle</code>s.
   */
  public static HierarchicalResourceBundle getBundleHierarchy() {
    return bundleHierarchy;
  }

  /**
   * Classe que engloba (<i>Wrapper</i>) um <code>ResourceBundle</code>,
   * possibilitando uma hierarquia de pacotes de propriedades. A chave a ser
   * traduzida � buscada desde o n�vel mais baixo da hierarquia at� o n�vel mais
   * superior, passando pelos arquivos representando os <code>Locales</code>
   * mais espec�ficos at� os mais gen�ricos.
   * 
   * @author Leonardo Barros
   */
  public static final class HierarchicalResourceBundle {
    /** Hierarquia de pacotes de propriedades previamente carregada */
    private HierarchicalResourceBundle parentHierarchy;

    /** Pacote de propriedades */
    private ResourceBundle bundle;

    /** Locale usado na tradu��o de propriedades */
    private final Locale locale;

    /**
     * Cria um pacote de propriedades hier�rquico.
     * 
     * @param bundle �ltimo pacote de propriedades carregado.
     */
    HierarchicalResourceBundle(ResourceBundle bundle) {
      this(bundle, bundle.getLocale(), null);
    }

    /**
     * Cria um pacote de propriedades hier�rquico.
     * 
     * @param bundle �ltimo pacote de propriedades carregado.
     * @param locale <code>Locale</code> usado na tradu��o de propriedades.
     * @param parentHierarchy refer�ncia para a hierarquia existente de pacotes
     *        de propriedades.
     */
    HierarchicalResourceBundle(ResourceBundle bundle, Locale locale,
      HierarchicalResourceBundle parentHierarchy) {
      this.bundle = bundle;
      this.locale = locale;
      this.parentHierarchy = parentHierarchy;
    }

    /**
     * Obt�m o <code>Locale</code> atualmente em uso.
     * 
     * @return <code>Locale</code> atualmente em uso.
     * 
     */
    Locale getLocale() {
      return locale;
    }

    /**
     * Verifica se a chave indicada est� definida em algum arquivo de idiomas,
     * independente da exist�ncia de um valor.
     * 
     * @param key chave a ser buscada.
     * @return TRUE se a chave indicada est� definida, FALSE caso contr�rio.
     */
    boolean has(String key) {
      try {
        bundle.getString(key);
        return true;
      }
      catch (MissingResourceException ex1) {
        if (parentHierarchy != null) {
          return parentHierarchy.has(key);
        }
        return false;
      }
    }

    /**
     * Obt�m a tradu��o de uma propriedade na hierarquia de pacotes carregados.
     * Se a chave n�o for encontrada no <i>bundle</i> atual, procura
     * recursivamente na hierarquia de <i>bundles</i> carregados.
     * 
     * @param key chave a ser buscada na hierarquia de pacotes.
     * 
     * @return valor correspondente � chave no <code>Locale</code> carregado.
     */
    String get(String key) {
      try {
        return bundle.getString(key);
      }
      catch (MissingResourceException ex1) {
        if (parentHierarchy == null) {
          return LNG.missingKeyMsg(key);
        }
        try {
          return parentHierarchy.get(key);
        }
        catch (MissingResourceException ex2) {
          return LNG.missingKeyMsg(key);
        }
      }
    }

    /**
     * Consulta hierarquicamente os bundles por um match para qualquer uma das
     * strings presentes em um array. O array � percorrido na ordem em que os
     * elementos s�o declarados, para cada bundle da hierarquia. A ordem de
     * busca �:
     * <ul>
     * <li>todas as chaves do array s�o buscadas no array corrente, em ordem. Se
     * algum resultado for encontrado, a busca � interrompida
     * <li>se nenhum resultado foi encontrado no bundle corrente, a busca �
     * repassada para o restante da hierarquia
     * </ul>
     * 
     * @param keys - chaves
     * @return o texto associado � primeira das chaves presentes em algum dos
     *         bundles, ou null caso nenhuma das strings esteja presente em
     *         nenhum dos bundles
     */
    String getAnyOf(String... keys) {
      for (String key : keys) {
        try {
          return bundle.getString(key);
        }
        catch (MissingResourceException e) {
          // n�o fazemos nada (continuamos o loop para tentar outras
          // chaves)
        }
      }
      if (parentHierarchy != null) {
        return parentHierarchy.getAnyOf(keys);
      }
      return null;
    }

    /**
     * Retorna o pacote de propriedades (bundle) encapsulado por esta classe.
     * 
     * @return pacote de propriedades {@link ResourceBundle}).
     */
    public ResourceBundle getBundle() {
      return bundle;
    }

    /**
     * Retorna a hierarquia de pacotes previamente carregada, "pai" desta
     * inst�ncia.
     * 
     * @return hierarquia-pai de pacotes desta inst�ncia.
     */
    public HierarchicalResourceBundle getParentHierarchy() {
      return parentHierarchy;
    }
  }

  /**
   * Retorna texto padr�o para chave n�o encontrada. Imprime mensagem de erro na
   * sa�da adequada.
   * 
   * @param key chave n�o encontrada.
   * 
   * @return texto padr�o para chave n�o encontrada.
   */
  private static String missingKeyMsg(String key) {
    System.err.println("[LNG] Chave n�o encontrada: " + key);
    return "<<<" + key + ">>>";
  }
}
