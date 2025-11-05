package br.com.outsera.worstmovie.service;

import br.com.outsera.worstmovie.dto.MovieResponse;
import br.com.outsera.worstmovie.entity.Movie;
import br.com.outsera.worstmovie.mapper.MovieResponseMapper;
import br.com.outsera.worstmovie.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

import static org.apache.commons.lang3.BooleanUtils.isFalse;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;

    private final MovieResponseMapper movieResponseMapper;

    public List<MovieResponse> getProducersByInterval() {

        List<Movie> allByWinner = movieRepository.findAllByWinner(true);

        Map<String, List<Integer>> yearMap = new HashMap<>();
        Map<String, List<Integer>> minYearMap = new HashMap<>();
        Map<String, List<Integer>> maxYearMap = new HashMap<>();

        separateYearsByProducers(allByWinner, yearMap);

        calculateInterval(yearMap, minYearMap, maxYearMap);

        return movieResponseMapper.toMovieResponse(minYearMap, maxYearMap);
    }

    private void separateYearsByProducers(List<Movie> allByWinner, Map<String, List<Integer>> yearMap) {

        allByWinner.forEach(movie -> {
            List<String> producersList = Arrays.stream(movie.getProducers().split(",|\\s+and\\s+")).toList();

            producersList.forEach(producer -> {
                if (isFalse(yearMap.containsKey(producer))) {
                    yearMap.put(producer.trim(), new ArrayList<>(List.of(movie.getRelease_year())));
                } else {
                    List<Integer> producerYear = yearMap.get(producer);
                    producerYear.add(movie.getRelease_year());
                    yearMap.put(producer.trim(), producerYear);
                }
            });
        });
    }

    private void calculateInterval(
            Map<String, List<Integer>> yearMap, Map<String, List<Integer>> minYearMap,
            Map<String, List<Integer>> maxYearMap) {

        AtomicInteger minInterval = new AtomicInteger(Integer.MAX_VALUE);
        AtomicInteger minStartYear = new AtomicInteger();
        AtomicInteger minEndYear = new AtomicInteger();
        AtomicReference<String> minProducer = new AtomicReference<>();

        AtomicInteger maxInterval = new AtomicInteger();
        AtomicInteger maxStartYear = new AtomicInteger();
        AtomicInteger maxEndYear = new AtomicInteger();
        AtomicReference<String> maxProducer = new AtomicReference<>();

        yearMap.forEach((producer, yearList) -> {
            Collections.sort(yearList);

            checkMinorAndMayorIntervalByProducer(
                    producer, yearList, minInterval, minStartYear, minEndYear, minProducer,
                    maxInterval, maxStartYear, maxEndYear, maxProducer);

            updateMinAndMaxYearMaps(
                    minYearMap, Integer.MAX_VALUE, minInterval.get(),
                    minEndYear.get() - minStartYear.get(),
                    minStartYear.get(), minEndYear.get(), minProducer.get());

            updateMinAndMaxYearMaps(maxYearMap, 0, maxInterval.get(),
                    maxEndYear.get() - maxStartYear.get(),
                    maxStartYear.get(), maxEndYear.get(), maxProducer.get());
        });
    }

    private void updateMinAndMaxYearMaps(
            Map<String, List<Integer>> minOrMaxYearMap, int minOrMaxIntervalStep, int interval, int minOrMaxInterval,
            int startYear, int endYear, String producer) {

        if (isFalse(minOrMaxYearMap.isEmpty())) {
            String key = minOrMaxYearMap.keySet().stream().toList().getFirst();
            Integer minOrMaxStartYearValue = minOrMaxYearMap.get(key).getFirst();
            Integer minOrMaxEndYearValue = minOrMaxYearMap.get(key).get(1);

            int intervalValue = minOrMaxEndYearValue - minOrMaxStartYearValue;

            if ((minOrMaxIntervalStep == Integer.MAX_VALUE && interval < Integer.MAX_VALUE
                    && intervalValue == minOrMaxInterval)
                    || (minOrMaxIntervalStep == 0 && interval > 0 && intervalValue == minOrMaxInterval)) {

                ArrayList<Integer> minOrMaxYearList = new ArrayList<>();
                minOrMaxYearList.add(startYear);
                minOrMaxYearList.add(endYear);

                minOrMaxYearMap.put(producer, minOrMaxYearList);
            } else {
                minOrMaxYearMap.clear();

                ArrayList<Integer> minYearList = new ArrayList<>();
                minYearList.add(startYear);
                minYearList.add(endYear);

                minOrMaxYearMap.put(producer, minYearList);
            }
        } else if (interval > 0) {
            minOrMaxYearMap.clear();

            ArrayList<Integer> minYearList = new ArrayList<>();
            minYearList.add(startYear);
            minYearList.add(endYear);

            minOrMaxYearMap.put(producer, minYearList);
        }
    }

    private void checkMinorAndMayorIntervalByProducer(
            String producer, List<Integer> yearList, AtomicInteger minInterval, AtomicInteger minStartYear,
            AtomicInteger minEndYear, AtomicReference<String> minProducer, AtomicInteger maxInterval,
            AtomicInteger maxStartYear, AtomicInteger maxEndYear, AtomicReference<String> maxProducer) {

        IntStream.range(0, yearList.size() - 1).forEach(i -> {
            int year = yearList.get(i);
            int nextYear = yearList.get(i + 1);
            int intervalIterator = nextYear - year;

            if (intervalIterator > 0 && intervalIterator <= minInterval.get()) {
                minInterval.set(intervalIterator);
                minStartYear.set(year);
                minEndYear.set(nextYear);
                minProducer.set(producer);
            }

            if (intervalIterator > 0 && intervalIterator >= maxInterval.get()) {
                maxInterval.set(intervalIterator);
                maxStartYear.set(year);
                maxEndYear.set(nextYear);
                maxProducer.set(producer);
            }
        });
    }
}
