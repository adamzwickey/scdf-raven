package com.pivotal.comcast.scdf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.dataflow.server.EnableDataFlowServer;

@SpringBootApplication
@EnableDataFlowServer
public class DfServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DfServerApplication.class, args);
	}
}
