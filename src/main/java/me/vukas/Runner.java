package me.vukas;

import java.util.HashMap;
import java.util.Map;
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
    @Autowired
    private TrigramRepository trigramRepository;

	final private Pattern p = Pattern.compile("\\w+",Pattern.UNICODE_CHARACTER_CLASS);

	  //@Transactional
    @Override
    public void run(String... args) throws Exception {
        System.out.println("all ok");

        //List<Article> al = articleRepository.findAll();

        int page = 0;
        Page<Article> pages;

        double cnt = articleRepository.count();
        long processed = 0;

        do {
            System.out.println("Page no. " + page);
            pages = articleRepository.findAll(PageRequest.of(page, 50)); //fetch 10000 records at once

            Map<TrigramKey, Trigram> trigs = new HashMap<>();
            Map<String, Rijec> rijeci = new HashMap<>();

            for (Article a : pages) {
							String[] titleList = p.matcher(a.getTitle()).results().map(MatchResult::group).map(String::toLowerCase).toArray(String[]::new);
							String[] contentList = p.matcher(a.getContent()).results().map(MatchResult::group).map(String::toLowerCase).toArray(String[]::new);

							//----


                for(int i=0; i<contentList.length-1; i++){
                    String[] trig = new String[3];
                    if(i==0){
                        trig[0] = "<s>";
                        trig[1] = "<s>";
                        trig[2] = contentList[i];
                    }
                    else if(i==1){
                        trig[0] = "<s>";
                        trig[1] = contentList[i];
                        trig[2] = contentList[i+1];
                    }
                    else if(i==contentList.length-2){
                        trig[0] = contentList[i];
                        trig[1] = contentList[i+1];
                        trig[2] = "</s>";
                    }
                    else {
                        trig[0] = contentList[i];
                        trig[1] = contentList[i+1];
                        trig[2] = contentList[i+2];
                    }

                    TrigramKey tgk = new TrigramKey(trig[0], trig[1], trig[2]);
                    Optional<Trigram> trge = trigramRepository.findByKey(tgk);
                    Trigram zapamti = trge.orElseGet(() -> trigs.computeIfAbsent(tgk, v -> {
                        Trigram tn = new Trigram();
                        tn.setKey(tgk);
                        tn.setPonavljanja(0);
                        return tn;
                    }));
                    zapamti.setPonavljanja(zapamti.getPonavljanja()+1);
                    trigs.put(zapamti.getKey(), zapamti);

                }
                //trigramRepository.saveAll(trigs);


							//---

							String[] result = Stream.of(titleList, contentList).flatMap(Stream::of).toArray(String[]::new);


                for (String ts1 : result) {
                    String ts = ts1;//.toLowerCase();
                    Optional<Rijec> r = rijecRepository.findByRijec(ts);
                    Rijec zapamti = r.orElseGet(() -> rijeci.computeIfAbsent(ts, v -> {
                        Rijec rch = new Rijec();
                        rch.setRijec(ts);
                        rch.setPojavljivanja(0);
                        return rch;
                    }));
                    zapamti.setPojavljivanja(zapamti.getPojavljivanja() + 1);
                    rijeci.put(zapamti.getRijec(), zapamti);


                    //System.out.println("Rijec :" + zapamti.getRijec() + " ; ponavljanja: " + zapamti.getPojavljivanja());
                }
                //rijecRepository.saveAll(rijeci);


                processed++;
                System.out.println("Remaining : " + ((1-((processed)/cnt))*100) + "%");
            }

            trigramRepository.saveAll(trigs.values());
            rijecRepository.saveAll(rijeci.values());

            page++;
        }while (pages.hasNext());
    }
}
