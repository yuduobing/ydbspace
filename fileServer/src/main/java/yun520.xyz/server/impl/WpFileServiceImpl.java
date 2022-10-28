package yun520.xyz.server.impl;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import yun520.xyz.entity.WpFile;
import yun520.xyz.entity.WpFileSlice;
import yun520.xyz.repository.WpFileRepository;
import yun520.xyz.repository.WpFileSliceRepository;
import yun520.xyz.server.WpFileService;
import yun520.xyz.service.StoreService;

import javax.annotation.Resource;

/**
 * @author qjf
 * @date 2022/10/23 22:23
 */
@Service
public class WpFileServiceImpl implements WpFileService {

    @Resource
    private WpFileRepository wpFileRepository;

    @Resource
    private WpFileSliceRepository wpFileSliceRepository;

    @Resource
    private StoreService storeService;

    @Override
    public WpFile upload(MultipartFile multipartFile, WpFileSlice file) {
        Assert.notNull(file,  "file不能为空");
        Assert.isTrue(!Objects.isNull(file.getMd5()) || !Objects.isNull(file.getSort()), "参数不全");
        WpFileSlice wpFileSlice = wpFileSliceRepository.findByMd5AndSortAndState(file.getMd5(), file.getSort(), 1);
        WpFile wpFile = wpFileRepository.findByMd5AndState(file.getMd5(), 1);
        if (!Objects.isNull(wpFileSlice)) {
            return wpFile;
        } else {
            file.setFileName(getUrl(multipartFile, file.getSort()));
            if (Objects.isNull(wpFile)) {
                WpFile nweWpFile = new WpFile();
                nweWpFile.setFileName(file.getFileName());
                nweWpFile.setMd5(file.getMd5());
                WpFile save = wpFileRepository.save(nweWpFile);
                return save;
            } else {
                file.setFileId(wpFile.getId());
                wpFileSliceRepository.save(file);
                return wpFile;
            }
        }
    }

    @Override
    public String down(String md5, Long id) {
        WpFile wpFile = null;
        if (StringUtils.isNoneBlank(md5)) {
            wpFile = wpFileRepository.findByMd5AndState(md5, 1);
        }
        if (Objects.isNull(id)) {
            wpFile = wpFileRepository.findByIdAndState(id, 1);
        }
        if (Objects.isNull(wpFile)) {
            return null;
        }
        List<WpFileSlice> wpFileSliceList = wpFileSliceRepository.findByFileIdAndState(wpFile.getId(), 1);

        return null;
    }

    /**
     * 文件上传
     * @param multipartFile
     * @param sort
     * @return
     */
    public String getUrl(MultipartFile multipartFile, Integer sort) {
        String name = multipartFile.getName().concat(String.valueOf(sort));
        try {
            return storeService.upload(multipartFile.getBytes(), name);
        } catch (IOException e) {
            return null;
        }
    }
}
