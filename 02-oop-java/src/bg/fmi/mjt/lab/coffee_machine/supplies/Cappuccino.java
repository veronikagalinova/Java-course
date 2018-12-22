package bg.fmi.mjt.lab.coffee_machine.supplies;

public class Cappuccino implements Beverage {
	private static final String TYPE = "Cappuccino";
	private static final double MILK = 150.0;
	private static final double COFFEE = 18.0;

	@Override
	public String getName() {
		return TYPE;
	}

	@Override
	public double getMilk() {
		return MILK;
	}

	@Override
	public double getCoffee() {
		return COFFEE;
	}

	@Override
	public double getWater() {
		return 0;
	}

	@Override
	public double getCacao() {
		return 0;
	}
}
