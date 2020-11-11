package io.github.gatoke.christmasdraw.port.adapter.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import javax.annotation.PostConstruct;

@Configuration
@RequiredArgsConstructor
public class MongoConfiguration {

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    @PostConstruct
    void configure() {
        reactiveMongoTemplate.createCollection("element", CollectionOptions
                .empty()
                .capped()
                .size(4096)
                .maxDocuments(10000))
                .subscribe();
    }
}
