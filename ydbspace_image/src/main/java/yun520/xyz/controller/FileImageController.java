package yun520.xyz.controller;
//上传文件

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.log.Log;
import cn.hutool.system.UserInfo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import yun520.xyz.context.StoreContext;
import yun520.xyz.entity.File;
import yun520.xyz.entity.FileWeb;
import yun520.xyz.entity.Filechunk;
import yun520.xyz.entity.Sharelinks;
import yun520.xyz.mapper.FileMapper;
import yun520.xyz.mapper.FilechunkMapper;
import yun520.xyz.mapper.SharelinksMapper;
import yun520.xyz.result.Result;
import yun520.xyz.result.ResultUtils;
import yun520.xyz.service.RedisService;
import yun520.xyz.service.StoreService;
import yun520.xyz.service.impl.FastDfsServiceimpl;
import yun520.xyz.service.impl.FileServiceImpl;
import yun520.xyz.thread.DownThread;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

@RestController
@Api(tags = "FileImageController", description = "文件上传")
@Scope(value = "prototype")
public class FileImageController {
    private static Logger logger = Logger.getLogger("FileImageController.class");
    @Autowired
    StoreContext storeContext;
    @Autowired
    FileMapper filemapper;
    @Autowired
    SharelinksMapper sharelinksmapper;
    @Autowired
    FilechunkMapper filechunkMapper;

    @Autowired
    private RedisService redisService;

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
        String filename = file.getOriginalFilename();

//          文件名不可有中文
        String chunkpath = "";
        int result2 = 0;

//        synchronized (FileImageController.class) {
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);


            result2++;
        StoreService storeService = storeContext.getStoreService("0");
        chunkpath = storeService.upload("group1",file.getInputStream(),file.getSize(), String.valueOf(originalFilename+RandomUtil.randomString(3)));

            //填充切片表
            Filechunk filechunk = Filechunk.builder().chunkmd5(fileparams.getIdentifier()).chunksize(fileparams.getChunkSize()).chunkpath(chunkpath).chunktotalnum(fileparams.getTotalChunks()).chunksnum(fileparams.getChunkNumber())
                    .createTime(LocalDateTime.now()).build();

            result2 = filechunkMapper.insert(filechunk);
