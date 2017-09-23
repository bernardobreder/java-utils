package breder.util.swing.model;

import java.util.Arrays;

import breder.util.library.PersonLibrary;
import breder.util.library.Person;

public class StaticObjectModelTest extends StaticObjectModel<Person> {

	public StaticObjectModelTest() {
		super("a", "b");
	}

	@Override
	public Person getRow(int index) {
		return PersonLibrary.ALL[index];
	}

	@Override
	public int getSize() {
		return PersonLibrary.ALL.length;
	}

	@Override
	public Comparable<?> getValueAt(Person element, int row, int column) {
		switch (column) {
		case -1:
			return Arrays.asList(PersonLibrary.ALL).indexOf(element);
		case 0:
			return (Comparable<?>) element.getId();
		case 1:
			return element.getName();
		default:
			throw new RuntimeException();
		}
	}
}
