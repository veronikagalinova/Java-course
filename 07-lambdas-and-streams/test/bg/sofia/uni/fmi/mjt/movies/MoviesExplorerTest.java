package bg.sofia.uni.fmi.mjt.movies;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import bg.sofia.uni.fmi.mjt.movies.model.Actor;
import bg.sofia.uni.fmi.mjt.movies.model.Movie;

public class MoviesExplorerTest {
	private static MoviesExplorer moviesExplorer;
	private static final int MOVIES_SIZE = 9;
	private static final int RELEASED_YEAR_1932 = 1932;
	private static final int RELEASED_YEAR_1972 = 1972;
	private static final int RELEASED_YEAR_1974 = 1974;
	private static final int RELEASED_YEAR_1977 = 1977;
	private static final int RELEASED_YEAR_1980 = 1980;
	private static final int RELEASED_YEAR_1990 = 1990;
	private static final int RELEASED_YEAR_1995 = 1995;
	private static final int RELEASED_YEAR_2010 = 2010;
	private static final int NUMBER_OF_ACTORS = 20;
	private List<Integer> releasedOrder = List.of(RELEASED_YEAR_1932, RELEASED_YEAR_1972, RELEASED_YEAR_1974,
			RELEASED_YEAR_1974, RELEASED_YEAR_1977, RELEASED_YEAR_1980, RELEASED_YEAR_1990, RELEASED_YEAR_1995,
			RELEASED_YEAR_2010);
	private Actor actor;

	@Before
	public void setup() {
		actor = new Actor("Al", "Pacino");
		StringBuilder data = new StringBuilder();
		data.append("Pretty Woman (1990) /Roberts, Julia").append(System.lineSeparator())
				.append("Star Wars (1977) /Fisher,Carrie /Ford,Harrison/Hammil,Mark/Baker,Kenny/ + "
						+ "Guiness, Alec/Prowse, David/ Cushing, Peter/Purvis, Jack")
				.append(System.lineSeparator())
				.append("Empire Strikes Back (1980) /Fisher,Carrie /Ford,Harrison/Hammil,Mark/Baker,Kenny")
				.append(System.lineSeparator()).append("The Godfather (1972) /Brando, Marlon/Pacino, Al/Caan, James")
				.append(System.lineSeparator())
				.append("The Godfather: Part II (1974) /Pacino, Al/De Niro, Robert/Duvall,Robert")
				.append(System.lineSeparator()).append("Chinatown (1974) /Nicholson,Jack/Dunaway,Faye/Huston,John")
				.append(System.lineSeparator()).append("Heat (1995) /Pacino, Al/De Niro, Robert/Kilmer,Val")
				.append(System.lineSeparator()).append("Kongo (1932) /Huston,Walter").append(System.lineSeparator())
				.append("Kongo (2010) /Simon,Maria").append(System.lineSeparator());

		ByteArrayInputStream input = new ByteArrayInputStream(data.toString().getBytes());

		moviesExplorer = new MoviesExplorer(input);
	}

	@Test
	public void testGetMovies() {
		assertEquals("Size not correct!", MOVIES_SIZE, moviesExplorer.getMovies().size());
	}

	@Test
	public void testcountMoviesReleasedIn() {
		final int numberOfMovies = 2;
		assertEquals("Method countMoviesReleasedIn(year) does not return correct value!", numberOfMovies,
				moviesExplorer.countMoviesReleasedIn(RELEASED_YEAR_1974));
	}

	@Test
	public void testfindFirstMovieWithTitleFindsResult() {
		String title = "Kongo";
		assertEquals("Method findFirstMovieWithTitle(title) does not return correct value!", RELEASED_YEAR_1932,
				moviesExplorer.findFirstMovieWithTitle(title).getYear());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testfindFirstMovieWithTitleThrows() {
		moviesExplorer.findFirstMovieWithTitle("no such movie");
	}

	@Test
	public void testGetFirstYear() {
		assertEquals("Method does not return correct value!", RELEASED_YEAR_1932, moviesExplorer.getFirstYear());
	}

	@Test
	public void testgetAllMoviesBy() {
		final int expected = 3;
		assertEquals("Method does not return correct value!", expected, moviesExplorer.getAllMoviesBy(actor).size());
	}

	@Test
	public void testGetMoviesSortedByReleaseYear() {
		List<Movie> actual = (List<Movie>) moviesExplorer.getMoviesSortedByReleaseYear();
		for (int i = 0; i < releasedOrder.size(); i++) {
			assertEquals((int) releasedOrder.get(i), actual.get(i).getYear());
		}
	}

	@Test
	public void testfindYearWithLeastNumberOfReleasedMovies() {
		final int actual = moviesExplorer.findYearWithLeastNumberOfReleasedMovies();
		assertTrue(releasedOrder.contains(actual));
	}

	@Test
	public void testMoviesWithGreathestNumberOfActors() {
		assertEquals("Star Wars", moviesExplorer.findMovieWithGreatestNumberOfActors().getTitle());
	}

	@Test
	public void testGetActors() {
		assertEquals(NUMBER_OF_ACTORS, moviesExplorer.getAllActors().size());
	}

}
