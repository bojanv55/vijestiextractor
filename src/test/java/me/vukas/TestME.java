package me.vukas;


import me.vukas.webflux.HomeController;
import me.vukas.webflux.Osoba;
import me.vukas.webflux.SomeRepository;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import static org.mockito.Mockito.when;

public class TestME {

    @Test
    public void nesto(){

        Osoba o = new Osoba();

        Flux<Osoba> tfls = Flux.just(o);

        SomeRepository sr = Mockito.mock(SomeRepository.class);
        when(sr.findAll()).thenReturn(tfls);


        WebTestClient wtc = WebTestClient.bindToController(
                new HomeController(sr)
        ).build();

        wtc.get().uri("/a/b")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isArray()
        .jsonPath("$[0].id").isEqualTo(null);

        wtc.get().uri("/a/b")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Osoba.class)
                .contains(o);

    }

}
