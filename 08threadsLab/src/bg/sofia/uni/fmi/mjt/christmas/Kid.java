package bg.sofia.uni.fmi.mjt.christmas;

public class Kid implements Runnable {
	private Workshop workshop;

	public Kid(Workshop workshop) {
		this.workshop = workshop;
	}

	/**
	 * Sends a wish for the given gift to Santa's workshop.
	 **/
	public void makeWish(Gift gift) {
		try {
			System.out.println("Kid is making wish...");
			Thread.sleep(1000);
			workshop.postWish(gift);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		makeWish(Gift.getGift());
	}

}