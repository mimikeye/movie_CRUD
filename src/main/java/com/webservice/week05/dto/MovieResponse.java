package com.webservice.week05.dto;

public record MovieResponse(
        Long id,
        String title,
        String director,
        String genre,
        int releaseYear,
        double rating,
        int runningTime
) {}