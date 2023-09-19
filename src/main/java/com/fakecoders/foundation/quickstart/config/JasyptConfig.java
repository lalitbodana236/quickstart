package com.fakecoders.foundation.quickstart.config;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentPBEConfig;
import org.jasypt.salt.RandomSaltGenerator;
import org.jasypt.salt.SaltGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JasyptConfig {

	@Bean
	public StandardPBEStringEncryptor stringEncryptor() {
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		EnvironmentPBEConfig config = new EnvironmentPBEConfig();

		// Set your encryption algorithm (e.g., PBEWithMD5AndDES)
		config.setAlgorithm("PBEWithMD5AndDES");

		// Set your encryption password
		config.setPassword("mySecretKey");

		// Optionally, configure a salt generator
		SaltGenerator saltGenerator = new RandomSaltGenerator();
		config.setSaltGenerator(saltGenerator);

		encryptor.setConfig(config);
		return encryptor;
	}
}

