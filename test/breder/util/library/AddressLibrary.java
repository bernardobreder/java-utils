package breder.util.library;

import java.lang.reflect.Field;

public class AddressLibrary {

	public static final Address ADDRESS1 = new Address(1, "Sete de Setembro");

	public static final Address ADDRESS2 = new Address(2, "João Pessoa");

	public static final Address ADDRESS3 = new Address(2, "Mem de Sá");

	public static final Address[] ALL;

	static {
		Field[] fields = AddressLibrary.class.getFields();
		ALL = new Address[fields.length - 1];
		for (int n = 0; n < fields.length; n++) {
			try {
				ALL[n] = (Address) fields[n].get(null);
			} catch (Exception e) {
			}
		}
	}

}
