package tests.java.saucedemo;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PDFExtractor {

    public static String extractTextFromPdf(String fileName) throws IOException {
        String filePath = "D:\\Java\\playwright-java\\target\\files\\";
        File file = new File(filePath + fileName);
        try (PDDocument document = Loader.loadPDF(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            System.out.println(text);
            return text;
        }
    }
}
