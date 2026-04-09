package com.example.backend.service;

import com.example.backend.dto.PdfUploadRequest;
import com.example.backend.entity.Pdf;
import com.example.backend.repository.PdfRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PdfService {

    @Autowired
    private PdfRepository pdfRepository;
    
    @Autowired
    private ActivityService activityService;

    public List<Pdf> getAllPdfs() {
        return pdfRepository.findAll();
    }

    public Pdf uploadPdf(PdfUploadRequest request) {
        if (request.getTitle() == null || request.getCategory() == null || request.getFile() == null || request.getFileName() == null) {
            throw new IllegalArgumentException("Missing required PDF fields.");
        }
        
        Pdf pdf = new Pdf();
        pdf.setId(String.valueOf(System.currentTimeMillis()));
        pdf.setTitle(request.getTitle());
        pdf.setDescription(request.getDescription() != null ? request.getDescription() : "");
        pdf.setCategory(request.getCategory());
        pdf.setSubdomain(request.getSubdomain() != null ? request.getSubdomain() : "");
        pdf.setSubSubdomain(request.getSubSubdomain() != null ? request.getSubSubdomain() : "");
        pdf.setFile(request.getFile());
        pdf.setFileName(request.getFileName());
        
        SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy");
        pdf.setUploadDate(sdf.format(new Date()));
        
        pdfRepository.save(pdf);
        
        activityService.logActivity(
            "Added",
            "PDF: " + pdf.getTitle(),
            pdf.getCategory(),
            "Uploaded " + pdf.getFileName() + " to " + pdf.getCategory()
        );
        
        return pdf;
    }

    public void deletePdf(String id) {
        pdfRepository.findById(id).ifPresent(pdf -> {
            pdfRepository.delete(pdf);
            
            activityService.logActivity(
                "Deleted",
                "PDF: " + pdf.getTitle(),
                pdf.getCategory(),
                "Removed " + pdf.getFileName() + " from " + pdf.getCategory()
            );
        });
    }
}
