package com.example.kafkaspringcloudstream.handlers;

import com.example.kafkaspringcloudstream.events.PageEvent;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Component
public class PageEventHandler {

    @Bean
    public Consumer<PageEvent> pageEventConsumer(){
        return (input)->{
            System.out.println("******************************");
            System.out.println(input.toString());
            System.out.println("******************************");
        };
    }

    @Bean
    public Supplier<PageEvent> pageEventSupplier(){
        return () ->{
            return new PageEvent(Math.random()>0.5?"P1":"P2",
                    Math.random()>0.5?"U1":"U2",
                    new Date(),
                   10+new Random().nextInt(10000));
        };
    }


    ////long is the number of visit
    @Bean
    public Function<KStream<String, PageEvent>, KStream<String, Long>> kStreamFunction(){
        return (stream)->
                stream
                        //.filter((k,v)->v.duration()>100) // je prend un stream et je vais prendre en cosidreation que le page event avec durée > 100
                        .map((k,v)->new KeyValue<>(v.name(), 0L)) // on va mapper par nom de la page et le durée
                        .groupByKey(Grouped.with(Serdes.String(), Serdes.Long())) // grouper par clé , Serdes.string correspond au v.name pour le serialisé et serdes.long et pour la duration
                        .windowedBy(TimeWindows.of(Duration.ofSeconds(5)))//le count va concerné que les 5 derniers secondes.
                        //.aggregate(()->0.0, (k,v,total)->total+v, Materialized.as("total-store"))
                        .count(Materialized.as("count-store")) // compter le nombre de visite et l'enregsitre dans count-store pour l'afficher en front
                        .toStream()
                        .map((k,v)->new KeyValue<>(k.key(), v)) //on a ajouter ça aprés le windows puisque le retour a changé
                ;

    }

}
