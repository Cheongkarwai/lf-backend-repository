package com.lfhardware.file.service;

import com.lfhardware.order.domain.OrderDetails;
import com.lfhardware.order.dto.OrderDetailsDTO;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringWebFluxTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PDFService implements FileService<Object,Object>{

    private final SpringWebFluxTemplateEngine templateEngine;

    public PDFService(SpringWebFluxTemplateEngine templateEngine){
        this.templateEngine = templateEngine;
    }

    @Override
    public Mono<byte[]> exportOrders(List<OrderDetailsDTO> orderDetails) {
        return null;
    }

    @Override
    public Mono<byte[]> createFile(Map<String, Object> content){
        return Mono.fromCallable(()->{
            Context context = new Context();
            context.setVariables(content);
            String html = templateEngine.process("invoice/invoice",context);
            ITextRenderer iTextRenderer = new ITextRenderer();

            iTextRenderer.setDocumentFromString(html);
            iTextRenderer.layout();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            iTextRenderer.createPDF(os);
            os.close();

//            String executable = WrapperConfig.findExecutable();
//            Pdf pdf = new Pdf(new WrapperConfig(executable));
//            pdf.addPageFromString(html);

            return os.toByteArray();
//            return html.getBytes();
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Object upload(Object input) {
        return null;
    }

    @Override
    public Object uploadServiceProviderDocuments(Object input) {
        return null;
    }

    @Override
    public Object uploadCompleteAppointmentEvidences(UUID id, Long serviceId, String serviceProviderId, String customerId, LocalDateTime createdAt, Object input) {
        return null;
    }

}
