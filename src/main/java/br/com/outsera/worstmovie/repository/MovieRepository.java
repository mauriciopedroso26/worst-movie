package br.com.outsera.worstmovie.repository;

import br.com.outsera.worstmovie.entity.Movie;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends CrudRepository<Movie, Long> {
    List<Movie> findAllByWinner(boolean winner);
}
