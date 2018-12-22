package bg.fmi.mjt.lab.coffee_machine.container;

import bg.fmi.mjt.lab.coffee_machine.supplies.Beverage;

public interface Container {
	/**
	* Returns the current quantity (in milliliters) of the water in the container
	*/
	public double getCurrentWater();

	/**
	* Returns the current quantity (in milliliters) of the milk in the container
	*/
	public double getCurrentMilk();

	/**
	* Returns the current quantity (in grams) of the coffee in the container
	*/
	public double getCurrentCoffee();

	/**
	* Returns the current quantity (in grams) of the cacao in the container
	*/
	public double getCurrentCacao();
	
	/**
	 * 
	 * Returns true if there are enough resources to make coffee
	 */
	public boolean checkResources(Beverage beverage, int quantity);
}
