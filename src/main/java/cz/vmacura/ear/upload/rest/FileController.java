package cz.vmacura.ear.upload.rest;


import cz.vmacura.ear.upload.entities.Account;
import cz.vmacura.ear.upload.entities.File;
import cz.vmacura.ear.upload.repositories.UrlRepository;
import cz.vmacura.ear.upload.service.AccountService;
import cz.vmacura.ear.upload.service.FileService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/file")
public class FileController {
    private static final Logger LOG = LoggerFactory.getLogger(FileController.class);

    private final FileService fileService;
    private final AccountService accountService;

    @Value("${fileLocation}")
    private String location;

    @Autowired
    public FileController(FileService fileService, AccountService accountService) {
        this.fileService = fileService;
        this.accountService = accountService;
    }

    @PreAuthorize("hasAnyAuthority('1','0')")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public File getFile(@PathVariable("id") Integer id) {
        switch (fileService.isAuthorizedRequest(id)) {
            case "UNAVAILABLE RESOURCES":
                System.out.println("You are neither admin nor the file owner, sorry.");
                return null;
            case "INVALID":
                System.out.println("You are neither admin nor the file owner, sorry.");
                return null;
            default:
                final File file = fileService.findFile(id);
                if (file == null) {
                    LOG.debug("could not find file:  {}.", file);
                }
                return file;
        }
    }


    @PreAuthorize("hasAnyAuthority('1')")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<File> getFiles() {
        return this.fileService.findAll();
    }

    @PreAuthorize("hasAnyAuthority('1')")
    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public @ResponseBody List<File> getAdminFiles()
    {
        return this.fileService.getAdminFiles();
    }


    @PreAuthorize("hasAnyAuthority('1','0')")
    @ResponseStatus(code = HttpStatus.OK)
    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = "multipart/form-data")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName=null;
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUserName = authentication.getName();
        }
        if(!(currentUserName==null)) {
            Account account = this.accountService.findByUsername(currentUserName);
            File uploadedFile = fileService.uploadFile(file, account);
            return uploadedFile.getUrl().getString();
        }
        return "Failed to upload file.";
    }


    @PreAuthorize("hasAnyAuthority('1','0')")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String removeFile(@PathVariable("id") Integer id) {
        System.out.println(id);
        System.out.println(fileService.isAuthorizedRequest(id));
        switch (fileService.isAuthorizedRequest(id)) {
            case "UNAVAILABLE RESOURCES":
                return "You are requesting unavailable resources.";
            case "INVALID":
                return "You are neither admin nor the file owner, sorry.";
            default:
                return this.fileService.removeFile(id);
        }
    }


    @PreAuthorize("hasAnyAuthority('1','0')")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public String editFile(@PathVariable("id") Integer id, @RequestBody File file) {
        switch (fileService.isAuthorizedRequest(id)) {
            case "UNAVAILABLE RESOURCES":
                return "You are requesting unavailable resources.";
            case "INVALID":
                return "You are neither admin nor the file owner, sorry.";
            default:
                return this.fileService.update(id, file);
        }
    }


    @PreAuthorize("hasAnyAuthority('1','0')")
    @RequestMapping(value = "/share/{id}", method = RequestMethod.GET)
    public String shareFile(@PathVariable("id") Integer id) {
        String template="www.nas_system.cz/file/download/";
        switch (fileService.isAuthorizedRequest(id)) {
            case "UNAVAILABLE RESOURCES":
                return "You are requesting unavailable resources.";
            case "INVALID":
                return "You are neither admin nor the file owner, sorry.";
            default:
                return template + this.fileService.shareFile(id);
        }
    }


    @RequestMapping(value = "/download/{code}", method = RequestMethod.GET)
    @ResponseBody
    public FileSystemResource downloadFile(@PathVariable("code") String code, HttpServletResponse response) {
        File file = this.fileService.findFileByCode(code);
        if(file != null){
            try {
                InputStream is = new FileSystemResource(location + file.getName()).getInputStream();
                IOUtils.copy(is, response.getOutputStream());
                response.flushBuffer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            System.out.println("File with code "+code+" not found");
        }
        return null;
    }
}
