package yun520.xyz.Comtroller;
//上传文件

import cn.hutool.core.date.DateTime;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import yun520.xyz.Result;
import yun520.xyz.ResultUtils;
import yun520.xyz.entity.File;
import yun520.xyz.entity.FileWeb;
import yun520.xyz.mapper.FileMapper;
import yun520.xyz.service.impl.FastDfsServiceimpl;
import yun520.xyz.service.impl.FileServiceImpl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;

@RestController
@Api(tags = "FileImageController", description = "图片文件上传")
public class FileImageController {
    @Autowired
    FastDfsServiceimpl fastdfs;
    @Autowired
    yun520.xyz.mapper.FileMapper filemapper;
    @GetMapping("/uploadImage")
    @ApiOperation(value = "上传图片")
    public Result uploadImage(){
        HashMap<String, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("10","你好");

        return ResultUtils.success(objectObjectHashMap);


    }


    @PostMapping("/upload")
    @ApiOperation(value = "上传文件")
    public  Result uplaodChunk(MultipartFile file,  FileWeb fileparams){
        String fileimage=file.getOriginalFilename();

        try {
//            文件名不可有中文
            String path=fastdfs.upload(file.getBytes(),"222314");
        } catch (IOException e) {
            e.printStackTrace();
        }

          //上传文件表
        File file1 =  File.builder().fileName(fileparams.getFilename()).fileType(fileparams.getFilename().substring(fileparams.getFilename().indexOf(".")+1)).fileSize(fileparams.getTotalSize()).fileSaveType("0").filemd5(fileparams.getIdentifier()
        ).createTime(LocalDateTime.now()).build();
        int result = filemapper.insert(file1); // 帮我们自动生成id
        //上传切片表
        System.out.println(result); // 受影响的行数
        System.out.println(fileimage);



        HashMap<String, Object> objectObjectHashMap = new HashMap<>();

        objectObjectHashMap.put("10","你好");

        return ResultUtils.success(objectObjectHashMap);



    }





}
