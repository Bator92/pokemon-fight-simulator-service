package hu.abator.pokemonfightsimulatorservice.config;

import com.triceracode.pokeapi.PokeAPIResourceService;
import com.triceracode.pokeapi.PokeAPIService;
import com.triceracode.pokeapi.imp.PokeAPIResourceServiceImp;
import com.triceracode.pokeapi.imp.PokeAPIServiceImp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class PokeApiConfig {

    @Bean
    public PokeAPIResourceService pokeAPIResourceService() {
        return new PokeAPIResourceServiceImp();
    }

    @Bean
    public PokeAPIService pokeAPIService() {
        return new PokeAPIServiceImp();
    }
}