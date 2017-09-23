package breder.util.util;

import java.text.CollationKey;
import java.text.Collator;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Métodos utilitários para manipulação e comparação de strings.
 * 
 * @author brbeder
 */
public class StringUtils {
  /**
   * Comparador de strings que não diferencia minúsculas de maiúsculas.
   * 
   * @see #getPlainSortCollator()
   */
  private static Comparator<String> plainSortComparator;
  /**
   * {@link Collator} que não diferencia minúsculas de maiúsculas. Este objeto é
   * um clone do <code>collator</code> default.
   */
  private static Collator plainSortCollator;

  /**
   * Mapa para remoção de acentos.
   */
  private static Map<Character, Character> ACCENT_REMOVAL_MAP =
    new HashMap<Character, Character>();
  /*
   * inicialização do mapa
   */
  static {
    ACCENT_REMOVAL_MAP.put('á', 'a');
    ACCENT_REMOVAL_MAP.put('à', 'a');
    ACCENT_REMOVAL_MAP.put('ã', 'a');
    ACCENT_REMOVAL_MAP.put('â', 'a');
    ACCENT_REMOVAL_MAP.put('Á', 'A');
    ACCENT_REMOVAL_MAP.put('À', 'A');
    ACCENT_REMOVAL_MAP.put('Ã', 'A');
    ACCENT_REMOVAL_MAP.put('Â', 'A');
    ACCENT_REMOVAL_MAP.put('é', 'e');
    ACCENT_REMOVAL_MAP.put('è', 'e');
    ACCENT_REMOVAL_MAP.put('ê', 'e');
    ACCENT_REMOVAL_MAP.put('É', 'E');
    ACCENT_REMOVAL_MAP.put('È', 'E');
    ACCENT_REMOVAL_MAP.put('Ê', 'E');
    ACCENT_REMOVAL_MAP.put('í', 'i');
    ACCENT_REMOVAL_MAP.put('ì', 'i');
    ACCENT_REMOVAL_MAP.put('Í', 'i');
    ACCENT_REMOVAL_MAP.put('Ì', 'i');
    ACCENT_REMOVAL_MAP.put('ó', 'o');
    ACCENT_REMOVAL_MAP.put('ò', 'o');
    ACCENT_REMOVAL_MAP.put('õ', 'o');
    ACCENT_REMOVAL_MAP.put('ô', 'o');
    ACCENT_REMOVAL_MAP.put('Ó', 'O');
    ACCENT_REMOVAL_MAP.put('Ò', 'O');
    ACCENT_REMOVAL_MAP.put('Õ', 'O');
    ACCENT_REMOVAL_MAP.put('Ô', 'O');
    ACCENT_REMOVAL_MAP.put('ú', 'u');
    ACCENT_REMOVAL_MAP.put('ù', 'u');
    ACCENT_REMOVAL_MAP.put('Ú', 'u');
    ACCENT_REMOVAL_MAP.put('Ù', 'u');
    ACCENT_REMOVAL_MAP.put('ç', 'c');
    ACCENT_REMOVAL_MAP.put('Ç', 'C');
  }

  /**
   * Retorna um {@link Collator} para comparação de strings que não diferencia
   * minúsculas de maiúsculas, i.e. {ZZZ, aaa} é ordenado como {aaa, ZZZ}.
   * 
   * OBS.: o objeto retornado é um singleton
   * 
   * IMPORTANTE: se o collator for usado em múltiplas comparações das mesmas
   * strings (p.ex. ordenação de longas listas), considerar o uso de
   * {@link CollationKey}. Referência:
   * <code>http://java.sun.com/docs/books/tutorial/i18n/text/perform.html</code>
   * 
   * @return <code>Collator</code> para comparação de strings que não diferencia
   *         minúsculas de maiúsculas
   */
  public static Collator getPlainSortCollator() {
    if (plainSortCollator == null) {
      /*
       * clonamos para garantir que não há efeitos colaterais
       */
      plainSortCollator = (Collator) Collator.getInstance().clone();
      plainSortCollator.setStrength(Collator.TERTIARY);
    }
    return plainSortCollator;
  }

