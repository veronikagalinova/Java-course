package bg.sofia.uni.fmi.mjt.christmas;

public class Christmas {

	private Workshop workshop;

	/**
	 * The number of kids that are going to send wishes to Santas's workshop.
	 **/
	private int numberOfKids;

	/**
	 * Christmas will start in {@code christmasTime} milliseconds.
	 **/
	private int christmasTime;

	public Christmas(Workshop workshop, int numberOfKids, int christmasTime) {
		this.workshop = workshop;
		this.numberOfKids = numberOfKids;
		this.christmasTime = christmasTime;
	}

	public static void main(String[] args) {
		Christmas christmas = new Christmas(new Workshop(), 100, 2000);
		christmas.celebrate();
	}

	public void celebrate() {
		Workshop workshop = new Workshop();
		Thread t1 = new Thread(new Kid(workshop));
		t1.start();
		try {
			t1.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Thread t2 = new Thread(new Kid(workshop));
		t2.start();
		try {
			t2.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Thread t3 = new Thread(new Kid(workshop));
		t3.start();
		try {
			t3.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Wishcount: " + workshop.getWishCount());
		System.out.println("Number of presents to be prepared: " + workshop.getBacklogSize());
		System.out.println("Total prepared gifts: " + workshop.getTotalPreparedGiftsSize());

		synchronized (workshop) {
			try {
				workshop.wait(900);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		workshop.setChristmasTime(true);
	}

	public Workshop getWorkshop() {
		return workshop;
	}
}