package br.com.outsera.worstmovie.controller;

import br.com.outsera.worstmovie.dto.MovieResponse;
import br.com.outsera.worstmovie.repository.MovieRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MovieControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MovieRepository movieRepository;

    @Test
    void getProducersByIntervalWithExactlyData() throws IOException {
        ResponseEntity<String> exchange = testRestTemplate.exchange("/producers", HttpMethod.GET, null, String.class);

        assertEquals(HttpStatus.OK, exchange.getStatusCode());

        assertThat(exchange.getBody()).isNotEmpty();

        String expectedResponse = Files.readString(Paths.get("src/test/resources/expected-response.json"));

        MovieResponse movieResponse = objectMapper.readValue(exchange.getBody(), MovieResponse.class);
        MovieResponse movieExpectedResponse = objectMapper.readValue(expectedResponse, MovieResponse.class);

        assertThat(movieResponse).isNotNull();
        assertThat(movieExpectedResponse).isNotNull();

        assertEquals(movieExpectedResponse, movieResponse);
    }

    @Test
    void getProducersByIntervalWithModifiedData() throws IOException {
        movieRepository.deleteAll();

        ResponseEntity<String> exchange = testRestTemplate.exchange("/producers", HttpMethod.GET, null, String.class);

        assertEquals(HttpStatus.OK, exchange.getStatusCode());

        assertThat(exchange.getBody()).isNotEmpty();

        String expectedResponse = Files.readString(Paths.get("src/test/resources/expected-response.json"));

        MovieResponse movieResponse = objectMapper.readValue(exchange.getBody(), MovieResponse.class);
        MovieResponse movieExpectedResponse = objectMapper.readValue(expectedResponse, MovieResponse.class);

        assertThat(movieResponse).isNotNull();

        assertNotEquals(movieExpectedResponse, movieResponse);
    }

    @Test
    void getProducersByIntervalModifiedDataWithoutModifyExpectResult() throws IOException {
        movieRepository.deleteAllById(List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L));

        ResponseEntity<String> exchange = testRestTemplate.exchange("/producers", HttpMethod.GET, null, String.class);

        assertEquals(HttpStatus.OK, exchange.getStatusCode());

        assertThat(exchange.getBody()).isNotEmpty();

        String expectedResponse = Files.readString(Paths.get("src/test/resources/expected-response.json"));

        MovieResponse movieResponse = objectMapper.readValue(exchange.getBody(), MovieResponse.class);
        MovieResponse movieExpectedResponse = objectMapper.readValue(expectedResponse, MovieResponse.class);

        assertThat(movieResponse).isNotNull();

        assertEquals(movieExpectedResponse, movieResponse);
    }
}
