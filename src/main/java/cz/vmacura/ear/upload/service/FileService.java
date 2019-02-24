package cz.vmacura.ear.upload.service;

import cz.vmacura.ear.upload.entities.Account;
import cz.vmacura.ear.upload.entities.File;
import cz.vmacura.ear.upload.entities.Url;
import cz.vmacura.ear.upload.repositories.AccountRepository;
import cz.vmacura.ear.upload.repositories.FileRepository;
import cz.vmacura.ear.upload.repositories.UrlRepository;
import org.eclipse.persistence.annotations.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Service
@Transactional
public class FileService {

    private FileRepository fileRepository;
    private UrlRepository urlRepository;
    private AccountRepository accountRepository;

    @Value("${fileLocation}")
    private String location;

    @Autowired
    public FileService(FileRepository fileRepository, UrlRepository urlRepository, AccountRepository accountRepository) {
        this.fileRepository = fileRepository;
        this.urlRepository = urlRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public void persist(File file) {
        this.fileRepository.persist(file);
    }


    public String isAuthorizedRequest(int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUser = authentication.getName();
        String fileOwner = "";
        try {
            fileOwner = findFile(id).getAccount().getUsername();
        } catch (NullPointerException e) {
            return "UNAVAILABLE RESOURCES";
        }
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("1")) || loggedInUser.equals(fileOwner)) {
            return "VALID";
        } else {
            return "INVALID";
        }
    }


    @Transactional
    public String removeFile(int id) {

        File file = fileRepository.find(id);
        if(file != null){
            Objects.requireNonNull(file);
            fileRepository.remove(file);
            return "File removed";
        }else{
            return "File does not exist";
        }

    }

    public String update(int id, File file) {
        File original = fileRepository.find(id);
        if(file != null) {
            if (file.getName() != null)
                original.setName(file.getName());
            if (file.getAccount() != null)
                original.setAccount(file.getAccount());
            if (file.getLocation() != null)
                original.setLocation(file.getLocation());
            this.update(original);
            return "Success";
        }

        return "File does not exist";
    }

    public void update(File file)
    {
        this.fileRepository.update(file);
        this.fileRepository.persist(file);
    }

    public String shareFile(int id) {
        File file = fileRepository.find(id);
        if(file != null){
            Objects.requireNonNull(file);
            List<Url> listOfUrls= urlRepository.getShareableLink(file);
            if(listOfUrls.size() > 0){
                return listOfUrls.get(0).getString();
            }else{
                return "File does not have any shareable url";
            }
        }else{
            return "File does not exist";
        }

    }



    public String generateRandomString(int limit) {

        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = limit;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }


    public File uploadFile(MultipartFile file, Account account) {

//        String location;
//        if (System.getProperty("os.name").toLowerCase().contains("win"))
//            location = "D:" + java.io.File.separator + "data" + java.io.File.separator;
//        else
//            location = "/Users/lauderdice/Desktop/ear_photos";
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, 1);
        java.util.Date expirationDate = cal.getTime();
        String urlcode=generateRandomString(10);
        String filename = file.getOriginalFilename();
        try{
            File fileToSave = new File();
            Url url = new Url();
            url.setFile(fileToSave);
            url.setString(urlcode);
            fileToSave.setAccount(account);
            account.getFiles().add(fileToSave);
            fileToSave.setExpiration(expirationDate);
            fileToSave.addUrl(url);
            fileToSave.setLocation(location);
            fileToSave.setName(filename);
            try{
                store(file,location, filename);
            }catch(Exception e){
                System.out.println("File with the same name detected... creating a copy with slightly different name..");
                filename = "Copy (version "+generateRandomString(5)+ ") - "+file.getOriginalFilename();
                store(file,location,filename);
                fileToSave.setName(filename);
            }
            accountRepository.update(account);
            persist(fileToSave);
            return fileToSave;
        }catch(Exception e){
            System.out.println("An error occured while saving the file :(");
            return null;
        }
    }

    public File findFile(int id) {
        return fileRepository.find(id);
    }

    public File findFileByCode(String code)
    {
        return urlRepository.findByCode(code).getFile();
    }

    public void store(MultipartFile file,String path, String filename) {
        try {
            if (file.isEmpty()) {
                System.out.println("EMPTY FILE!");
            }
            Files.copy(file.getInputStream(), Paths.get(path).resolve(filename));
        } catch (IOException e) {
            System.out.println("Exception while saving the file!");
            e.printStackTrace();
        }
    }

    public List<File> findAll()
    {
        return this.fileRepository.findAll();
    }

    public List<File> getAdminFiles()
    {
        return this.fileRepository.findAllAdminFiles();
    }
}
