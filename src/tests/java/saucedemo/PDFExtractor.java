package tests.java.saucedemo;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PDFExtractor {

    public static String extractTextFromPdf(Path filePath) throws IOException {
        File file = filePath.toFile();
        try (PDDocument document = Loader.loadPDF(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            return text;
        }
    }

    public static BigDecimal getValueFromString(String[] text, String stringStartWith) {
        for (int i = 0; i < text.length; i++) {
            if (text[i].startsWith(stringStartWith)) {
                return new BigDecimal(text[i].replaceAll("[^\\d.]", ""));
            }
        }
        throw new IllegalArgumentException("String starts with " + stringStartWith + " wasn't found");

    }
}
