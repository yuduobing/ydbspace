package yun520.xyz.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @description 文件表
 * @author zhengkai.blog.csdn.net
 * @date 2022-10-24
 */
@Entity
@Data
@Table(name="wp_file")
public class WpFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    /**
     * 自增id
     */
    @Column(name="id")
    private Long id;

    /**
     * 文件名称 collate utf8mb4_general_ci
     */
    @Column(name="file_name")
    private String fileName;

    /**
     * 文件md5值 collate utf8mb4_general_ci
     */
    @Column(name="md5")
    private String md5;

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

    public WpFile() {
    }

}