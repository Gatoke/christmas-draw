package io.github.gatoke.christmasdraw.port.adapter.configuration;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import java.time.OffsetDateTime;
import java.util.Date;

import static java.time.Clock.systemUTC;

@EnableReactiveMongoRepositories
public class MongoConfiguration extends AbstractReactiveMongoConfiguration {//todo can i remove it

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create();
    }

    @Override
    protected String getDatabaseName() {
        return "christmas-draw";
    }

    @Override
    protected void configureConverters(final MongoCustomConversions.MongoConverterConfigurationAdapter converterConfigurationAdapter) {
        converterConfigurationAdapter.registerConverter(new DateToOffsetDateTimeConverter());
        converterConfigurationAdapter.registerConverter(new OffsetDateTimeToDateConverter());
        super.configureConverters(converterConfigurationAdapter);
    }

    private static class OffsetDateTimeToDateConverter implements Converter<OffsetDateTime, Date> {
        @Override
        public Date convert(final OffsetDateTime source) {
            return source == null ? null : Date.from(source.toInstant());
        }
    }

    private static class DateToOffsetDateTimeConverter implements Converter<Date, OffsetDateTime> {

        @Override
        public OffsetDateTime convert(final Date source) {
            return source == null ? null : OffsetDateTime.ofInstant(source.toInstant(), systemUTC().getZone());
        }
    }
}
