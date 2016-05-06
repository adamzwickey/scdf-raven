package com.pivotal.comcast.scdf;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.dataflow.shell.EnableDataFlowShell;

@SpringBootApplication
@EnableDataFlowShell
public class DfShellApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(DfShellApplication.class)
				.bannerMode(Banner.Mode.OFF)
				.run(args);
	}
}
