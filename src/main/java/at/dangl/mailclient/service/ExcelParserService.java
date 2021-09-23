package at.dangl.mailclient.service;

import com.google.common.collect.ArrayListMultimap;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

public interface ExcelParserService {

    ArrayListMultimap<String, String> getAndExtractMailRecipientsFrom(MultipartFile mailRecipientsXlsXFile);

    String changeExcelColumnIndex(int excelNameColumnIndex, int excelEmailColumnIndex);

    String resetExcelConfiguration();

    String getExcelConfiguration();
}
