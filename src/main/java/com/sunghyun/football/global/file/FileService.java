package com.sunghyun.football.global.file;

import com.sunghyun.football.domain.stadium.domain.StadiumImage;
import com.sunghyun.football.global.exception.ErrorCode;
import com.sunghyun.football.global.exception.exceptions.file.FileUploadFailException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class FileService {

    @Value("${file.dir}")
    private String fileDir;

    public List<StadiumImage> doProcess(List<MultipartFile> stadiumImageFiles) {
        if(stadiumImageFiles==null || stadiumImageFiles.size()==0){
            return null;
        }

       List<StadiumImage> stadiumImages = convertToStadiumImage(stadiumImageFiles);

        return stadiumImages;
    }

    private List<StadiumImage> convertToStadiumImage(List<MultipartFile> multipartFiles){
        List<StadiumImage> stadiumImages = new ArrayList<>();
        for(MultipartFile multipartFile:multipartFiles){
            final String originalFileName = multipartFile.getOriginalFilename();
            final String filePath = getFilePath();
            final String newFileName = getNewFileName(originalFileName);
            final Integer fileSize = (int)multipartFile.getSize();

            try{
                uploadFile(multipartFile,filePath,newFileName);
            }catch (FileUploadFailException e){
                return Collections.EMPTY_LIST;
            }

            stadiumImages.add(
                    StadiumImage.builder()
                            .originalFileName(originalFileName)
                            .fileSize(fileSize)
                            .newFileName(newFileName)
                            .filePath(filePath)
                            .build()
            );
        }
        return stadiumImages;
    }

    private String getNewFileName(String originalFilename){
        int pos = originalFilename.lastIndexOf(".");
        String uuid = UUID.randomUUID().toString();
        String ext = originalFilename.substring(pos+1);
        return uuid + "." + ext;
    }

    private String getFilePath(){
        String folderPath = LocalDate.now().format(DateTimeFormatter.ofPattern("/yyyy/MM/dd"));
        String uploadFolderPath = fileDir+folderPath;
        uploadFolderPath = uploadFolderPath.replace("/", File.separator);

        //폴더 생성
        File uploadFolder = new File(fileDir.replace("/",File.separator),folderPath.replace("/",File.separator));
        if(!(uploadFolder.exists())){
            log.info(uploadFolder.toString() + " 폴더 생성 진행");
            uploadFolder.mkdirs();
            log.info(uploadFolder.toString() + " 폴더 생성 완료");
        }

        // 폴더
        return uploadFolderPath;
    }

    private void uploadFile(MultipartFile multipartFile,String filePath,String newFileName)  {
        try {
            multipartFile.transferTo(new File(filePath+File.separator+newFileName));
        } catch (IOException e) {
            log.info("[{}] 파일 업로드 실패하였습니다.",multipartFile.getOriginalFilename());
            throw new FileUploadFailException(ErrorCode.FILE_UPLOAD_FAIL);
        }
    }
}
