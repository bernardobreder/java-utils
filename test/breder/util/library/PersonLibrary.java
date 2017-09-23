package breder.util.library;

import java.lang.reflect.Field;

public class PersonLibrary {

	public static final Person PERSON1 = new Person(1, "Bernardo");

	public static final Person PERSON2 = new Person(2, "Renata");

	public static final Person[] ALL;

	static {
		Field[] fields = PersonLibrary.class.getFields();
		ALL = new Person[fields.length - 1];
		for (int n = 0; n < fields.length; n++) {
			try {
				ALL[n] = (Person) fields[n].get(null);
			} catch (Exception e) {
			}
		}
	}

}
