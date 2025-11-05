package br.com.outsera.worstmovie.mapper;

import br.com.outsera.worstmovie.dto.ProducerResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ProducerResponseMapper {

    public List<ProducerResponse> minYearMapToProducerResponseList(Map<String, List<Integer>> minYearMap) {
        return minYearMap.entrySet().stream()
                .map(entry -> ProducerResponse.builder()
                        .producer(entry.getKey())
                        .interval(entry.getValue().get(1) - entry.getValue().getFirst())
                        .previousWin(entry.getValue().getFirst())
                        .followingWin(entry.getValue().get(1))
                        .build())
                .toList();
    }

    public List<ProducerResponse> maxYearMapToProducerResponseList(Map<String, List<Integer>> maxYearMap) {
        return maxYearMap.entrySet().stream()
                .map(entry -> ProducerResponse.builder()
                        .producer(entry.getKey())
                        .interval(entry.getValue().get(1) - entry.getValue().getFirst())
                        .previousWin(entry.getValue().getFirst())
                        .followingWin(entry.getValue().get(1))
                        .build())
                .toList();
    }
}
