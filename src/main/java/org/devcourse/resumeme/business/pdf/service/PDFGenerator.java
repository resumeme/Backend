package org.devcourse.resumeme.business.pdf.service;

import lombok.RequiredArgsConstructor;
import org.devcourse.resumeme.business.resume.service.ResumeService;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Pdf;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.print.PageSize;
import org.openqa.selenium.print.PrintOptions;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@Transactional
@RequiredArgsConstructor
public class PDFGenerator {

    private final ResumeService resumeService;

    private final String RESUME_URL_FOR_PDF = "https://resumeme.vercel.app/resume/";

    public Path createPdf(Long resumeId, String accessToken) throws IOException, InterruptedException {
        String resumeTitle = resumeService.getOne(resumeId).getTitle();
        Pdf pdf = getPDFofPage(resumeId, accessToken);

        return Files.write(Paths.get("./src/main/resources/pdf/"+ resumeTitle + ".pdf"), OutputType.BYTES.convertFromBase64Png(pdf.getContent()));
    }

    private Pdf getPDFofPage(Long resumeId, String accessToken) throws InterruptedException {
        ChromeDriver chromeDriver = new ChromeDriver(getChromeDriverService(), getChromeOptions());

        chromeDriver.get(RESUME_URL_FOR_PDF + resumeId);
        chromeDriver.manage().addCookie(new Cookie("authorization", accessToken));
        chromeDriver.get(RESUME_URL_FOR_PDF + resumeId);
        Thread.sleep(5000);

        Pdf createdPdf = chromeDriver.print(getPrintOptions());
        chromeDriver.close();

        return createdPdf;
    }

    private ChromeDriverService getChromeDriverService() {
        return new ChromeDriverService.Builder()
                .usingDriverExecutable(new File("/usr/bin/chromedriver"))
                .usingAnyFreePort()
                .build();
    }

    private ChromeOptions getChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox=new");
        options.addArguments("disable-gpu");
        options.addArguments("window-size=1920x1080");
        options.addArguments("lang=ko_KR");
        options.addArguments("Content-Type=application/json; charset=utf-8");

        return options;
    }

    private PrintOptions getPrintOptions() {
        PrintOptions printOptions = new PrintOptions();
        printOptions.setPageSize(new PageSize());

        return printOptions;
    }

}
