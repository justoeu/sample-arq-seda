package br.com.justoeu.application.gateway.http;

import br.com.justoeu.domain.gateway.InitialProcessEvent;
import br.com.justoeu.domain.model.InvoiceRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static br.com.justoeu.application.gateway.http.URLMapping.API_CREATE_INVOICE;
import static br.com.justoeu.application.gateway.http.URLMapping.ROOT_API;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(ROOT_API)
@Slf4j
public class InvoiceController {

    @Autowired
    InitialProcessEvent initialProcess;

    @PostMapping(value = API_CREATE_INVOICE, produces = APPLICATION_JSON_VALUE+";charset=UTF-8", consumes = APPLICATION_JSON_VALUE+";charset=UTF-8")
    public ResponseEntity<String> createInvoice(@RequestBody InvoiceRequest req) {

        initialProcess.startProcess(req);

        return new ResponseEntity("Create Process", HttpStatus.ACCEPTED);
    }


}