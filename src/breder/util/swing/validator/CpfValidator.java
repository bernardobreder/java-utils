package breder.util.swing.validator;

import breder.util.swing.Validator;

public class CpfValidator implements Validator {

  @Override
  public boolean valid(String text) {
    String cpf = text.replace(".", "").replace("-", "").trim();
    return validaCPF(cpf);
  }

  private static String calcDigVerif(String num) {
    Integer primDig, segDig;
    int soma = 0, peso = 10;
    for (int i = 0; i < num.length(); i++)
      soma += Integer.parseInt(num.substring(i, i + 1)) * peso--;

    if (soma % 11 == 0 | soma % 11 == 1)
      primDig = new Integer(0);
    else
      primDig = new Integer(11 - (soma % 11));

    soma = 0;
    peso = 11;
    for (int i = 0; i < num.length(); i++)
      soma += Integer.parseInt(num.substring(i, i + 1)) * peso--;

    soma += primDig.intValue() * 2;
    if (soma % 11 == 0 | soma % 11 == 1)
      segDig = new Integer(0);
    else
      segDig = new Integer(11 - (soma % 11));

    return primDig.toString() + segDig.toString();
  }

  private static int calcSegDig(String cpf, int primDig) {
    int soma = 0, peso = 11;
    for (int i = 0; i < cpf.length(); i++)
      soma += Integer.parseInt(cpf.substring(i, i + 1)) * peso--;

    soma += primDig * 2;
    if (soma % 11 == 0 | soma % 11 == 1)
      return 0;
    else
      return 11 - (soma % 11);
  }

  public static boolean validaCPF(String cpf) {
    if (cpf.length() != 11)
      return false;

    String numDig = cpf.substring(0, 9);
    return calcDigVerif(numDig).equals(cpf.substring(9, 11));
  }

}
