package bg.fmi.mjt.lab.coffee_machine;

public class Product {
	private String name;
	private int quantity;
	private String luck;
	
	Product(String name, int quantity, String luck) {
		this.name = name;
		this.quantity = quantity;
		this.luck = luck;
	}
	
	public String getName() {
		return name;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public String getLuck() {
		return luck;
	}

}
