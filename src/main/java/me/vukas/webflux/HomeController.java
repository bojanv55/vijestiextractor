package me.vukas.webflux;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@RestController
@RequestMapping(path = "/a")
public class HomeController {

    private SomeRepository someRepository;

    public HomeController(SomeRepository someRepository) {
        this.someRepository = someRepository;
    }

    @GetMapping("/b")
    public Flux<Osoba> dajSve(){
        return someRepository.findAll().take(10);
    }

    @GetMapping("/c")
    public Mono<Osoba> dajJedan(){
        return someRepository.findById(1);
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Osoba> postOsoba(@RequestBody Mono<Osoba> osobaMono){
        return someRepository.saveAll(osobaMono).next();
    }

    @PostMapping("/e")
    public Mono<Void> doSmt(){
        return Mono.when(x -> x.onComplete());//??
    }

}
