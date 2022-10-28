package yun520.xyz.server;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;
import yun520.xyz.ResultUtils;
import yun520.xyz.entity.WpFile;
import yun520.xyz.entity.WpFileSlice;

/**
 * @author qjf
 * @date 2022/10/23 22:22
 */
public interface WpFileService {

    WpFile upload(MultipartFile multipartFile, WpFileSlice file);

    String down(String md5, Long id);
}
