package hu.abator.pokemonfightsimulatorservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BattleDto {
    private UUID id;
    private PokemonDto winnerPokemon;
    private PokemonDto looserPokemon;
    private OffsetDateTime createdAt;
}
