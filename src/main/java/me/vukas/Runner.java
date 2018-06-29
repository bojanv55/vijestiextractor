package me.vukas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class Runner implements CommandLineRunner {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private RijecRepository rijecRepository;

    final private Pattern p = Pattern.compile("\\w+",Pattern.UNICODE_CHARACTER_CLASS);

    @Override
    @Transactional(readOnly = true)
    public void run(String... args) throws Exception {
        System.out.println("all ok");

        //mora u try zbog close() na kraju streama
        try(Stream<Article> al = articleRepository.streamAll()) {

            al.forEach(a -> {

                String[] titleList = p.matcher(a.getTitle()).results().map(MatchResult::group).toArray(String[]::new);
                String[] contentList = p.matcher(a.getContent()).results().map(MatchResult::group).toArray(String[]::new);

                String[] result = Stream.of(titleList, contentList).flatMap(Stream::of).toArray(String[]::new);

                saveNew(result);
            });
        }
    }

    public void saveNewFake(String[] result){
        String cs = "jdbc:mysql://10.10.121.137:33306/crawled?useUnicode=yes&characterEncoding=UTF-8&serverTimezone=Europe/Oslo";
        try(Connection c = DriverManager.getConnection(cs, "root", "123456");
            Statement st1 = c.createStatement();) {
            int res = st1.executeUpdate("INSERT INTO rijeci (rijec, pojavljivanja) VALUES ('asd',4)");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void saveNew(String[] result){
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
            rijecRepository.saveN(zapamti);

            System.out.println("Rijec :" + zapamti.getRijec() + " ; ponavljanja: " + zapamti.getPojavljivanja());
        }
    }

    /**
     * THIS IS PAGING RUNNER - GOES PAGE BY PAGE AND PROCESSES DATA
     */

    //    @Override
//    public void run(String... args) throws Exception {
//        System.out.println("all ok");
//
//        //List<Article> al = articleRepository.findAll();
//
//        int page = 0;
//        Page<Article> pages;
//
//        do {
//            pages = articleRepository.findAll(PageRequest.of(page, 10)); //fetch 10 records at once
//
//            for (Article a : pages) {
//
//                String[] titleList = p.matcher(a.getTitle()).results().map(MatchResult::group).toArray(String[]::new);
//                String[] contentList = p.matcher(a.getContent()).results().map(MatchResult::group).toArray(String[]::new);
//
////                String[] titleList = a.getTitle()
////                        .split("[^a-zA-Z]+");
////                String[] contentList = a.getContent()
////                        .split("[^a-zA-Z]+");
//
//                String[] result = Stream.of(titleList, contentList).flatMap(Stream::of).toArray(String[]::new);
//
//                for (String ts1 : result) {
//                    String ts = ts1.toLowerCase();
//                    Optional<Rijec> r = rijecRepository.findByRijec(ts);
//                    Rijec zapamti = r.orElseGet(() -> {
//                        Rijec rch = new Rijec();
//                        rch.setRijec(ts);
//                        rch.setPojavljivanja(0);
//                        return rch;
//                    });
//                    zapamti.setPojavljivanja(zapamti.getPojavljivanja() + 1);
//                    rijecRepository.save(zapamti);
//
//                    System.out.println("Rijec :" + zapamti.getRijec() + " ; ponavljanja: " + zapamti.getPojavljivanja());
//                }
//            }
//            page++;
//        }while (pages.hasNext());
//    }
}
