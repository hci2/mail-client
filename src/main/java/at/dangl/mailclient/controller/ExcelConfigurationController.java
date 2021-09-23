package at.dangl.mailclient.controller;

import at.dangl.mailclient.service.ExcelParserService;
import at.dangl.mailclient.util.ExcelConst;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/excel", produces = MediaType.TEXT_PLAIN_VALUE)
@Slf4j
public class ExcelConfigurationController {

    private final ExcelParserService excelParserService;

    @ApiOperation("You can see the current excel configuration.")
    @GetMapping(value = "/configuration")
    public ResponseEntity<String> getMailConfiguration() {
        String response;

        response = excelParserService.getExcelConfiguration();
        log.info(response);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @ApiOperation("You can change the name and email column of your excel parsing.")
    @PostMapping(value = "/configuration/column/nameemail")
    public ResponseEntity<String> changeExcelColumnIndexConfiguration(
            @ApiParam(
                    value = "The name of the excel parsing. DEFAULT: 0/A",
                    example = "0" //swagger bug
            )
            @RequestParam(value = "excelNameColumnIndex") int excelNameColumnIndex,
            @ApiParam(
                    value = "The text of the excel parsing. DEFAULT: 2/C",
                    example = "2" //swagger bug
            )
            @RequestParam(value = "excelTextColumnIndex") int excelTextColumnIndex) {
        String response;

        response = excelParserService.changeExcelColumnIndex(excelNameColumnIndex, excelTextColumnIndex);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @ApiOperation("It reset the configuration to the default one. DEFAULT name index: " + ExcelConst.DEFAULT_EXCEL_NAME_COLUMN_INDEX + ", DEFAULT email: " + ExcelConst.DEFAULT_EXCEL_EMAIL_COLUMN_INDEX)
    @DeleteMapping(value = "/configuration/reset", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> resetExcelConfiguration() {
        String response;

        response = excelParserService.resetExcelConfiguration();
        log.info(response);
        return new ResponseEntity(response, HttpStatus.OK);
    }

}
