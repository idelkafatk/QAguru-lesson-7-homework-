import com.codeborne.pdftest.PDF;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.xlstest.XLS;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WorkWithFilesTest {

    private final String urlUpload = "https://filelu.com/";
    private final String urlDownload = "https://github.com/selenide/selenide/blob/master/README.md";
    private final String urlDownloadPDF = "https://aldebaran.ru/author/bakulin_aleksandr/kniga_gravitaciya_i_yefir/";
    private final static String urlDownloadXLS = "https://ckmt.ru/price-download.html";

    private final SelenideElement uploadFileSelector = $("input[type='file']");
    private final SelenideElement downloadFileSelector = $("#raw-url");
    private final SelenideElement pdfFile = $(byText("pdf (A6)"));
    private final SelenideElement xlsFile = $("h3 a[href='https://ckmt.ru/TehresursPrice.xls']");

    private final static String selenideReadme = "Selenide = UI Testing Framework powered by Selenium WebDriver";
    private final static String xlsTitleCheck = "УОНИ 13/55";
    private final String FILENAME = "test.pdf";

    @Test
    @DisplayName("Upload file")
    void uploadFile() {
        open(urlUpload);
        uploadFileSelector.uploadFromClasspath(FILENAME);
        $(".xrow .xfname").shouldHave(Condition.text(FILENAME));
    }

    @Test
    @DisplayName("Download file")
    void downloadFile() throws IOException {
        open(urlDownload);
        File download = downloadFileSelector.download();
        String file  = IOUtils.toString(new FileReader(download));
        assertTrue(file.contains(selenideReadme));
    }

    @Test
    @DisplayName("Download pdf-file")
    void downloadPdfFileTest() throws IOException {
        open(urlDownloadPDF);
        File pdf = pdfFile.download();
        PDF parsedPdf = new PDF(pdf);
        Assertions.assertEquals(667, parsedPdf.numberOfPages);
    }

    @Test
    @DisplayName("Download Xls file")
    void downloadXlsFileTest() throws FileNotFoundException {
        open(urlDownloadXLS);
        File xls = xlsFile.download();
        XLS xlsFile = new XLS(xls);

        boolean check = xlsFile.excel
                .getSheetAt(0)
                .getRow(33)
                .getCell(0)
                .getStringCellValue()
                .contains(xlsTitleCheck);

        //xlsFile.excel._sheets.get(0)._rows.get(33).getCell(0)
        assertTrue(check);
    }
}
