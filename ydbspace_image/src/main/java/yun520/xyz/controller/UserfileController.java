package yun520.xyz.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.beust.jcommander.Parameter;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import yun520.xyz.RestResult;
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
@RequestMapping("/web/userfile")
public class UserfileController {
   @Autowired
   public IUserfileService iUserService;
    //查询分页
    @ApiOperation(value = "获取文件列表")
    @GetMapping(value = "/getfilelist")
    @ResponseBody
    public RestResult getFileList(
            @Parameter(description = "文件路径", required = true) String filePath,
            @Parameter(description = "当前页", required = true) long currentPage,
            @Parameter(description = "页面数量", required = true) long pageCount){

        IPage<FileListVo> fileList = iUserService.getFileList(null, filePath, currentPage, pageCount);

        Map<String, Object> map = new HashMap<>();
        map.put("total", fileList.getTotal());
        map.put("list", fileList.getRecords());


        return RestResult.success().data(map);

    }

}
