package br.com.outsera.worstmovie.mapper;

import br.com.outsera.worstmovie.dto.MovieResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class MovieResponseMapper {

    private final ProducerResponseMapper producerResponseMapper;

    public MovieResponse toMovieResponse(
            Map<String, List<Integer>> minYearMap, Map<String, List<Integer>> maxYearMap) {

        return MovieResponse.builder()
                .min(producerResponseMapper.minYearMapToProducerResponseList(minYearMap))
                .max(producerResponseMapper.maxYearMapToProducerResponseList(maxYearMap))
                .build();
    }
}
