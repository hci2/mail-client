package at.dangl.mailclient.controller;

import at.dangl.mailclient.service.MailService;
import at.dangl.mailclient.util.Util;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static at.dangl.mailclient.util.MailProperty.DEFAULT_SUBJECT;
import static at.dangl.mailclient.util.MailProperty.DEFAULT_TEXT;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/mail" , produces = MediaType.TEXT_PLAIN_VALUE)
@Slf4j
public class MailConfigurationController {

    private final MailService mailService;

    @ApiOperation("You can see the current mail configuration.")
    @GetMapping("/configuration")
    public ResponseEntity<String> getMailConfiguration() {
        String response;

        response = mailService.getMailConfiguration();
        log.info(response);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @ApiOperation("You can see the possible mail folders of the sender email account.")
    @GetMapping("/sender/folders")
    public ResponseEntity<String> getMailSenderPossibleFolders() {
        String response;

        response = mailService.getMailSenderPossibleFolders();
        log.info(response);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @ApiOperation("You can see the mail folder of the sender email account, where the copies of the sent emails are saved.")
    @GetMapping("/sender/folder")
    public ResponseEntity<String> getMailSenderFolder() {
        String response;

        response = mailService.getMailSenderFolder();
        log.info(response);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @ApiOperation("You can set the mail folder of the sender email account, where the copies of the sent emails are saved.")
    @PostMapping("/sender/folder/{folderName}")
    public ResponseEntity<String> setMailSenderFolder(
            @ApiParam(
                    value = "The name of folder, where the sent emails are saved."
            )
            @PathVariable String folderName) {
        String response;

        response = mailService.setMailSenderFolder(folderName);
        log.info(response);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @ApiOperation("You can change the subject and text of your mail.")
    @PostMapping(value = "/configuration/subjecttext")
    public ResponseEntity<String> changeSubjectTextConfiguration(
            @ApiParam(
                    value = "The subject of the mails."
            )
            @RequestParam(value = "mailSubject") String mailSubject,
            @ApiParam(
                    value = "The text of the mails. Format in html, for example: &lt;html&gt;&lt;body&gt; Hello %s content, kind regards &lt;/body&gt;&lt;/html&gt;, needs one time %s in the content as placeholder for the name in the greeting."
            )
            @RequestParam(value = "mailText") String mailText) {
        String response;

        response = mailService.changeSubjectTextConfiguration(mailSubject, mailText);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @ApiOperation("You can add attachments to your mail.")
    @PutMapping(value = "/configuration/attachment", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> addAttachmentToConfiguration(
            @ApiParam(
                    value = "A attachment for the mails."
            )
            @RequestBody MultipartFile mailAttachment) throws IOException {
        HashMap<String, Boolean> resultAddAttachments = new HashMap<>();
        List<MultipartFile> mailAttachments = Collections.singletonList(mailAttachment);

        if (mailAttachments.isEmpty()) {
            log.error("No attachment added.");
            resultAddAttachments.put("No attachments were added: ", true);
            return new ResponseEntity(Util.getStringFromHashMap(resultAddAttachments), HttpStatus.NO_CONTENT);
        }

        log.debug("Name: {}, File size: {}, and bytes: {}", mailAttachments.get(0).getOriginalFilename(), mailAttachments.get(0).getSize(), mailAttachments.get(0).getBytes());

        resultAddAttachments = mailService.addAttachments(mailAttachments);
        return new ResponseEntity(Util.getStringFromHashMap(resultAddAttachments), HttpStatus.OK);
    }

    @ApiOperation("You can change the interval between the sending of the next email of the async execution.")
    @PostMapping(value = "/configuration/async/interval")
    public ResponseEntity<String> setAsyncExecutionConfiguration(
            @ApiParam(
                    value = "The min sleeping value between the next async execution. DEFAULT: 500 mS",
                    example = "500" //swagger bug
            )
            @RequestParam(value = "excelNameColumnIndex") long asyncMinSleepTime,
            @ApiParam(
                    value = "The max sleeping value between the next async execution. DEFAULT: 1750 mS",
                    example = "1750" //swagger bug
            )
            @RequestParam(value = "excelTextColumnIndex") long asyncMaxSleepTime) {
        String response;

        response = mailService.setAsyncExecutionConfiguration(asyncMinSleepTime, asyncMaxSleepTime);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @ApiOperation("It reset the configuration to the default one. DEFAULT subject: " + DEFAULT_SUBJECT + ", DEFAULT text: " + DEFAULT_TEXT)
    @DeleteMapping(value = "/configuration/reset", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> resetMailConfiguration() {
        String response;

        response = mailService.resetMailConfiguration();
        log.info(response);
        return new ResponseEntity(response, HttpStatus.OK);
    }

}
