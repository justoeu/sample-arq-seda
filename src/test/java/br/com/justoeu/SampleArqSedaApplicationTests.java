package br.com.justoeu;

import br.com.justoeu.domain.InvoiceRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class SampleArqSedaApplicationTests {

    @Autowired
    ObjectMapper mapper;

    @Test
    void contextLoads() throws JsonProcessingException {

        InvoiceRequest request = new InvoiceRequest();
        request.setId(123456l);
        request.setCreatedDate(LocalDateTime.now());
        request.setPackId(1111l);
        request.setReferenceInvoiceId(11222l);
        request.setSellerId("11123");
        request.setShipmentId(11123l);
        request.setStatus("INIT_INVOICE");

        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request));
    }

}