  /**
   * Retorna um <code>Comparator&lt;String&gt;</code> que não diferencia
   * minúsculas de maiúsculas.
   * 
   * OBS.: o objeto retornado é um <i>singleton</i>
   * 
   * @return <code>Comparator&lt;String&gt;</code> que não diferencia minúsculas
   *         de maiúsculas
   */
  public static Comparator<String> getPlainSortComparator() {
    if (plainSortComparator == null) {
      plainSortComparator = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
          return StringUtils.compare(o1, o2);
        }
      };
    }
    return plainSortComparator;
  }

  /**
   * Compara alfabeticamente duas strings, sem diferenciar minúsculas de
   * maiúsculas.
   * 
   * @see Collator#compare(String, String)
   * 
   * @param s1 - primeira string
   * @param s2 - segunda string
   * @return -1 se s1 < s2, 0 se s1 == s2 e 1 se s1 > s2
   */
  public static int compare(String s1, String s2) {
    return getPlainSortCollator().compare(s1, s2);
  }

  /**
   * Ordena alfabeticamente um array de strings, sem diferenciar minúsculas de
   * maiúsculas. A ordenação acontece <i>in place</i>, i.e. o próprio array é
   * modificado e retornado como resultado.
   * 
   * @param array - array a ser ordenado
   * @return o array fornecido como entrada, ordenado
   */
  public static String[] sort(String[] array) {
    Arrays.sort(array, getPlainSortComparator());
    return array;
  }

  /**
   * Torna a primeira letra de uma string maiúscula. Ex. "fooBah" se torna
   * "FooBah" e "x" se torna "X".
   * 
   * @param name a string cuja primeira letra deverá se tornar maiúscula.
   * @return uma nova string igual a <code>name</code>, porém com a primeira
   *         letra maiúscula.
   */
  public static String capitalize(String name) {
    if (isEmptyString(name)) {
      return name;
    }
    char chars[] = name.toCharArray();
    chars[0] = Character.toUpperCase(chars[0]);
    return new String(chars);
  }

  /**
   * "Remove" acentos: substitui caracteres acentuados pelos caracteres
   * correspondentes sem acento (i.e. 'Á' por 'A', 'ç' por 'c' etc.)
   * 
   * @param str string com acentos
   * @return nova string com os acentos "removidos"
   */
  public static String removeAccents(String str) {
    char[] cStr = str.toCharArray();
    for (int i = 0; i < cStr.length; i++) {
      Character c = ACCENT_REMOVAL_MAP.get(Character.valueOf(cStr[i]));
      if (c != null) {
        cStr[i] = c.charValue();
      }
    }
    return new String(cStr);
  }

  /**
   * Teste se uma string é nula ou vazia.
   * 
   * @param str a string a ser testada
   * @return um indicativo booleano
   */
  public static boolean isEmptyString(final String str) {
    return str == null || str.trim().isEmpty();
  }

  /**
   * Retorna o último caracter de uma string.
   * 
   * @param str
   * @return último caracter da string
   */
  public static char lastChar(String str) {
    if (str.isEmpty()) {
      throw new IllegalArgumentException("string vazia");
    }
    return str.charAt(str.length() - 1);
  }

  /**
   * Concatena strings inserindo um separador entre elas. Strings nulas (
   * <code>null</code>) são tratadas como vazias.
   * <p>
   * Exemplos:
   * 
   * <pre>
   * concat(',', false, "aaa", null, "ccc") == aaa,,ccc
   * concat(',', true, "aaa", "bbb", null) == aaa,bbb,,
   * </pre>
   * 
   * @param separator separador
   * @param separatorAtEnd <code>true</code> se um separador deve ser anexado
   *        após o último valor
   * @param strings strings a serem concatenadas
   * @return strings concatenadas, com o separador entre elas
   */
  public static String concat(char separator, boolean separatorAtEnd,
    String... strings) {
    StringBuffer buffer =
      new StringBuffer((strings[0] == null ? "" : strings[0]));
    for (int i = 1; i < strings.length; i++) {
      buffer.append(separator);
      buffer.append((strings[i] == null ? "" : strings[i]));
    }
    if (separatorAtEnd) {
      buffer.append(separator);
    }
    return buffer.toString();
  }
}
