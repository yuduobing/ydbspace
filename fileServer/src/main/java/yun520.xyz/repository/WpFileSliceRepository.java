package yun520.xyz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yun520.xyz.entity.WpFileSlice;

import java.util.List;

/**
 * @Author qjf
 * @Date 2022-10-28 10:18
 **/
@Repository
public interface WpFileSliceRepository  extends JpaRepository<WpFileSlice,Integer> {

    WpFileSlice findByMd5AndSortAndState(String md5, Integer sort, Integer state);

    List<WpFileSlice> findByFileIdAndState(Long id, Integer i);
}
