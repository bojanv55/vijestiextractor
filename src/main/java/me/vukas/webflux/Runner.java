package me.vukas.webflux;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.handler.timeout.ReadTimeoutHandler;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.retry.Retry;

import org.springframework.boot.CommandLineRunner;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class Runner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {

        Mono.just("Craig")
                .map(String::toUpperCase)
                .map(x -> "Cao " + x)
                .subscribe(System.out::println);

        Flux<String> ffs = Flux.just("a", "b", "c");

        //koristi se za testiranje
//        StepVerifier.create(ffs)
//                .expectNext("a")
//                .expectNext("b")
//                .expectNext("c")
//                .verifyComplete();

        //ffs = Flux.fromIterable(Arrays.asList("1", "2", "3"));
        ffs = Flux.fromStream(Stream.of("1", "2", "3", "4"));

        ffs.subscribe(f -> System.out.println("pr: " + f));


        Flux.range(1,50).subscribe(System.out::println);

        //mora biti webapp ili sleep jer je ocigledno neki deamon thread
        //svake 2 sekunde emituje elemente od 0..9
        //Flux.interval(Duration.ofSeconds(2)).take(10).subscribe(System.out::println);

        //merge 2 fluxs
        Flux<Integer> fi1 = Flux.fromStream(Stream.iterate(99, x -> x+1).limit(10))
                .delayElements(Duration.ofMillis(500));
        Flux<Integer> fi2 = Flux.fromStream(Stream.iterate(120, x -> x+1).limit(10))
                .delaySubscription(Duration.ofMillis(250)) //nemoj odma da emitujes poslije subscr
                .delayElements(Duration.ofMillis(500)); //emitovanje je odlozeno za po 500 ms

        //fi1.mergeWith(fi2).subscribe(System.out::println);
        Flux.merge(fi1, fi2).subscribe(System.out::println);

        Flux<Integer> fi21 = Flux.range(99,10);
        Flux<Integer> fi22 = Flux.range(120,10);

        Flux.zip(fi21, fi22).subscribe(System.out::println); // [99, 120], [100,121], ...
        //flux se ne trosi kao Stream (koji moze samo 1)
        Flux.zip(fi21, fi22, (a,b) -> a + " + " + b).subscribe(System.out::println); // 99+120...

        //odabere brzi od 2 fluxa
        Flux.first(fi21, fi22.delaySubscription(Duration.ofMillis(100)))
                .subscribe(System.out::println);    //samo ce fi21 da se publishuje jer je drugi spor


        //preskace prvih 10, pa svaki sledeci stampa po 1 sekund a dodatno preskoci jos 4 zbog duration skipa
        //take ogranicava na 20 elemenata poslije skipa
        Flux.range(1,100).skip(10).take(20).delayElements(Duration.ofSeconds(1))
                .skip(Duration.ofSeconds(5)).subscribe(System.out::println);

        Flux.just("e","r","z","r","r","g")
                .distinct() //samo jedno r
                .filter(x -> x.equals("r")) //samo r
                .map(x -> "|"+x+"|")    //garantuje redosljed
                .subscribe(System.out::println);

        //mapping moze da se odradi asynch i parallelno? Nije garantovan redosljed
        Flux.just("a", "b", "c", "d", "e", "f", "g") //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                .flatMap(n -> Mono.just(n)
                .map(f -> f.toUpperCase()).subscribeOn(Schedulers.parallel())
                ).subscribe(System.out::println);


        //grupise u liste od po 3
        Flux.just("a", "b", "c", "d", "e", "f", "g").buffer(3)
                .subscribe(System.out::println);    // [a,b,c], [d,e,f] ...

        Flux.just("x", "y", "z", "y", "j", "r", "d").buffer(2)
                .flatMap(x ->
                        Flux.fromIterable(x)    //svaki array koji je bufferovan se obradjuje u novom tredu
                        .map(y -> y.toUpperCase())
                        .subscribeOn(Schedulers.parallel())
                        .log()  //prikazuje se u log-u
                ).subscribe();


        Mono<List<String>> mls = Flux.just("x", "y", "z", "y", "j", "r", "d").collectList();
        Mono<Map<String, String>> mmf = Flux.just("x", "y", "z", "y", "j", "r", "d").collectMap(k -> k.toUpperCase());


        Flux.just("x", "y", "z", "y", "j", "r", "d").any(x -> x.equals("x")).subscribe(System.out::println);
        Flux.just("x", "y", "z", "y", "j", "r", "d").any(x -> x.equals("g")).subscribe(System.out::println);

        //WEB CLIENT

        ReactorClientHttpConnector connector = new ReactorClientHttpConnector(opt -> opt
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30_000)	//IOException if cannot connect in X MILLIS, 30sec default
            .compression(true)
            .afterNettyContextInit(ctx -> {
                ctx.addHandlerLast(new ReadTimeoutHandler(30_000 /*5000*/, TimeUnit.MILLISECONDS));	//RuntimeException - Timeout if no data in X MILLIS from server
            })
        );

        WebClient wc = WebClient.builder()
            .clientConnector(connector)
            .build();

        wc.get().uri("http://10.10.121.178:9022/matches")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .retrieve()
            .bodyToFlux(String.class)
            .retryWhen(
                Retry.anyOf(IOException.class, ReadTimeoutException.class)
                    .exponentialBackoff(Duration.ofMillis(100), Duration.ofSeconds(60))
                    .retryMax(Integer.MAX_VALUE)
            )
            .subscribe(System.out::println);
    }
}
