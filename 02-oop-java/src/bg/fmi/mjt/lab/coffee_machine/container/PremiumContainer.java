package bg.fmi.mjt.lab.coffee_machine.container;

import bg.fmi.mjt.lab.coffee_machine.supplies.Beverage;

public class PremiumContainer implements Container {
	private double water;
	private double coffee;
	private double milk;
	private double cacao;
	
	public PremiumContainer() {
		water = 1000;
		coffee = 1000;
		milk = 1000;
		cacao = 300;
	}
	
	@Override
	public double getCurrentWater() {
		return water;
	}
	
	@Override
	public double getCurrentMilk() {
		return milk;
	}
	@Override
	public double getCurrentCoffee() {
		return coffee;
	}
	@Override
	public double getCurrentCacao() {
		return cacao;
	}


	@Override
	public boolean checkResources(Beverage beverage, int quantity) {
		for (int i = 0; i < quantity; i++) {
			
			if(water-beverage.getWater() < 0 
					|| coffee - beverage.getCoffee() < 0
					|| milk - beverage.getMilk() < 0
					|| cacao - beverage.getCacao() < 0)
				return false;
			
			water -= beverage.getWater();
			coffee -= beverage.getCoffee();
			milk -= beverage.getMilk();
			cacao -= beverage.getCacao();
		}
		return true;
	}
}
