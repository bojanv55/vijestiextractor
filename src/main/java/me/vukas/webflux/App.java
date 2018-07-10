package me.vukas.webflux;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class})
public class App {
    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(App.class)
                .bannerMode(Banner.Mode.OFF)
                //.web(WebApplicationType.NONE)
                .build()
                .run(args);
    }

//    @Bean
//    public RouterFunction<?> routerFunction(){
//        return route(GET("/x/y"), this::resp1)
//                .andRoute(POST("/x/x"), this::resp1);
//    }
//
//    public Mono<ServerResponse> resp1(ServerRequest request){
//        return ok().body(just("Cao"), String.class);
//    }
}
