package com.pivotal.comcast.scfd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Publisher;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.config.EnablePublisher;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@SpringBootApplication
@RestController
@EnableBinding(Source.class)
public class DfSourceSampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(DfSourceSampleApplication.class, args);
	}

	protected final Logger LOG = LoggerFactory.getLogger(DfSourceSampleApplication.class);

	@Autowired Source _channels;

	@RequestMapping("/event")
	public String event(String msg) {
		//invoke data microservice stream
		_channels.output().send(MessageBuilder.createMessage(
				msg,
				new MessageHeaders(Collections.singletonMap("Header1", "Sent from data microservice")))
		);
		LOG.info("processed message");
		return "event[" + msg + "] placed on streaming bus";
	}
}
