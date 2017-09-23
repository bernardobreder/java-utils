package breder.util.library;

public class Person {

	private Number id;

	private String name;

	public Person(Number id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Number getId() {
		return id;
	}

	public void setId(Number id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
