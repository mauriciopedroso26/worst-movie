package br.com.outsera.worstmovie.controller;

import br.com.outsera.worstmovie.dto.MovieResponse;
import br.com.outsera.worstmovie.dto.ProducerResponse;
import br.com.outsera.worstmovie.entity.Movie;
import br.com.outsera.worstmovie.repository.MovieRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MovieControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MovieRepository movieRepository;

    @Test
    void getProducersByIntervalCase01() throws JsonProcessingException {
        ResponseEntity<String> exchange = testRestTemplate.exchange("/producers", HttpMethod.GET, null, String.class);

        assertEquals(HttpStatus.OK, exchange.getStatusCode());

        assertThat(exchange.getBody()).isNotEmpty();

        List<MovieResponse> movieResponses = objectMapper.readValue(exchange.getBody(), new TypeReference<>() {
        });

        assertThat(movieResponses)
                .isNotEmpty()
                .hasSize(1);

        assertThat(movieResponses.getFirst().getMin())
                .isNotEmpty()
                .hasSize(1);

        assertThat(movieResponses.getFirst().getMax())
                .isNotEmpty()
                .hasSize(1);

        assertEquals(movieResponses.getFirst().getMin().getFirst(), getMinProducer());
        assertEquals(movieResponses.getFirst().getMax().getFirst(), getMaxProducer());
    }

    @Test
    void getProducersByIntervalCase02() throws JsonProcessingException {
        movieRepository.deleteAll();

        ResponseEntity<String> exchange = testRestTemplate.exchange("/producers", HttpMethod.GET, null, String.class);

        assertEquals(HttpStatus.OK, exchange.getStatusCode());

        assertThat(exchange.getBody()).isNotEmpty();

        List<MovieResponse> movieResponses = objectMapper.readValue(exchange.getBody(), new TypeReference<>() {
        });

        assertThat(movieResponses)
                .isNotEmpty()
                .hasSize(1);

        assertThat(movieResponses.getFirst().getMin())
                .hasSize(0);

        assertThat(movieResponses.getFirst().getMax())
                .hasSize(0);
    }

    @Test
    void getProducersByIntervalCase03() throws JsonProcessingException {
        movieRepository.deleteAll();

        movieRepository.saveAll(getMovieList());

        ResponseEntity<String> exchange = testRestTemplate.exchange(
                "/producers", HttpMethod.GET, null, String.class);

        assertEquals(HttpStatus.OK, exchange.getStatusCode());

        assertThat(exchange.getBody()).isNotEmpty();

        List<MovieResponse> movieResponses = objectMapper.readValue(exchange.getBody(), new TypeReference<>() {
        });

        assertThat(movieResponses)
                .isNotEmpty()
                .hasSize(1);

        assertThat(movieResponses.getFirst().getMin())
                .hasSize(2);

        assertThat(movieResponses.getFirst().getMax())
                .hasSize(2);

        assertEquals(movieResponses.getFirst().getMin().getFirst(), getProducerList().getFirst());
        assertEquals(movieResponses.getFirst().getMin().get(1), getProducerList().get(1));
        assertEquals(movieResponses.getFirst().getMax().getFirst(), getProducerList().get(2));
        assertEquals(movieResponses.getFirst().getMax().get(1), getProducerList().get(3));
    }

    private ProducerResponse getMinProducer() {
        return ProducerResponse.builder()
                .producer("Joel Silver")
                .interval(1)
                .previousWin(1990)
                .followingWin(1991)
                .build();
    }

    private ProducerResponse getMaxProducer() {
        return ProducerResponse.builder()
                .producer("Buzz Feitshans")
                .interval(9)
                .previousWin(1985)
                .followingWin(1994)
                .build();
    }

    private List<Movie> getMovieList() {
        return List.of(
                Movie.builder().release_year(1985).producers("Steve Shagan").winner(true).build(),
                Movie.builder().release_year(1990).producers("Steve Shagan").winner(true).build(),
                Movie.builder().release_year(1980).producers("Allan Carr").winner(true).build(),
                Movie.builder().release_year(1985).producers("Allan Carr").winner(true).build(),
                Movie.builder().release_year(1990).producers("Jennings Lang").winner(true).build(),
                Movie.builder().release_year(2000).producers("Jennings Lang").winner(true).build(),
                Movie.builder().release_year(2000).producers("William Frye").winner(true).build(),
                Movie.builder().release_year(2010).producers("William Frye").winner(true).build()
        );
    }

    private List<ProducerResponse> getProducerList() {
        return List.of(
                ProducerResponse.builder().producer(getMovieList().getFirst().getProducers()).interval(5)
                        .previousWin(getMovieList().getFirst().getRelease_year())
                        .followingWin(getMovieList().get(1).getRelease_year()).build(),
                ProducerResponse.builder().producer(getMovieList().get(2).getProducers()).interval(5)
                        .previousWin(getMovieList().get(2).getRelease_year())
                        .followingWin(getMovieList().get(3).getRelease_year()).build(),
                ProducerResponse.builder().producer(getMovieList().get(4).getProducers()).interval(10)
                        .previousWin(getMovieList().get(4).getRelease_year())
                        .followingWin(getMovieList().get(5).getRelease_year()).build(),
                ProducerResponse.builder().producer(getMovieList().get(6).getProducers()).interval(10)
                        .previousWin(getMovieList().get(6).getRelease_year())
                        .followingWin(getMovieList().get(7).getRelease_year()).build()
        );
    }
}
