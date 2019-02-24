package cz.vmacura.ear.upload.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import cz.vmacura.ear.upload.config.*;
import cz.vmacura.ear.upload.entities.Account;
import cz.vmacura.ear.upload.entities.File;
import cz.vmacura.ear.upload.entities.Url;
import cz.vmacura.ear.upload.environment.Environment;
import cz.vmacura.ear.upload.environment.Generator;
import cz.vmacura.ear.upload.service.AccountService;
import cz.vmacura.ear.upload.service.FileService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ContextConfiguration(classes = {AppConfig.class})
@WebAppConfiguration
public class FileControllerTest extends BaseControllerTest{


    @Mock
    private FileService fileServiceMock;

    @Mock
    private AccountService accountServiceMock;

    @InjectMocks
    private FileController fileController = new FileController(fileServiceMock, accountServiceMock);

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        super.setUp(fileController);

    }


    @Test
    public void getFilesReturnsAllFiles() throws Exception {
        final List<File> files = IntStream.range(0, 1).mapToObj(i -> {
            final File file = new File();
            file.setName("file-" + i);
            file.addUrl(new Url());
            return file;
        }).collect(Collectors.toList());
        when(fileServiceMock.findAll()).thenReturn(files);
        final MvcResult mvcResult = mockMvc.perform(get("/file/")).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
        final List<File> result = readValue(mvcResult, new TypeReference<List<File>>() {
        });
        System.out.println(result.toString());
        assertNotNull(result);
        assertEquals(files.size(), result.size());
        for (int i = 0; i < files.size(); i++) {
            assertEquals(files.get(i).getName(), result.get(i).getName());
        }
    }

}