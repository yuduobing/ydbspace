package yun520.xyz.entity;

import cn.easyes.annotation.HighLight;
import cn.easyes.annotation.IndexField;
import cn.easyes.annotation.IndexId;
import cn.easyes.annotation.IndexName;
import cn.easyes.annotation.rely.Analyzer;
import cn.easyes.annotation.rely.FieldType;
import cn.easyes.annotation.rely.IdType;
import lombok.Data;

import java.io.Serializable;

@IndexName(value = "customer")
@Data
public class Doc implements Serializable {
    private static final long serialVersionUID = -1L;
    @IndexId(type = IdType.CUSTOMIZE)
    private Long id;
    private int accountNumber;
    private int balance;
    @HighLight
    private String firstName;
    @HighLight
    @IndexField(fieldType = FieldType.TEXT, analyzer = Analyzer.IK_MAX_WORD, searchAnalyzer = Analyzer.IK_SMART)
    private String lastName;
    @HighLight
    private int age;
    private String gender;
    private String address;
    private String employer;
    /**
     * 高亮返回值被映射的字段
     */
    private String highlightContent;
}
