package bg.sofia.uni.fmi.mjt.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.time.Month;

import org.junit.Before;
import org.junit.Test;

public class MemCacheTest {
	private Cache<String, String> cache;

	@Before
	public void setup() {
		cache = new MemCache<>(10);
	}

	// TESTS FOR SET
	@Test
	public void testDoesNotAddNullValue() {
		cache.set("Key1", null, LocalDateTime.now());
		assertEquals(0, cache.size());
	}

	@Test
	public void testDoesNotAddNullKey() {
		cache.set(null, "value", LocalDateTime.now());
		assertEquals(0, cache.size());
	}

	@Test
	public void testDoesNotAddNullKeyAndValue() {
		cache.set(null, null, LocalDateTime.now());
		assertEquals(0, cache.size());
	}

	@Test(expected = CapacityExceededException.class)
	public void setShouldThrowIfFullCapacityNoExpired() {
		for (int i = 0; i < 10; i++) {
			cache.set("key " + i, "value", null);
		}
		cache.set("k", "v", null);
	}

	@Test
	public void testSetPresentKey() {
		cache.set("Key1", "Value1", null);
		cache.set("Key1", "Value2", null);
		assertEquals("Value2", cache.get("Key1"));
	}

	@Test
	public void getExpirationPresentKey() {
		cache.set("Key1", "value1", LocalDateTime.of(2018, Month.NOVEMBER, 10, 15, 25));
		assertEquals(LocalDateTime.of(2018, Month.NOVEMBER, 10, 15, 25), cache.getExpiration("Key1"));
	}

	@Test
	public void getExpirationKeyNotPresent() {
		cache.set("key", "value", null);
		assertTrue(cache.getExpiration("not present") == null);
	}

	@Test
	public void removePresentKey() {
		cache.set("key", "value", null);
		assertTrue(cache.remove("key"));
	}

	@Test
	public void removeKeyNotPresent() {
		cache.set("key", "value", null);
		assertFalse(cache.remove("k"));
	}

	@Test
	public void setFullCacheContainsExpiredItems() {
		for (int i = 0; i < 9; i++) {
			cache.set("key " + i, "value " + i, null);
		}
		cache.set("k", "v", LocalDateTime.now());
		cache.set("key", "value", null);
		assertEquals("value", cache.get("key"));
	}

	// TESTS FOR GET
	@Test
	public void getPresentAndNotExpired() {
		cache.set("Key1", "Value1", null);
		String actualValue = cache.get("Key1");
		assertEquals("Value1", actualValue);
	}

	@Test
	public void getPresentaAndNotExpired2() {
		cache.set("Key1", "Value1", LocalDateTime.of(2018, Month.NOVEMBER, 10, 8, 25));
		String actualValue = cache.get("Key1");
		assertEquals("Value1", actualValue);
	}

	@Test
	public void testGetExpired() {
		cache.set("key", "value", LocalDateTime.of(2017, Month.NOVEMBER, 10, 20, 25));
		assertTrue(cache.get("key") == null);
	}

	@Test
	public void testGetExpired2() {
		cache.set("expired", "value", LocalDateTime.now());
		assertTrue(cache.get("expired") == null);
	}

	@Test
	public void getPresentKeyExpiredshouldReturnNull() {
		cache.set("asdf", "Value1", LocalDateTime.now());
		assertTrue(cache.get("asdf") == null);
	}

	@Test
	public void getKeyNotPresentShouldReturnNull() {
		cache.set("Key1", "Value1", LocalDateTime.now());
		assertTrue(cache.get("K") == null);
	}

	@Test
	public void testSize() {
		cache.set("key1", "value", null);
		cache.set("key2", "value", LocalDateTime.now());
		cache.set("key3", "value", LocalDateTime.of(2018, Month.NOVEMBER, 10, 20, 25));
		assertEquals(3, cache.size());
	}

	@Test
	public void testClear() {
		cache.set("key1", "value", null);
		cache.clear();
		assertEquals(0, cache.size());
	}

	// @Test
	// public void testHitRateWithSuccessfulHits() {
	// cache.set("key1", "value", null);
	// cache.set("key13", "value", null);
	// cache.set("key2", null, null);
	// cache.set(null, null, null);
	// assertEquals(2.0, cache.getHitRate(), 1e-15);
	// }

	@Test
	public void testHitRateNoSuccessfulHit() {
		cache.set("key1", null, null);
		assertEquals(0, cache.getHitRate(), 1e-15);
	}

	@Test
	public void testHitRate() {
		cache.set("k1", "v1", null);
		cache.set("k2", "v2", null);
		cache.set("k3", "v1", null);
		cache.set("k4", "v2", null);
		// cache.set("key1", null, null);
		// cache.set("key2", null, null);
		double result = cache.getHitRate();
		assertEquals(1, result, 1e-15);
	}
}
