package hu.abator.pokemonfightsimulatorservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RandomFightingPairDto {
    PokemonDto pokemon1;
    PokemonDto pokemon2;
}
