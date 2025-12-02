package hu.abator.pokemonfightsimulatorservice.dto;

import lombok.Data;

@Data
public class BattleRequestDto {
    private String pokemon1Name;
    private String pokemon2Name;
}
