package com.webservice.week05.repository;

import com.webservice.week05.domain.Movie;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MemoryMovieRepository implements MovieRepository {

    private final Map<Long, Movie> store = new LinkedHashMap<>();
    private long sequence = 0L;

    @Override
    public Movie save(Movie movie) {
        movie.setId(++sequence);
        store.put(movie.getId(), movie);
        return movie;
    }

    @Override
    public List<Movie> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Optional<Movie> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Movie update(Movie movie) {
        store.put(movie.getId(), movie);
        return movie;
    }

    @Override
    public void deleteById(Long id) {
        store.remove(id);
    }
}