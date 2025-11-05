package br.com.outsera.worstmovie.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProducerResponse {

    private String producer;
    private int interval;
    private int previousWin;
    private int followingWin;
}
