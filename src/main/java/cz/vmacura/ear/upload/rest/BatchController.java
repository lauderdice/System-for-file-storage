package cz.vmacura.ear.upload.rest;

import cz.vmacura.ear.upload.entities.Batch;
import cz.vmacura.ear.upload.entities.File;
import cz.vmacura.ear.upload.service.AccountService;
import cz.vmacura.ear.upload.service.BatchService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;


@RestController
@RequestMapping("/batch")
public class BatchController {
    private static final Logger LOG = LoggerFactory.getLogger(cz.vmacura.ear.upload.rest.BatchController.class);

    private final BatchService batchService;

    @Autowired
    public BatchController(BatchService batchService) {
        this.batchService = batchService;
    }


    @PreAuthorize("hasAnyAuthority('1','0')")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Batch showBatchContent(@PathVariable("id") Integer id) {
        switch (batchService.isAuthorizedRequestForBatch(id)) {
            case "UNAVAILABLE RESOURCES":
                System.out.println("You are requesting unavailable resources.");
                return null;
            case "INVALID":
                System.out.println("You are neither admin nor the file owner, sorry.");
                return null;
            default:
                Batch batch = batchService.findBatch(id);
                if (batch == null) {
                    LOG.debug("could not find batch:  {}.", batch);
                }
                return batch;
        }
    }

    @PreAuthorize("hasAnyAuthority('1','0')")
    @RequestMapping(value = "/{batch_id}/{file_id}", method = RequestMethod.PUT)
    public boolean addFileToBatch(@PathVariable("batch_id") Integer batchId,@PathVariable("file_id") Integer fileId) {
        switch (batchService.isAuthorizedRequestForBatchAndFile(batchId,fileId)) {
                case "UNAVAILABLE RESOURCES":
                    System.out.println("You are requesting unavailable resources.");
                    return false;
                case "INVALID":
                    System.out.println("You are neither admin nor the file owner, sorry.");
                    return false;
                default:
                    return batchService.addFileToBatch(batchId,fileId);
        }
    }

    @PreAuthorize("hasAnyAuthority('1','0')")
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Batch createBatch(){
        return batchService.createBatch();
    }


    @PreAuthorize("hasAnyAuthority('1','0')")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public boolean deleteBatch(@PathVariable("id") Integer id) {
        switch (batchService.isAuthorizedRequestForBatch(id)) {
            case "UNAVAILABLE RESOURCES":
                System.out.println("You are requesting unavailable resources.");
                return false;
            case "INVALID":
                System.out.println("You are neither admin nor the file owner, sorry.");
                return false;
            default:
                return batchService.deleteBatch(id);
        }

    }

    @PreAuthorize("hasAnyAuthority('1','0')")
    @RequestMapping(value = "/{batch_id}/{file_id}", method = RequestMethod.DELETE)
    public boolean removeFileFromBatch(@PathVariable("batch_id") Integer batchId,@PathVariable("file_id") Integer fileId) {
        switch (batchService.isAuthorizedRequestForBatchAndFile(batchId, fileId)) {
            case "UNAVAILABLE RESOURCES":
                System.out.println("You are requesting unavailable resources.");
                return false;
            case "INVALID":
                System.out.println("You are neither admin nor the file owner, sorry.");
                return false;
            default:
                return batchService.removeFileFromBatch(batchId, fileId);
        }
    }

        @PreAuthorize("hasAnyAuthority('1','0')")
        @RequestMapping(value = "/share/{id}", method = RequestMethod.GET)
        public String shareBatch(@PathVariable("id") Integer id) {
            String template="www.nas_system.cz/download/";
            switch (batchService.isAuthorizedRequestForBatch(id)) {
                case "UNAVAILABLE RESOURCES":
                    return "You are requesting unavailable resources.";
                case "INVALID":
                    return "You are neither admin nor the file owner, sorry.";
                default:
                    return template+this.batchService.shareBatch(id);
            }
    }

}