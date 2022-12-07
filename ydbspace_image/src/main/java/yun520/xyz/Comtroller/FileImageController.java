package yun520.xyz.Comtroller;
//上传文件

import cn.hutool.core.date.DateTime;
import cn.hutool.log.Log;
import cn.hutool.system.UserInfo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
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
import java.net.URLEncoder;
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
    public Result uploadImage() {
        HashMap<String, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("10", "你好");

        return ResultUtils.success(objectObjectHashMap);

    }

    @PostMapping("/upload")
    @ApiOperation(value = "上传文件")
    @Transactional
    public synchronized Result uplaodChunk(MultipartFile file, FileWeb fileparams) throws Exception {
        String fileimage = file.getOriginalFilename();

//          文件名不可有中文
        String chunkpath = "";
        int result2 = 0;

        synchronized (FileImageController.class) {
            result2++;
            chunkpath = fastdfs.upload(file.getBytes(), String.valueOf(fileparams.getChunkNumber()));
            System.out.println("此时插入" + result2);
            //填充切片表
            Filechunk filechunk = Filechunk.builder().chunkmd5(fileparams.getIdentifier()).chunksize(fileparams.getChunkSize()).chunkpath(chunkpath).chunktotalnum(fileparams.getTotalChunks()).chunksnum(fileparams.getChunkNumber())
                    .createTime(LocalDateTime.now()).build();

            result2 = filechunkMapper.insert(filechunk);
        }

        //上传切片表
        System.out.println("插入成功" + result2);

        HashMap<String, Object> objectObjectHashMap = new HashMap<>();

        objectObjectHashMap.put("result", result2);

        return ResultUtils.success(objectObjectHashMap);

    }

    @GetMapping("/upload")
    @ApiOperation(value = "第一次上传整个文件的信息")
    @Transactional
    public Result uplaodChunkGET(FileWeb fileparams) throws Exception {

//            文件名不可有中文

        //上传文件表
        File file1 = File.builder().fileName(fileparams.getFilename()).fileType(fileparams.getFilename().substring(fileparams.getFilename().length() - 3)).fileSize(fileparams.getTotalSize()).fileSaveType("0").filemd5(fileparams.getIdentifier()
        ).createTime(LocalDateTime.now()).build();
        //填充文件表
        int result = filemapper.insert(file1); // 帮我们自动生成id

        //上传切片表
        System.out.println("插入成功" + result);

        HashMap<String, Object> objectObjectHashMap = new HashMap<>();

        objectObjectHashMap.put("result", result);

        return ResultUtils.success(objectObjectHashMap);

    }

    @GetMapping("/download")
    @ApiOperation(value = "根据文件md5下载文件")
    public void downfile(FileWeb fileparams, HttpServletResponse response) throws Exception {
        //根据文件下md5载文件
        String filename = "";

        //往响应流中写入数据

        QueryWrapper<Filechunk> queryWrapper = new QueryWrapper<Filechunk>();
        queryWrapper.eq("chunkmd5", fileparams.getIdentifier()).orderByAsc("chunksnum");
        List<Filechunk> userInfoList = filechunkMapper.selectList(queryWrapper);

//查文件名  重名怎么办 下载加上文件id
        QueryWrapper<File> queryWrapperfile = new QueryWrapper<File>();
        queryWrapperfile.eq("filemd5", fileparams.getIdentifier());
        List<File> userInfoList2 = filemapper.selectList(queryWrapperfile);

        //参考文章  https://www.jianshu.com/p/f8ac039fef5b
//
// 这步很重要，需要在给前端返回的请求头中添加Content-Disposition字段，否则前端会取不到Content-Disposition的信息
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        //文件下载响应头的设置
        //content-type 指示响应内容的格式
        //
        //content-disposition 指示如何处理响应内容。
        //
        //一般有两种方式：
        //
        //inline：直接在页面显示
        //attchment：以附件形式下载
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(userInfoList2.get(0).getFileName(), "utf-8"));
        //设置响应头中文件的为在页面直接显示，以及设置文件名
        //resp.setHeader("Content-Disposition","inline; filename=" + fileName);
        //设置响应头的编码格式为UTF-8
        response.setCharacterEncoding("UTF-8");
        //设置响应头中文件类型为pdf格式
        response.setContentType("application/" + userInfoList2.get(0).getFileType());
        OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        try {
            userInfoList.forEach(val -> {
                byte[] download = fastdfs.download(val.getChunkpath());
                try {
                    outputStream.write(download);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

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

        } catch (Exception e) {

        } finally {
            //强制将缓存区的数据进行输出
            outputStream.flush();
            //关流
            outputStream.close();
        }

    }

}
