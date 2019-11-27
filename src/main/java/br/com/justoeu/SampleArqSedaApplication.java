package br.com.justoeu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static br.com.justoeu.config.jdk.warning.DisableAccessWarnings.disableAccessWarnings;

@SpringBootApplication
public class SampleArqSedaApplication {

    public static void main(String[] args) {
        disableAccessWarnings();
        SpringApplication.run(SampleArqSedaApplication.class, args);
    }

}