package com.pivotal.comcast.scdf;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.core.Pollers;
import org.springframework.integration.mongodb.inbound.MongoDbMessageSource;
import org.springframework.messaging.MessageChannel;

import java.util.Arrays;


@EnableBinding(Source.class)
@EnableConfigurationProperties(MongoDBProperties.class)
@Import(ServerAddressConverterConfiguration.class)
public class MongoDBSourceConfiguration {

    @Autowired private MongoDBProperties _config;

    @Autowired
    @Qualifier(Source.OUTPUT)
    private MessageChannel output;

    @Bean
    protected MongoTemplate mongoTemplate() {
        try {
            switch(_config.getHostAddresses().length) {

                case 0:
                    return new MongoTemplate(new SimpleMongoDbFactory(new MongoClient(
                            new ServerAddress(ServerAddress.defaultHost(), ServerAddress.defaultPort())), _config.getDb()));
                case 1:
                    return new MongoTemplate(new SimpleMongoDbFactory(
                            new MongoClient(_config.getHostAddresses()[0]), _config.getDb()));
                default:
                    return new MongoTemplate(new SimpleMongoDbFactory(
                            new MongoClient(Arrays.asList(_config.getHostAddresses())), _config.getDb()));
            }

        } catch (Exception ex) {
            throw new BeanCreationException(ex.getMessage(), ex);
        }
    }

    @Bean
    protected MessageSource<Object> mongoSource() {
        MongoDbMessageSource ms = new MongoDbMessageSource(mongoTemplate(), new LiteralExpression(_config.getQuery()));
        ms.setCollectionNameExpression(new LiteralExpression(_config.getCollection()));
        return ms;
    }

    @Bean
    public IntegrationFlow startFlow() throws Exception {
        return IntegrationFlows.from(mongoSource(),
                c -> c.poller(Pollers.fixedRate(10000)))
                .split()
                //.transform(Object::toString)
                //.handle(System.out::println)
                .channel(output)
                .get();
    }

}
