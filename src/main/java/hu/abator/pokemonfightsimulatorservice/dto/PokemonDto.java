package hu.abator.pokemonfightsimulatorservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PokemonDto {
    private String name;
    private String pictureUrl;
    private String type;
    private int power;
}
