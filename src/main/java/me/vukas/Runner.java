package me.vukas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Component
public class Runner implements CommandLineRunner {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private RijecRepository rijecRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("all ok");

        //List<Article> al = articleRepository.findAll();

        int page = 0;
        Page<Article> pages;

        do {
            pages = articleRepository.findAll(PageRequest.of(page, 10)); //fetch 10 records at once

            for (Article a : pages) {
                String[] titleList = a.getTitle()
                        .split(" ");
                String[] contentList = a.getContent()
                        .split(" ");

                String[] result = Stream.of(titleList, contentList).flatMap(Stream::of).toArray(String[]::new);

                for (String ts1 : result) {
                    String ts = ts1.toLowerCase();
                    Optional<Rijec> r = rijecRepository.findByRijec(ts);
                    Rijec zapamti = r.orElseGet(() -> {
                        Rijec rch = new Rijec();
                        rch.setRijec(ts);
                        rch.setPojavljivanja(0);
                        return rch;
                    });
                    zapamti.setPojavljivanja(zapamti.getPojavljivanja() + 1);
                    rijecRepository.save(zapamti);

                    System.out.println("Rijec :" + zapamti.getRijec() + " ; ponavljanja: " + zapamti.getPojavljivanja());
                }
            }
            page++;
        }while (pages.hasNext());
    }
}
