package com.example.backend.controller;

import com.example.backend.dto.MessageResponse;
import com.example.backend.dto.PdfUploadRequest;
import com.example.backend.entity.Pdf;
import com.example.backend.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pdfs")
public class PdfController {

    @Autowired
    private PdfService pdfService;

    @GetMapping
    public ResponseEntity<?> getAllPdfs() {
        try {
            return ResponseEntity.ok(pdfService.getAllPdfs());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Failed to read PDFs."));
        }
    }

    @PostMapping
    public ResponseEntity<?> uploadPdf(@RequestBody PdfUploadRequest request) {
        try {
            Pdf newPdf = pdfService.uploadPdf(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(newPdf);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Failed to save PDF."));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePdf(@PathVariable String id) {
        try {
            pdfService.deletePdf(id);
            return ResponseEntity.ok(new MessageResponse("PDF deleted."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Failed to delete PDF."));
        }
    }
}
