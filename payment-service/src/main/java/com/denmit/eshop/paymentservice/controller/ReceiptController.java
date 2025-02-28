package com.denmit.eshop.paymentservice.controller;

import com.denmit.eshop.paymentservice.service.ReceiptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/receipts")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Receipt controller")
public class ReceiptController {

    private final ReceiptService receiptService;

    @GetMapping("/download/{orderId}")
    @Operation(summary = "Download pdf file to order receipt")
    public ResponseEntity<ByteArrayResource> download(@PathVariable Long orderId) {
        ByteArrayResource resource = receiptService.downloadReceiptFile(orderId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=receipt_" + orderId + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }
}
