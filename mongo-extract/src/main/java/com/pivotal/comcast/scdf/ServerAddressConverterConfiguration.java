package com.pivotal.comcast.scdf;

import com.mongodb.ServerAddress;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;

import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerAddressConverterConfiguration {

	@Bean
	@ConfigurationPropertiesBinding
	public Converter<String, ServerAddress> serverAddressConverter() {
		return new ServerAddressConverter();
	}

	public static class ServerAddressConverter implements Converter<String, ServerAddress> {

		private static final Pattern HOST_AND_PORT_PATTERN = Pattern.compile("^\\s*(.*?):(\\d+)\\s*$");

		@Override
		public ServerAddress convert(String hostAddress) {
			try {
				Matcher m = HOST_AND_PORT_PATTERN.matcher(hostAddress);
				if (m.matches()) {
					String host = m.group(1);
					int port = Integer.parseInt(m.group(2));
					return new ServerAddress(host, port);
				}
			} catch (UnknownHostException ex) {
				throw new IllegalArgumentException(String.format("%s is not a valid [host]:[port] value.", hostAddress));
			}
			throw new IllegalArgumentException(String.format("%s is not a valid [host]:[port] value.", hostAddress));
		}
	}
}