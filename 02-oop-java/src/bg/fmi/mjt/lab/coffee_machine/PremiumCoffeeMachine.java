package bg.fmi.mjt.lab.coffee_machine;

import java.util.List;

import bg.fmi.mjt.lab.coffee_machine.container.Container;
import bg.fmi.mjt.lab.coffee_machine.container.PremiumContainer;
import bg.fmi.mjt.lab.coffee_machine.supplies.Beverage;

public class PremiumCoffeeMachine implements CoffeeMachine {
	private Container container;
	private boolean autoRefill;
	private List<String> lucks = List.of("If at first you don't succeed call it version 1.0.",
			"Today you will make magic happen!", "Have you tried turning it off and on again?",
			"Life would be much more easier if you had the source code.");
	private int luckIndex = 0;

	public PremiumCoffeeMachine() {
		container = new PremiumContainer();
		autoRefill = false;
	}

	/**
	 * @param autoRefill
	 *            - if true, it will automatically refill the container if there are
	 *            not enough ingredients to make the coffee drink
	 */
	public PremiumCoffeeMachine(boolean autoRefill) {
		container = new PremiumContainer();
		this.autoRefill = autoRefill;
	}

	/**
	 * If quantity is <= 0 or the quantity is not supported for the particular
	 * Coffee Machine the method returns null
	 */
	public Product brew(Beverage beverage, int quantity) {
		if (quantity <= 0 || quantity > 3)
			return null;

		if (container.checkResources(beverage, quantity)) {
			return new Product(beverage.getName(), quantity, lucks.get(luckIndex++ % 4));
		} else if (autoRefill) {
			refill();
		}
		return null;

	}

	@Override
	public Product brew(Beverage beverage) {
		return brew(beverage, 1);
	}

	@Override
	public Container getSupplies() {
		return container;
	}

	@Override
	public void refill() {
		container = new PremiumContainer();
	}
}
