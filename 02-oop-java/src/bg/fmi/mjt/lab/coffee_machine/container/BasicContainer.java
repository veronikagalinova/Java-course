package bg.fmi.mjt.lab.coffee_machine.container;

import bg.fmi.mjt.lab.coffee_machine.supplies.Beverage;
import bg.fmi.mjt.lab.coffee_machine.supplies.Espresso;

public class BasicContainer implements Container {
	private double water;
	private double coffee;
	
	public BasicContainer() {
		water = 600;
		coffee = 600;
	}
	
	@Override
	public double getCurrentWater() {
		return water;
	}
	
	@Override
	public double getCurrentMilk() {
		return 0;
	}
	
	@Override
	public double getCurrentCoffee() {
		return coffee;
	}
	
	@Override
	public double getCurrentCacao() {
		return 0;
	}

	@Override
	public boolean checkResources(Beverage beverage,int quantity) {
		if (beverage instanceof Espresso
				&& water - beverage.getWater() >= 0 
				&& coffee - beverage.getCoffee() >= 0) {
			
			water -= beverage.getWater();
			coffee -= beverage.getCoffee();
			return true;
		}
		return false;
	}
}
