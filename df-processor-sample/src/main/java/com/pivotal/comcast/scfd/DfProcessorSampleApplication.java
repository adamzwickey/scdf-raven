package com.pivotal.comcast.scfd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;

@SpringBootApplication
@EnableBinding(Processor.class)
public class DfProcessorSampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(DfProcessorSampleApplication.class, args);
	}

	protected final Logger LOG = LoggerFactory.getLogger(DfProcessorSampleApplication.class);

	@Transformer(inputChannel = Processor.INPUT, outputChannel = Processor.OUTPUT)
	public Object customProcessor(Message<?> message) {
		LOG.info("Processing message with payload: %s", message.getPayload());
		return message.getPayload().toString().toUpperCase();
	}
}