//        }

        //上传切片表
       logger.info("插入成功" + result2);

        HashMap<String, Object> objectObjectHashMap = new HashMap<>();

        objectObjectHashMap.put("result", result2);

        return ResultUtils.success(objectObjectHashMap);

    }

    @GetMapping("/upload")
    @ApiOperation(value = "第一次上传整个文件的信息，返回文件id")
    @Transactional
    public Result uplaodChunkGET(FileWeb fileparams) throws Exception {

        //上传文件表
        File file1 = File.builder().fileName(fileparams.getFilename()).fileType(fileparams.getFilename().substring(fileparams.getFilename().length() - 3)).fileSize(fileparams.getTotalSize()).fileSaveType("0").filemd5(fileparams.getIdentifier()
        ).createTime(LocalDateTime.now()).build();
        //填充文件表
        int result = filemapper.insert(file1); // 帮我们自动生成id

        //上传切片表
       logger.info("文件上传成功id" + file1.getFid());

        HashMap<String, Object> objectObjectHashMap = new HashMap<>();

        objectObjectHashMap.put("fid", file1.getFid());

        return ResultUtils.success(objectObjectHashMap);

    }

    @GetMapping("/download")
    @ApiOperation(value = "根据文件id｜文件md5下载文件")
    //为防止刷
    //todo 后续加个临时下载码存在redis ，330分过期
    public void downfile(FileWeb fileparams, HttpServletResponse response, HttpServletRequest request) throws Exception {


       logger.info("filemapper地址" + filemapper.hashCode());
       logger.info("*****开始时间*******" + DateUtil.now());
        //根据文件下md5载文件
        String filename = "";
        //往响应流中写入数据
//查文件名  重名怎么办 下载加上文件id
        QueryWrapper<File> queryWrapperfile = new QueryWrapper<File>();
        //true null拼接  false 不拼接
        queryWrapperfile.eq(false, "filemd5", fileparams.getIdentifier()).or().eq("fid", fileparams.getFid());
        List<File> userInfoList2 = filemapper.selectList(queryWrapperfile);
//得到块
        QueryWrapper<Filechunk> queryWrapper = new QueryWrapper<Filechunk>();
        queryWrapper.eq("chunkmd5", userInfoList2.get(0).getFilemd5()).orderByAsc("chunksnum");
        List<Filechunk> userInfoList = filechunkMapper.selectList(queryWrapper);

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

        StoreService storeService = storeContext.getStoreService("0");

        //多线程下载等待线程
        try {
            //线程等待计算正在写入第几片
            AtomicInteger automIterator = new AtomicInteger(1);
            //线程等待所有线程执行完成
            CountDownLatch countDownLatch = new CountDownLatch(userInfoList.get(0).getChunktotalnum());
            //创建下载线程池
            LinkedBlockingQueue<Runnable> linkedBlockingQueue = new LinkedBlockingQueue<>();
            ExecutorService executorService = new ThreadPoolExecutor(3, 3,
                    0L, TimeUnit.MILLISECONDS,
                    linkedBlockingQueue);

            userInfoList.forEach(val -> {
                executorService.execute(new DownThread(executorService, countDownLatch, automIterator, val, outputStream, storeService,linkedBlockingQueue));

            });
            countDownLatch.await();

           logger.info("********任务结束时间*********" + DateUtil.now());
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
            e.printStackTrace();
           logger.info("此时发生了异常");

        } finally {
           logger.info("结束流");
            //强制将缓存区的数据进行输出
            outputStream.flush();
            //关流
            outputStream.close();
        }

    }

    @PostMapping("/shareLinks")
    @ApiOperation(value = "生成文件分享连接")
    public Result sharelinks(@RequestBody Sharelinks sharelinks) throws Exception {

        sharelinks.setSharetime(LocalDateTime.now());
        sharelinks.setPassworld(RandomUtil.randomString(3));
        //这里后续通过redis自增1
        sharelinks.setDownweb(RandomUtil.randomString(3));
        Sharelinks.builder().sharetime(LocalDateTime.now());

        int insert = sharelinksmapper.insert(sharelinks);
        HashMap<String, Object> objectObjectHashMap = new HashMap<>();
        if (insert > 0) {

            objectObjectHashMap.put("result", sharelinks);

            return ResultUtils.success(objectObjectHashMap);
        }
        return ResultUtils.error("文件链接分享生成失败");
    }

    @GetMapping("/toencrypt")
    @ApiOperation(value = "查看文件是否加密")
    /*
    加密和不加密
    加密 弹窗输入验证码
    不加密 返回文件信息和下载码
     */
    public Result toencrypt(@RequestParam String downweb) throws Exception {
        HashMap<String, Object> objectObjectHashMap = new HashMap<>();
        //查文件名  重名怎么办 下载加上文件id
        QueryWrapper<Sharelinks> queryWrapperfile = new QueryWrapper<Sharelinks>();
        queryWrapperfile.eq("downweb", downweb);
        Sharelinks sharelinkone = sharelinksmapper.selectOne(queryWrapperfile);
        if (sharelinkone.getToencrypt()) {
            FileWeb fileweb = FileWeb.builder().toencrypt(true).build();
            objectObjectHashMap.put("result", fileweb);
            return ResultUtils.success(objectObjectHashMap);
        }

//得到块
        QueryWrapper<File> queryWrapper = new QueryWrapper<File>();
        queryWrapper.eq("fid", sharelinkone.getFileid());
        File file = filemapper.selectOne(queryWrapper);

//加密不返回文件id
//    不加密返回文件id  都返回一些必要的文件信息
        if (file != null) {
            FileWeb fileweb = FileWeb.builder().toencrypt(false).fid(file.getFid()).filename(file.getFileName()).filetype(file.getFileType()).build();
            objectObjectHashMap.put("result", fileweb);
            return ResultUtils.success(objectObjectHashMap);
        }

        return ResultUtils.error("查询错误");
    }

    @PostMapping("/sharePages2")
    @ApiOperation(value = "上送验证码和后坠加密下载")
    /*
    加密和不加密
    第一次告诉页面 需不需要验证码
     */
    public Result sharePages2(@RequestBody Sharelinks sharelinks) throws Exception {

        sharelinks.setSharetime(LocalDateTime.now());
        sharelinks.setPassworld(sharelinks.getPassworld());
        //todo 这里后续通过redis自增1
        sharelinks.setDownweb(RandomUtil.randomString(3));

        Sharelinks.builder().sharetime(LocalDateTime.now());

        int insert = sharelinksmapper.insert(sharelinks);
        HashMap<String, Object> objectObjectHashMap = new HashMap<>();
        if (insert > 0) {

            objectObjectHashMap.put("result", sharelinks);

            return ResultUtils.success(objectObjectHashMap);
        }
        return ResultUtils.error("插入失败");
    }

}
