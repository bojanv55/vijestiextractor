package me.vukas;

import java.util.Optional;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
public class Runner implements CommandLineRunner {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private RijecRepository rijecRepository;

	final private Pattern p = Pattern.compile("\\w+",Pattern.UNICODE_CHARACTER_CLASS);

    @Override
    public void run(String... args) throws Exception {
        System.out.println("all ok");

        //List<Article> al = articleRepository.findAll();

        int page = 0;
        Page<Article> pages;

        do {
            pages = articleRepository.findAll(PageRequest.of(page, 10)); //fetch 10 records at once

            for (Article a : pages) {
							String[] titleList = p.matcher(a.getTitle()).results().map(MatchResult::group).toArray(String[]::new);
							String[] contentList = p.matcher(a.getContent()).results().map(MatchResult::group).toArray(String[]::new);


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
