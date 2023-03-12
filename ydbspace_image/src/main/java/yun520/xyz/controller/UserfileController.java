package yun520.xyz.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;
import yun520.xyz.entity.File;
import yun520.xyz.entity.FileWeb;
import yun520.xyz.entity.Filechunk;
import yun520.xyz.entity.Userfile;
import yun520.xyz.mapper.FileMapper;
import yun520.xyz.mapper.FilechunkMapper;
import yun520.xyz.mapper.SharelinksMapper;
import yun520.xyz.mapper.UserfileMapper;
import yun520.xyz.result.RestResult;
import yun520.xyz.result.Result;
import yun520.xyz.result.ResultUtils;
import yun520.xyz.service.IUserfileService;
import yun520.xyz.service.StoreService;
import yun520.xyz.vo.file.FileListVo;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yuduobin
 * @since 2023-01-09
 */
@RestController
@RequestMapping("/web/userfile")
@Api(tags = "UserfileController", description = "用户文件管理")
public class UserfileController {
    @Autowired
    StoreService fastdfs;
    @Autowired
    FileMapper filemapper;
    @Autowired
    SharelinksMapper sharelinksmapper;

    @Autowired
    UserfileMapper userfileMapper;

    private static Logger logger = Logger.getLogger("UserfileController.class");
   @Autowired
   public IUserfileService iUserService;
    //查询分页
    @ApiOperation(value = "获取文件列表")
    @GetMapping(value = "/getfilelist")
    @ResponseBody
    public RestResult getFileList(
            @Parameter(description = "文件路径", required = false) String filePath,
            @Parameter(description = "当前页", required = true) long currentPage,
            @Parameter(description = "页面数量", required = true) long pageCount){

        IPage<FileListVo> fileList = iUserService.getFileList(null, filePath, currentPage, pageCount);
        Map<String, Object> map = new HashMap<>();
        map.put("total", fileList.getTotal());
        map.put("list", fileList.getRecords());

        return RestResult.success().data(map);

    }

    //创建文件目录
    @ApiOperation(value = "创建文件目录")
    @GetMapping(value = "/newMk")
    @ResponseBody
    public RestResult newMk(
            @Parameter(description = "文件路径", required = false) String filePath,
            @Parameter(description = "文件名称", required = false) String fileName){
        Boolean fileList = iUserService.newMk(null, filePath, fileName);
        return RestResult.success();

    }

    @PostMapping("/upload")
    @ApiOperation(value = "上传文件")
    @Transactional
    public synchronized Result uplaodChunk(MultipartFile file, FileWeb fileparams) throws Exception {

        iUserService.upload(file,fileparams);

        //上传切片表
        logger.info("插入成功" );

        HashMap<String, Object> objectObjectHashMap = new HashMap<>();

        objectObjectHashMap.put("result", "1");

        return ResultUtils.success(objectObjectHashMap);

    }

    @GetMapping("/upload")
    @ApiOperation(value = "第一次上传整个文件的信息，返回文件id")
    @Transactional
    public Result uplaodChunkGET(FileWeb fileparams) throws Exception {
        //用户id
        long userid=1;

        //上传文件表
        File file1 = File.builder().fileName(fileparams.getFilename()).fileType(fileparams.getFilename().substring(fileparams.getFilename().length() - 3)).fileSize(fileparams.getTotalSize()).fileSaveType("0").filemd5(fileparams.getIdentifier()
        ).createTime(LocalDateTime.now()).build();
        //填充文件表
        int result = filemapper.insert(file1); // 帮我们自动生成id


        //上传文件目录表
        Userfile userfile = Userfile.builder().userId(userid).isDir(0).filePath(fileparams.getFilePath()).fileName(fileparams.getFilename()).fileId(file1.getFid().toString()).extendName(fileparams.getFilename().substring(fileparams.getFilename().length() - 3)).deleteFlag(0).build();

        int insert = userfileMapper.insert(userfile);
        if (result>0&&insert>0){
            HashMap<String, Object> objectObjectHashMap = new HashMap<>();

            objectObjectHashMap.put("fid", file1.getFid());

            return ResultUtils.success(objectObjectHashMap);
        }


        return ResultUtils.error("插入信息失败");

    }

}
