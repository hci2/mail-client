package at.dangl.mailclient.controller;

import at.dangl.mailclient.service.MailService;
import at.dangl.mailclient.util.Const;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/mail", produces = MediaType.TEXT_PLAIN_VALUE)
@Slf4j
public class MailSendController {

    private final MailService mailService;

    @ApiOperation("You can send asynchronously emails to the provided excel list recipients based on the configuration of subject, text and attachments.")
    @PostMapping(value = "/send/async")
    public ResponseEntity<String> sendAsyncMails(
            @ApiParam(
                    value = "The excel file containing the names and email of the recipients. Format of the file is in xlsx. Takes name of column 1 or A and email of column 3 or C, skips first row (header row)"
            )
            @RequestParam("mailRecipientsExcelFile") MultipartFile mailRecipientsXlsXFile) throws IOException {
        String resultSentEmails;
        Instant start = Instant.now();

        if (mailRecipientsXlsXFile == null || !mailRecipientsXlsXFile.getContentType().equalsIgnoreCase("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
            log.error("File is empty or it is not an xlsx file: {}", mailRecipientsXlsXFile);
            resultSentEmails = "The file is empty or it is not an xlsx file";
            return new ResponseEntity(resultSentEmails, HttpStatus.NO_CONTENT);
        }

        log.debug("Name: {}, File size: {}, and bytes: {}", mailRecipientsXlsXFile.getOriginalFilename(), mailRecipientsXlsXFile.getSize(), mailRecipientsXlsXFile.getBytes());

        resultSentEmails = mailService.sendMailsAsyncTo(mailRecipientsXlsXFile);
        Instant end = Instant.now();
        long durationInSeconds = Duration.between(start, end).getSeconds();
        log.info("Duration of sending emails asynchronously in seconds: {}",durationInSeconds);
        resultSentEmails += Const.DISPLAY_DURATION_SECONDS + durationInSeconds + Const.DISPLAY_NEW_LINE;
        return new ResponseEntity(resultSentEmails, HttpStatus.OK);
    }

    @ApiOperation("You can send synchronously emails to the provided excel list recipients based on the configuration of subject, text and attachments.")
    @PostMapping(value = "/send/sync")
    public ResponseEntity<String> sendSyncMails(
            @ApiParam(
                    value = "The excel file containing the names and email of the recipients. Format of the file is in xlsx. Takes name of column 1 or A and email of column 3 or C, skips first row (header row)"
            )
            @RequestParam("mailRecipientsExcelFile") MultipartFile mailRecipientsXlsXFile) throws IOException {
        String resultSentEmails;
        Instant start = Instant.now();

        if (mailRecipientsXlsXFile == null || !mailRecipientsXlsXFile.getContentType().equalsIgnoreCase("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
            log.error("File is empty or it is not an xlsx file: {}", mailRecipientsXlsXFile);
            resultSentEmails = "The file is empty or it is not an xlsx file";
            return new ResponseEntity(resultSentEmails, HttpStatus.NO_CONTENT);
        }

        log.debug("Name: {}, File size: {}, and bytes: {}", mailRecipientsXlsXFile.getOriginalFilename(), mailRecipientsXlsXFile.getSize(), mailRecipientsXlsXFile.getBytes());

        resultSentEmails = mailService.sendMailsSyncTo(mailRecipientsXlsXFile);
        Instant end = Instant.now();
        long durationInSeconds = Duration.between(start, end).getSeconds();
        log.info("Duration of sending emails synchronously in seconds: {}",durationInSeconds);
        resultSentEmails += Const.DISPLAY_DURATION_SECONDS + durationInSeconds + Const.DISPLAY_NEW_LINE;
        return new ResponseEntity(resultSentEmails, HttpStatus.OK);
    }


}
