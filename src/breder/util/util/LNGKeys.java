/*
 * Created on Mar 23, 2005
 */
package breder.util.util;

/**
 * Interface contendo constantes que s�o chaves para a tradu��oo de textos da
 * biblioteca, usada em conjunto com a classe <code>LNG</code>.
 * 
 * @author Leonardo Barros
 */
public interface LNGKeys {
  /** Nome de base para o arquivo de propriedades de idioma. */
  String IDIOM_BASE_NAME = "javautils_lng";

  /** Chave para conjun��o aditiva "e" */
  String AND = "javautils.and";

  /** Chave para verbo "Cancelar" */
  String CANCEL = "javautils.cancel";

  /**
   * Chave para o formato padr�o para formata��o de datas (internacionalizado
   * nos arquivos de idioma).
   */
  String DEFAULT_DATE_FORMAT = "javautils.default.date.format";

  /** Chave para mensagem de erro para erro de preenchimento */
  String INPUT_ERROR_MESSAGE = "javautils.input.error.msg";

  /** Chave para mensagem de erro para �cone faltando */
  String MISSING_ICON_MESSAGE = "javautils.missing.icon.msg";

  /** Chave para adv�rbio de nega��o "N�o" */
  String NO = "javautils.no";

  /** Chave para adv�rbio de nega��o "N�o para todos" */
  String NO_TO_ALL = "javautils.noToAll";

  /** Chave para adv�rbio de afirma��o "Sim" */
  String YES = "javautils.yes";

  /** Chave para adv�rbio de afirma��o "Sim para todos" */
  String YES_TO_ALL = "javautils.yesToAll";

  /** Chave para verbo "Fechar" */
  String CLOSE = "javautils.close";
}
