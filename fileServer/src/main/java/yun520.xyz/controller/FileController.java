package yun520.xyz.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import yun520.xyz.entity.WpFile;
import yun520.xyz.entity.WpFileSlice;
import yun520.xyz.server.WpFileService;

import javax.annotation.Resource;

/**
 * @author qjf
 * @date 2022/10/24 20:17
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Resource
    private WpFileService wpFileService;

    @PostMapping("/upload")
    public WpFile upload(MultipartFile multipartFile, @RequestBody WpFileSlice wpFileSlice) {
        return wpFileService.upload(multipartFile, wpFileSlice);
    }

    @GetMapping("/down")
    public String down(@RequestParam(name = "md5", required = false) String md5,
                       @RequestParam(name = "id", required = false) Long id) {
        return wpFileService.down(md5, id);
    }
}
