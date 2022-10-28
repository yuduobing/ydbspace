package yun520.xyz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yun520.xyz.entity.WpFile;

/**
 * @author qjf
 * @date 2022/10/24 20:36
 */
public interface WpFileRepository extends JpaRepository<WpFile,Integer> {

    WpFile findByMd5AndState(String md5, Integer state);

    WpFile findByIdAndState(Long id, Integer i);
}
