package yun520.xyz.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import yun520.xyz.entity.File;
import yun520.xyz.entity.FileWeb;
import yun520.xyz.entity.Userfile;
import yun520.xyz.mapper.FileMapper;
import yun520.xyz.mapper.SharelinksMapper;
import yun520.xyz.mapper.UserfileMapper;
import yun520.xyz.result.RestResult;
import yun520.xyz.result.Result;
import yun520.xyz.result.ResultUtils;
import yun520.xyz.service.IUserfileService;
import yun520.xyz.service.LoginService;
import yun520.xyz.vo.file.FileListVo;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author yuduobin
 * @since 2023-01-09
 */
@RestController
@RequestMapping("/web")
@Api(tags = "UserfileController", description = "用户文件管理")
public class UserfileController extends  BaseController {
    private static Logger logger = Logger.getLogger("UserfileController.class");
    @Value("${user.wpfiletype:AliYunOpen")
    private final String wpfiletype="AliYunOpen";

    @Autowired
    SharelinksMapper sharelinksmapper;
    @Autowired
    FileMapper filemapper;


    @Autowired
    UserfileMapper userfileMapper;

    @Autowired
    private LoginService loginService;
    @Autowired
    public IUserfileService iUserService;

    //查询分页
    @ApiOperation(value = "获取文件列表")
    @GetMapping(value = "/userfile/getfilelist")
    @ResponseBody
    public RestResult getFileList(
            @Parameter(description = "文件路径", required = false) String filePath,
            @Parameter(description = "当前页", required = true) long currentPage,
            @Parameter(description = "页面数量", required = true) long pageCount) {

        //       Assert.log principal=null
        IPage<FileListVo> fileList = iUserService.getFileList(getUserid(), filePath, currentPage, pageCount);
        Map<String, Object> map = new HashMap<>();
        map.put("total", fileList.getTotal());
        map.put("list", fileList.getRecords());

        return RestResult.success().data(map);

    }

    //创建文件目录
    @ApiOperation(value = "创建文件目录")
    @GetMapping(value = "/userfile/newMk")
    @ResponseBody
    public RestResult newMk(
            @Parameter(description = "文件路径", required = false) String filePath,
            @Parameter(description = "文件夹名称", required = false) String fileName) {
        Boolean fileList = iUserService.newMk(getUserid(), filePath, fileName);
        return RestResult.success();

    }

    @PostMapping("/upload")
    @ApiOperation(value = "上传文件")
    @Transactional
    public synchronized Result uplaodChunk(MultipartFile file, FileWeb fileparams) throws Exception {
        Assert.notNull(fileparams.getFileSaveType(),"filesavtype必传");

        iUserService.upload(file, fileparams);

        //上传切片表
        logger.info("插入成功");

        HashMap<String, Object> objectObjectHashMap = new HashMap<>();

        objectObjectHashMap.put("result", "1");

        return ResultUtils.success(objectObjectHashMap);

    }

    @GetMapping("/upload")
    @ApiOperation(value = "第一次上传整个文件的信息，返回文件id")
    @Transactional
    public Result uplaodChunkGET(FileWeb fileparams) throws Exception {
        //用户id
        Long userid = getUserid(fileparams.getUserId());

        //上传文件表
        File file1 = File.builder().fileName(fileparams.getFilename()).fileType(fileparams.getFilename().substring(fileparams.getFilename().length() - 3)).fileSize(fileparams.getTotalSize()).fileSaveType(wpfiletype).filemd5(fileparams.getIdentifier()
        ).createTime(LocalDateTime.now()).build();
        //填充文件表
        int result = filemapper.insert(file1); // 帮我们自动生成id


        //上传文件目录表
        Userfile userfile = Userfile.builder().userId(userid).isDir(0).filePath(fileparams.getFilePath()).fileName(fileparams.getFilename()).fileId(file1.getFid().toString())
                .extendName(fileparams.getFilename().substring(fileparams.getFilename().length() - 3)).deleteFlag(0).uploadTime(new Date()).build();

        int insert = userfileMapper.insert(userfile);
        if (result > 0 && insert > 0) {
            HashMap<String, Object> objectObjectHashMap = new HashMap<>();

            objectObjectHashMap.put("fid", file1.getFid());
            objectObjectHashMap.put("fileSaveType", wpfiletype);
            return ResultUtils.success(objectObjectHashMap);
        }

        return ResultUtils.error("插入信息失败");

    }



    //创建文件目录
    @ApiOperation(value = "删除文件 或目录")
    @GetMapping(value = "/userfile/deletemk")
    @ResponseBody
    public RestResult deletemk(FileWeb fileparams) {
        Boolean result = iUserService.deletemk( fileparams);

        return result?RestResult.success():RestResult.fail();
    }

}
