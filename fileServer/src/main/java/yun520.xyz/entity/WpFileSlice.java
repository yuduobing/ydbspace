package yun520.xyz.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @author qjf
 * @date 2022/10/23 22:28
 */
@Data
public class WpFileSlice implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    private Long id;

    /**
     * 文件id
     */
    private Long fileId;

    /**
     * 文件名称 collate utf8mb4_general_ci
     */
    private String fileName;

    /**
     * http路径 collate utf8mb4_general_ci
     */
    private String url;

    /**
     * 排序
     */
    private int sort;

    /**
     * 创建人 collate utf8mb4_general_ci
     */
    private String createdBy;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 更新人 collate utf8mb4_general_ci
     */
    private String updatedBy;

    /**
     * 更新时间
     */
    private Date updatedTime;

    /**
     * 状态
     */
    private int state;

}