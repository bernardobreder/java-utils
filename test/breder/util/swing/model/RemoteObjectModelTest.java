package breder.util.swing.model;

import java.rmi.RemoteException;

import breder.util.library.Person;
import breder.util.library.PersonLibrary;

public class RemoteObjectModelTest extends RemoteObjectModel<Person> {

	public RemoteObjectModelTest() {
		super("A", "B");
	}

	@Override
	public Person[] refreshModelRemote() throws RemoteException {
		return service();
	}

	public static Person[] service() throws RemoteException {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
		}
		return PersonLibrary.ALL;
	}

	public static void main(String[] args) {
		ObjectModelTest.main(new RemoteObjectModelTest());
	}

}
