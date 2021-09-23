package at.dangl.mailclient.service;

import at.dangl.mailclient.util.ExcelConst;
import com.google.common.collect.ArrayListMultimap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static at.dangl.mailclient.util.Const.*;
import static at.dangl.mailclient.util.Const.DISPLAY_NEW_LINE;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExcelParserServiceImpl implements ExcelParserService {

    //https://www.callicoder.com/java-read-excel-file-apache-poi/

    @Override
    public ArrayListMultimap<String, String> getAndExtractMailRecipientsFrom(MultipartFile mailRecipientsXlsXFile) {
        ArrayListMultimap<String, String> recipientsNameMail = ArrayListMultimap.create();
        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(mailRecipientsXlsXFile.getInputStream());

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    //skip header row
                    continue;
                } else {
                    Cell cellName = row.getCell(ExcelConst.EXCEL_NAME_COLUMN_INDEX);
                    Cell cellEmail = row.getCell(ExcelConst.EXCEL_EMAIL_COLUMN_INDEX);
                    if (cellName != null && cellEmail != null) {
                        log.debug("Cell name: {}", cellName);
                        log.debug("Cell email: {}", cellEmail);
                        recipientsNameMail.put(cellName.getStringCellValue(), cellEmail.getStringCellValue());
                    }
                }
            }

            workbook.close();
        } catch (IOException e) {
            log.warn("Could not load excel file: {}, exception: {}", mailRecipientsXlsXFile.getOriginalFilename(), e.getLocalizedMessage());
        }
        log.debug("Extracted recipients: {} from loaded excel file: {}, ", recipientsNameMail, mailRecipientsXlsXFile.getOriginalFilename());
        return recipientsNameMail;
    }

    @Override
    public String changeExcelColumnIndex(int excelNameColumnIndex, int excelEmailColumnIndex) {
        ExcelConst.EXCEL_NAME_COLUMN_INDEX = excelNameColumnIndex;
        ExcelConst.EXCEL_EMAIL_COLUMN_INDEX = excelEmailColumnIndex;
        return "Successfully excel column index configuration.";
    }

    @Override
    public String resetExcelConfiguration() {
        ExcelConst.EXCEL_NAME_COLUMN_INDEX = ExcelConst.DEFAULT_EXCEL_NAME_COLUMN_INDEX;
        ExcelConst.EXCEL_EMAIL_COLUMN_INDEX = ExcelConst.DEFAULT_EXCEL_EMAIL_COLUMN_INDEX;
        return "Successfully reset excel configuration.";
    }

    @Override
    public String getExcelConfiguration() {
        String mailConfiguration;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(DISPLAY_SUMMARY_WITH_EMPTY_ROW);
        stringBuilder.append(DISPLAY_EXCEL_INDEX_NAME +ExcelConst.EXCEL_NAME_COLUMN_INDEX + DISPLAY_NEW_LINE);
        stringBuilder.append(DISPLAY_EXCEL_INDEX_EMAIL + ExcelConst.EXCEL_EMAIL_COLUMN_INDEX+ DISPLAY_NEW_LINE);
        mailConfiguration = stringBuilder.toString();
        return mailConfiguration;
    }
}
