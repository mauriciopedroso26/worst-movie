package br.com.outsera.worstmovie.controller;

import br.com.outsera.worstmovie.dto.MovieResponse;
import br.com.outsera.worstmovie.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/producers")
    public ResponseEntity<List<MovieResponse>> getProducersByInterval() {
        return ResponseEntity.status(HttpStatus.OK).body(movieService.getProducersByInterval());
    }
}
