package yun520.xyz.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @description 文件切片表
 * @author zhengkai.blog.csdn.net
 * @date 2022-10-28
 */
@Entity
@Data
@Table(name="wp_file_slice")
public class WpFileSlice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    /**
     * 自增id
     */
    @Column(name="id")
    private Long id;

    /**
     * 文件id
     */
    @Column(name="file_id")
    private Long fileId;

    /**
     * 文件名称 collate utf8mb4_general_ci
     */
    @Column(name="file_name")
    private String fileName;

    /**
     * 文件的md5值 collate utf8mb4_general_ci
     */
    @Column(name="md5")
    private String md5;

    /**
     * http路径 collate utf8mb4_general_ci
     */
    @Column(name="url")
    private String url;

    /**
     * 排序
     */
    @Column(name="sort")
    private int sort;

    /**
     * 创建人 collate utf8mb4_general_ci
     */
    @Column(name="created_by")
    private String createdBy;

    /**
     * 创建时间
     */
    @Column(name="created_time")
    private Date createdTime;

    /**
     * 更新人 collate utf8mb4_general_ci
     */
    @Column(name="updated_by")
    private String updatedBy;

    /**
     * 更新时间
     */
    @Column(name="updated_time")
    private Date updatedTime;

    /**
     * 状态
     */
    @Column(name="state")
    private int state;

    public WpFileSlice() {
    }

}