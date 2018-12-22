package bg.fmi.mjt.lab.coffee_machine;

import bg.fmi.mjt.lab.coffee_machine.container.BasicContainer;
import bg.fmi.mjt.lab.coffee_machine.container.Container;
import bg.fmi.mjt.lab.coffee_machine.supplies.Beverage;

public class BasicCoffeeMachine implements CoffeeMachine {
	private Container container;

	public BasicCoffeeMachine() {
		container = new BasicContainer();
	}

	@Override
	public Product brew(Beverage beverage) {
		if (container.checkResources(beverage, 1)) {
			return new Product("Espresso", 1, null);
		}
		return null;
	}

	@Override
	public Container getSupplies() {
		return container;
	}

	@Override
	public void refill() {
		container = new BasicContainer();
	}

}
