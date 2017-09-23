package breder.util.library;

import java.lang.reflect.Field;

public class TelefoneLibrary {

	public static final Telefone TELEFONE1 = new Telefone(1, "27140597");

	public static final Telefone TELEFONE2 = new Telefone(2, "26100842");

	public static final Telefone TELEFONE3 = new Telefone(2, "26100840");

	public static final Telefone[] ALL;

	static {
		Field[] fields = TelefoneLibrary.class.getFields();
		ALL = new Telefone[fields.length - 1];
		for (int n = 0; n < fields.length; n++) {
			try {
				ALL[n] = (Telefone) fields[n].get(null);
			} catch (Exception e) {
			}
		}
	}

}
