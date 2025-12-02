package hu.abator.pokemonfightsimulatorservice.mapper;

import com.triceracode.pokeapi.model.resource.pokemon.Pokemon;
import hu.abator.pokemonfightsimulatorservice.domain.PokemonEntity;
import hu.abator.pokemonfightsimulatorservice.dto.PokemonDto;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class PokemonMapper {

    public PokemonDto toDto(PokemonEntity pokemon) {
        return PokemonDto.builder()
                .name(pokemon.getName())
                .power(pokemon.getPower())
                .pictureUrl(pokemon.getPictureUrl())
                .type(pokemon.getType())
                .build();
    }

    public PokemonEntity toEntity(Pokemon pokemon){
        return PokemonEntity.builder()
                .power(getRandomInt(1,20))
                .type(pokemon.getTypes().get(0).getType().getName())
                .name(pokemon.getName())
                .pictureUrl(pokemon.getSprites().getFrontDefault())
                .build();
    }

    private int getRandomInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

}
