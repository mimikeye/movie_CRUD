package com.webservice.week05.service;

import com.webservice.week05.domain.Movie;
import com.webservice.week05.dto.MovieRequest;
import com.webservice.week05.dto.MovieResponse;
import com.webservice.week05.repository.MovieRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class MovieService {

    private final MovieRepository repository;

    public MovieService(MovieRepository repository) {
        this.repository = repository;
    }

    public MovieResponse create(MovieRequest r) {
        validate(r);

        Movie movie = new Movie(
                null,
                r.title(),
                r.director(),
                r.genre(),
                r.releaseYear(),
                r.rating(),
                r.runningTime()
        );

        return toResponse(repository.save(movie));
    }

    public List<MovieResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<MovieResponse> findByGenre(String genre) {
        return repository.findByGenre(genre)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public MovieResponse findById(Long id) {
        return toResponse(findMovie(id));
    }

    public MovieResponse update(Long id, MovieRequest r) {
        validate(r);

        Movie movie = findMovie(id);

        movie.setTitle(r.title());
        movie.setDirector(r.director());
        movie.setGenre(r.genre());
        movie.setReleaseYear(r.releaseYear());
        movie.setRating(r.rating());
        movie.setRunningTime(r.runningTime());

        return toResponse(repository.update(movie));
    }

    public void delete(Long id) {
        findMovie(id);
        repository.deleteById(id);
    }

    private void validate(MovieRequest r) {
        if (r.title() == null || r.title().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Title is required"
            );
        }

        if (r.director() == null || r.director().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Director is required"
            );
        }

        if (r.genre() == null || r.genre().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Genre is required"
            );
        }

        if (r.releaseYear() < 1888 || r.releaseYear() > 2026) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid release year"
            );
        }

        if (r.rating() < 0 || r.rating() > 10) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Rating must be between 0 and 10"
            );
        }

        if (r.runningTime() <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Running time must be greater than 0"
            );
        }
    }

    private Movie findMovie(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Movie not found: " + id
                        )
                );
    }

    private MovieResponse toResponse(Movie movie) {
        return new MovieResponse(
                movie.getId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getGenre(),
                movie.getReleaseYear(),
                movie.getRating(),
                movie.getRunningTime()
        );
    }
}