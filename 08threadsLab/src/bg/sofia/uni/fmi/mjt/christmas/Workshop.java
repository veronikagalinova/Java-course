package bg.sofia.uni.fmi.mjt.christmas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Workshop {
	private static final int ELVES_SIZE = 20;
	private Elf[] elves = new Elf[ELVES_SIZE];
	private BlockingQueue<Gift> backlog = new ArrayBlockingQueue<>(1024);
	private AtomicInteger wishCount = new AtomicInteger(0);
	List<Gift> preparedGifts = Collections.synchronizedList(new ArrayList<Gift>());
	private boolean isChristmasTime = false;
	ExecutorService executorService = Executors.newFixedThreadPool(ELVES_SIZE);

	public Workshop() {
		AtomicInteger id = new AtomicInteger(0);
		for (int i = 0; i < ELVES_SIZE; i++) {
			elves[i] = new Elf(id.incrementAndGet(), this);
			executorService.execute(elves[i]);
		}

	}

	/**
	 * Adds a gift to the elves' backlog.
	 **/
	public void postWish(Gift gift) {
		if (gift == null) {
			return;
		}

		wishCount.incrementAndGet();
		backlog.offer(gift);
		System.out.println("Size of baglog: " + backlog.size());
	}

	/**
	 * Returns the next gift from the elves' backlog that has to be manufactured.
	 **/
	public Gift nextGift() {
		Gift gift = null;
		try {
			while (backlog.take() != null) {
				gift = backlog.take();
				System.out.println(gift.getType());
				preparedGifts.add(gift);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return gift;
	}

	/**
	 * Returns an array of the elves working in Santa's workshop.
	 **/
	public Elf[] getElves() {
		return elves;
	}

	public int getBacklogSize() {
		return backlog.size();
	}

	/**
	 * Returns the total number of wishes sent to Santa's workshop by the kids.
	 **/
	public int getWishCount() {
		return wishCount.get();
	}

	public int getTotalPreparedGiftsSize() {
		return preparedGifts.size();
	}

	public void setChristmasTime(boolean isChristmasTime) {
		this.isChristmasTime = isChristmasTime;
		System.out.println("It's Christmas Time!");
		executorService.shutdown();
	}

}