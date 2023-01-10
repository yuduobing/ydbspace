package yun520.xyz.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import yun520.xyz.result.RestResult;
import yun520.xyz.service.IUserfileService;
import yun520.xyz.vo.file.FileListVo;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yuduobin
 * @since 2023-01-09
 */
@Controller
@RestController("/web/userfile")
@Api(tags = "UserfileController", description = "用户文件管理")
public class UserfileController {
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


}
