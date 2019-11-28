package br.com.justoeu.application.gateway.http;

import br.com.justoeu.application.gateway.message.amqp.InitialProcessEvent;
import br.com.justoeu.domain.InvoiceRequest;
import br.com.justoeu.domain.InvoiceRespose;
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
    public ResponseEntity<InvoiceRespose> createInvoice(@RequestBody InvoiceRequest req) {

        String trackingID = initialProcess.startProcess(req);
        InvoiceRespose response = InvoiceRespose.builder().id(req.getId()).referenceInvoiceId(req.getReferenceInvoiceId()).TrackingID(trackingID).build();

        return new ResponseEntity(response, HttpStatus.ACCEPTED);
    }


}