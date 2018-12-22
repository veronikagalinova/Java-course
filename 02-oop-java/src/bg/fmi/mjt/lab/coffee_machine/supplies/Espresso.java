package bg.fmi.mjt.lab.coffee_machine.supplies;

public class Espresso implements Beverage {
	private static final String TYPE = "Espresso";
	private static final double COFFEE = 10.0;
	private static final double WATER = 30.0;

	@Override
	public String getName() {
		return TYPE;
	}

	@Override
	public double getMilk() {
		return 0;
	}

	@Override
	public double getCoffee() {
		return COFFEE;
	}

	@Override
	public double getWater() {
		return WATER;
	}

	@Override
	public double getCacao() {
		return 0;
	}

}
