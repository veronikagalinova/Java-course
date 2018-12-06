package bg.sofia.uni.fmi.mjt.christmas;

public class Tester {

	public static void main(String[] args) throws InterruptedException {
		Workshop workshop = new Workshop();
		Thread t1 = new Thread(new Kid(workshop));
		t1.start();
		t1.join();
		Thread t2 = new Thread(new Kid(workshop));
		t2.start();
		t2.join();
		Thread t3 = new Thread(new Kid(workshop));
		t3.start();
		t3.join();

		System.out.println("Wishcount: " + workshop.getWishCount());
		System.out.println("Number of presents to be prepared: " + workshop.getBacklogSize());
		System.out.println("Total prepared gifts: " + workshop.getTotalPreparedGiftsSize());

		synchronized (workshop) {
			workshop.wait(900);
		}
		workshop.setChristmasTime(true);
	}

}
