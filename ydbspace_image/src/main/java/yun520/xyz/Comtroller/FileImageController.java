package yun520.xyz.Comtroller;
//上传文件

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import yun520.xyz.Result;
import yun520.xyz.ResultUtils;

import java.util.HashMap;

@RestController("/fileimage")
@Api(tags = "FileImageController", description = "图片文件上传")
public class FileImageController {
    @GetMapping("/uploadImage")
    @ApiOperation(value = "上传图片")
    public Result uploadImage(){
        HashMap<String, Object> objectObjectHashMap = new HashMap<>();

        objectObjectHashMap.put("10","你好");

        return ResultUtils.success(objectObjectHashMap);



    }


    @PostMapping("/uploadChunk")
    @ApiOperation(value = "上传文件")
    public  Result uplaodChunk(MultipartFile file){
        String fileimage=file.getOriginalFilename();




        System.out.println(fileimage);



        HashMap<String, Object> objectObjectHashMap = new HashMap<>();

        objectObjectHashMap.put("10","你好");

        return ResultUtils.success(objectObjectHashMap);



    }





}
