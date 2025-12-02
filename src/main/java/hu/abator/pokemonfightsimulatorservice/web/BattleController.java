package hu.abator.pokemonfightsimulatorservice.web;

import hu.abator.pokemonfightsimulatorservice.dto.BattleDto;
import hu.abator.pokemonfightsimulatorservice.dto.BattleRequestDto;
import hu.abator.pokemonfightsimulatorservice.manager.BattleManager;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/battles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BattleController {

    BattleManager battleManager;

    @PostMapping
    public ResponseEntity<BattleDto> start(@RequestBody BattleRequestDto req) {
        return ResponseEntity.ok(battleManager.start(req));
    }

    @GetMapping
    public ResponseEntity<Set<BattleDto>> getBattles(int limit) {
        return ResponseEntity.ok(battleManager.getBattles(limit));
    }
}
