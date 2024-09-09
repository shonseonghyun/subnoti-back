package com.sunghyun.football.global.file;

import com.sunghyun.football.domain.stadium.domain.StadiumImage;
import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    @InjectMocks
    private FileService fileService;

    @BeforeEach
    public void setup(){
        ReflectionTestUtils.setField(fileService, "fileDir", "C:/Users/sungh/Desktop/img");
    }

    @DisplayName("MultiFile인스턴스를 StadiumImgae로 변환")
    @Test
    void multiFileInstanceToStadiumImage(){
        final String originalFileName = "imgName1.img";

        MockMultipartFile file = new MockMultipartFile("imgName1", "imgName1.img", MediaType.IMAGE_JPEG_VALUE, "img".getBytes());
        List<MultipartFile> multipartFiles = Arrays.asList(file);

        List<StadiumImage> stadiumImages = fileService.doProcess(multipartFiles);

        Assertions.assertThat(multipartFiles.size()).isEqualTo(stadiumImages.size());
        Assertions.assertThat(stadiumImages.get(0).getOriginalFileName()).isEqualTo(originalFileName);
        Assertions.assertThat(stadiumImages.get(0).getNewFileName()).isNotBlank();
        Assertions.assertThat(stadiumImages.get(0).getFilePath()).isNotBlank();
    }

}