package cz.vmacura.ear.upload.service;

import cz.vmacura.ear.upload.entities.Account;
import cz.vmacura.ear.upload.entities.Batch;
import cz.vmacura.ear.upload.entities.File;
import cz.vmacura.ear.upload.entities.Url;
import cz.vmacura.ear.upload.repositories.AccountRepository;
import cz.vmacura.ear.upload.repositories.BatchRepository;
import cz.vmacura.ear.upload.repositories.FileRepository;
import cz.vmacura.ear.upload.repositories.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
@Transactional
public class BatchService {

    private BatchRepository batchRepository;
    private FileRepository fileRepository;
    private FileService fileService;
    private AccountRepository accountRepository;
    private UrlRepository urlRepository;


    @Autowired
    public BatchService(UrlRepository urlRepository, BatchRepository batchRepository,FileRepository fileRepository,FileService fileService, AccountRepository accountRepository) {
        this.batchRepository = batchRepository;
        this.fileRepository = fileRepository;
        this.fileService = fileService;
        this.accountRepository = accountRepository;
        this.urlRepository = urlRepository;
    }

    public void persist(Batch batch) {
        this.batchRepository.persist(batch);
    }


    @Transactional
    public Batch createBatch(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account loggedInUser = accountRepository.findByUsername(authentication.getName());

        Batch batch = new Batch();
        batch.setAccount(loggedInUser);
        Url url = new Url();
        url.setBatch(batch);
        url.setString(fileService.generateRandomString(10));
        persist(batch);
//        urlRepository.persist(url);
//        accountRepository.update(loggedInUser);
        return batch;
    }

    @Transactional
    public boolean deleteBatch(int batchId){
        Batch batch = batchRepository.find(batchId);
        if(batch != null){
            List<File> files = batch.getFiles();
            for(File file : files){
                file.getBatches().remove(batch);
                fileRepository.update(file);
            }
            files.clear();
            batchRepository.update(batch);
            batchRepository.remove(batch);
            return true;
        }
        return false;
    }


    public String shareBatch(int id) {
        Batch batch = batchRepository.find(id);
        if(batch != null){
            Objects.requireNonNull(batch);
            List<Url> listOfUrls= urlRepository.getShareableLink(batch);
            if(listOfUrls.size() > 0){
                return listOfUrls.get(0).getString();
            }else{
                return "Batch does not have any shareable url";
            }
        }else{
            return "Batch does not exist";
        }

    }
    public String isAuthorizedRequestForBatch(int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUser = authentication.getName();
        String batchOwner = "";
        try {
            batchOwner = findBatch(id).getAccount().getUsername();
        } catch (NullPointerException e) {
            return "UNAVAILABLE RESOURCES";
        }
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("1")) || loggedInUser.equals(batchOwner)) {
            return "VALID";
        } else {
            return "INVALID";
        }
    }

    public String isAuthorizedRequestForBatchAndFile(int batchId,int fileId) {
        if(isAuthorizedRequestForBatch(batchId) == "VALID"){
            return fileService.isAuthorizedRequest(fileId);
        }else{
            return isAuthorizedRequestForBatch(batchId);
        }
    }
    public Batch findByUrl(String code){
        return batchRepository.findByUrl(code);
    }


    public void update(Batch batch) {
        batchRepository.update(batch);
    }

    public Batch findBatch(int id) {
        return batchRepository.find(id);
    }


    public boolean addFileToBatch(int batchId, int fileId) {
        Batch batch = findBatch(batchId);
        System.out.println(fileId);
        File file = fileRepository.find(fileId);
        System.out.println(file);
        if (batch == null) {
            System.out.printf("could not find batch:  {}.\n", batch);
            return false;
        }
        if (file == null) {
            System.out.printf("could not find file:  {}.\n", file);
            return false;
        }
        try {
            batch.getFiles().add(file);
            fileService.update(file);
            update(batch);
            return true;
        } catch (Exception e) {
            System.out.println("Exception in adding file to batch:");
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeFileFromBatch(int batchId,int fileId) {
        Batch batch = findBatch(batchId);
        File file = fileRepository.find(fileId);
        if (batch == null) {
            System.out.printf("could not find batch:  {}.\n", batch);
            return false;
        }
        if (file == null) {
            System.out.printf("could not find file:  {}.\n", file);
            return false;
        }
        try {
            batch.getFiles().remove(file);
            update(batch);
            fileService.update(file);
            return true;
        } catch (Exception e) {
            System.out.println("Exception in removing file from batch:");
            e.printStackTrace();
            return false;
        }
    }
}
