package bg.sofia.uni.fmi.mjt.movies;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import bg.sofia.uni.fmi.mjt.movies.model.Actor;
import bg.sofia.uni.fmi.mjt.movies.model.Movie;

public class MoviesExplorer {
	private List<Movie> movies;

	/**
	 * Loads the dataset from the given {@code dataInput} stream.
	 */
	public MoviesExplorer(InputStream dataInput) {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(dataInput))) {
			movies = br.lines().map(Movie::createMovie).collect(Collectors.toList());
		} catch (IOException e) {
			System.err.println("Error in MovieExplorer constructor: cannot read data input!\n" + e.getMessage());
		}
	}

	/**
	 * Returs all the movies loaded from the dataset.
	 **/
	public Collection<Movie> getMovies() {
		return Collections.unmodifiableList(movies);
	}

	public int countMoviesReleasedIn(int year) {
		return (int) movies.stream().filter(m -> m.getYear() == year).count();
	}

	public Movie findFirstMovieWithTitle(String title) {
		return movies.stream().filter(m -> m.getTitle().contains(title)).findFirst()
				.orElseThrow(IllegalArgumentException::new);
	}

	public Collection<Actor> getAllActors() {
		Set<Actor> result = new HashSet<>();
		this.movies.forEach(m -> result.addAll(m.getActors()));
		return result;
	}

	public int getFirstYear() {
		return movies.stream().min(Comparator.comparing(Movie::getYear)).get().getYear();
	}

	public Collection<Movie> getAllMoviesBy(Actor actor) {
		return movies.stream().filter(a -> a.getActors().contains(actor)).collect(Collectors.toSet());
	}

	public Collection<Movie> getMoviesSortedByReleaseYear() {
		// return movies.stream().map(Collections.sort(movies), new Comparator<Movie>()
		// {
		// @Override
		// public int compare(final Movie lhs, Movie rhs) {
		// return Integer.signum(lhs.getYear() - rhs.getYear());
		// }
		// });

		return movies.stream().sorted(Comparator.comparing(Movie::getYear)).collect(Collectors.toList());
	}

	public int findYearWithLeastNumberOfReleasedMovies() {
		Map<Integer, Long> result = movies.stream()
				.collect(Collectors.groupingBy(Movie::getYear, Collectors.counting()));
		return result.entrySet().stream().min(Comparator.comparing(x -> x.getValue())).get().getKey();
	}

	public Movie findMovieWithGreatestNumberOfActors() {
		return movies.stream().max(Comparator.comparing(Movie::getActorsSize)).get();
	}

}