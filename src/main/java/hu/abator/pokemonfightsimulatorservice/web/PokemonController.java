package hu.abator.pokemonfightsimulatorservice.web;

import hu.abator.pokemonfightsimulatorservice.dto.RandomFightingPairDto;
import hu.abator.pokemonfightsimulatorservice.manager.PokemonManager;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pokemons")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PokemonController {
    PokemonManager pokemonManager;

    @GetMapping("/random-fighting-pair")
    ResponseEntity<RandomFightingPairDto> getRandomFightingPair(){
        return ResponseEntity.ok(pokemonManager.getRandomFightingPair());
    }



}
