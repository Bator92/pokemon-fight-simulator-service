package hu.abator.pokemonfightsimulatorservice.service;

import hu.abator.pokemonfightsimulatorservice.domain.*;
import hu.abator.pokemonfightsimulatorservice.exception.CannotDetermineWinnerException;
import hu.abator.pokemonfightsimulatorservice.repository.BattleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.Set;

import static org.springframework.data.domain.Pageable.ofSize;

@Service
@RequiredArgsConstructor
@FieldDefaults( makeFinal = true, level = AccessLevel.PRIVATE)
public class BattleService {

    BattleRepository battleRepository;

    public BattleEntity save(BattleEntity battleEntity) {
        return battleRepository.save(battleEntity);
    }

    public PokemonEntity selectWinner(PokemonEntity pokemon1, PokemonEntity pokemon2) throws CannotDetermineWinnerException {
        if(pokemon1.getPower() > pokemon2.getPower()){
            return pokemon1;
        } else if(pokemon2.getPower() > pokemon1.getPower()){
            return pokemon2;
        } else {
            throw new CannotDetermineWinnerException();
        }
    }

    public @Nullable Set<BattleEntity> findBattles(int limit) {
        return battleRepository.findAllByOrderByCreatedAtDesc(ofSize(limit));
    }
}
