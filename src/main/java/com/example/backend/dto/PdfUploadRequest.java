package com.example.backend.dto;

public class PdfUploadRequest {
    private String title;
    private String description;
    private String category;
    private String subdomain;
    private String subSubdomain;
    private String file;
    private String fileName;

    public PdfUploadRequest() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubdomain() {
        return subdomain;
    }

    public void setSubdomain(String subdomain) {
        this.subdomain = subdomain;
    }

    public String getSubSubdomain() {
        return subSubdomain;
    }

    public void setSubSubdomain(String subSubdomain) {
        this.subSubdomain = subSubdomain;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
