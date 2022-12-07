package yun520.xyz.Comtroller;
//上传文件

import cn.hutool.core.date.DateTime;
import cn.hutool.log.Log;
import cn.hutool.system.UserInfo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import yun520.xyz.Result;
import yun520.xyz.ResultUtils;
import yun520.xyz.entity.File;
import yun520.xyz.entity.FileWeb;
import yun520.xyz.entity.Filechunk;
import yun520.xyz.mapper.FileMapper;
import yun520.xyz.mapper.FilechunkMapper;
import yun520.xyz.service.impl.FastDfsServiceimpl;
import yun520.xyz.service.impl.FileServiceImpl;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
@Api(tags = "FileImageController", description = "图片文件上传")
public class FileImageController {
    @Autowired
    FastDfsServiceimpl fastdfs;
    @Autowired
    yun520.xyz.mapper.FileMapper filemapper;
    @Autowired
    FilechunkMapper filechunkMapper;
    @GetMapping("/uploadImage")
    @ApiOperation(value = "上传图片")
    public Result uploadImage(){
        HashMap<String, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("10","你好");

        return ResultUtils.success(objectObjectHashMap);


    }


    @PostMapping("/upload")
    @ApiOperation(value = "上传文件")
    @Transactional
    public  Result uplaodChunk(MultipartFile file,  FileWeb fileparams)throws Exception{
        String fileimage=file.getOriginalFilename();


//          文件名不可有中文
            String chunkpath=fastdfs.upload(file.getBytes(),"222314");
            //上传文件表
            File file1 =  File.builder().fileName(fileparams.getFilename()).fileType(fileparams.getFilename().substring(fileparams.getFilename().indexOf(".")+1)).fileSize(fileparams.getTotalSize()).fileSaveType("0").filemd5(fileparams.getIdentifier()
            ).createTime(LocalDateTime.now()).build();
            //填充切片表
            Filechunk filechunk =  Filechunk.builder().chunksize(fileparams.getChunkSize()).chunkpath(chunkpath).chunktotalnum(fileparams.getTotalChunks()).chunksnum(fileparams.getChunkNumber())
            .createTime(LocalDateTime.now()).build();
            int result = filemapper.insert(file1); // 帮我们自动生成id
            int result2 = filechunkMapper.insert(filechunk);

            //上传切片表
        System.out.println("插入成功"+result+result2);




        HashMap<String, Object> objectObjectHashMap = new HashMap<>();

        objectObjectHashMap.put("result",result+result2);

        return ResultUtils.success(objectObjectHashMap);



    }
    @GetMapping("/upload")
    @ApiOperation(value = "第一次上传整个文件的信息")
    @Transactional
    public  Result uplaodChunkGET( FileWeb fileparams)throws Exception{


//            文件名不可有中文

        //上传文件表
        File file1 =  File.builder().fileName(fileparams.getFilename()).fileType(fileparams.getFilename().substring(fileparams.getFilename().indexOf(".")+1)).fileSize(fileparams.getTotalSize()).fileSaveType("0").filemd5(fileparams.getIdentifier()
        ).createTime(LocalDateTime.now()).build();
        //填充文件表
           int result = filemapper.insert(file1); // 帮我们自动生成id


        //上传切片表
        System.out.println("插入成功"+result);




        HashMap<String, Object> objectObjectHashMap = new HashMap<>();

        objectObjectHashMap.put("result",result);

        return ResultUtils.success(objectObjectHashMap);



    }


    @GetMapping("/download")
    @ApiOperation(value = "根据文件md5下载文件")
    @Transactional

    public  Result downfile(FileWeb fileparams, HttpServletResponse response)throws Exception{

        OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
      //根据文件下md5载文件

     //参考文章  https://www.jianshu.com/p/f8ac039fef5b




        String filepath="/Users/yu/Documents/asda.png";
        try {
        QueryWrapper<Filechunk> queryWrapper = new QueryWrapper<Filechunk>();
        queryWrapper.ge("chunkmd5",22).orderByAsc("chunksnum");
        List<Filechunk> userInfoList = filechunkMapper.selectList(queryWrapper);

        userInfoList.forEach(val->{
            byte[] download = fastdfs.download(val.getChunkpath());
            try {
                outputStream.write(download);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        //根据文件设置
        response.setContentType("application/zip");

    //读取指定路径下面的文件

//        InputStream in = new FileInputStream(filepath);
//
//        //创建存放文件内容的数组
//        byte[] buff = new byte[1024];
//        //所读取的内容使用n来接收
//        int n;
//        //当没有读取完时,继续读取,循环
//        while ((n = in.read(buff)) != -1) {
//            //将字节数组的数据全部写入到输出流中
//            outputStream.write(buff, 0, n);
//        }
        //强制将缓存区的数据进行输出
        outputStream.flush();
        //关流
        outputStream.close();

    } catch (IOException e) {

    }



        HashMap<String, Object> objectObjectHashMap = new HashMap<>();



        objectObjectHashMap.put("result","12");

        return ResultUtils.success(objectObjectHashMap);



    }


}
