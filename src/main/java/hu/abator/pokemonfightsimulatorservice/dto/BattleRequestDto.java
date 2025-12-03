package hu.abator.pokemonfightsimulatorservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BattleRequestDto {
    private String pokemon1Name;
    private String pokemon2Name;
}
