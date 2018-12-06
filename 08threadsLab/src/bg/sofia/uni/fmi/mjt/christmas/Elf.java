package bg.sofia.uni.fmi.mjt.christmas;

import java.util.concurrent.atomic.AtomicInteger;

public class Elf implements Runnable {
	private int id;
	private Workshop workshop;
	private AtomicInteger count = new AtomicInteger(0);

	public Elf(int id, Workshop workshop) {
		this.id = id;
		this.workshop = workshop;
	}

	/**
	 * Gets a wish from the backlog and creates the wanted gift.
	 **/
	public void craftGift() {
		synchronized (workshop) {
			try {
				System.out.println("Elf is waiting...");
				workshop.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			Gift gift = workshop.nextGift();
			System.out.println("----Gift: " + gift.getType());
			try {
				Thread.sleep(gift.getCraftTime());

				System.out.println("Elf prepared gift");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Returns the total number of gifts that the given elf has crafted.
	 **/
	public int getTotalGiftsCrafted() {
		return count.get();
	}

	@Override
	public void run() {
		System.out.println("Elf running");
		craftGift();
	}
}