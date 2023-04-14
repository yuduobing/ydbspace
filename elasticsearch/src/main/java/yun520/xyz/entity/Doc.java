package yun520.xyz.entity;

import cn.easyes.annotation.IndexField;
import cn.easyes.annotation.IndexId;
import cn.easyes.annotation.IndexName;
import cn.easyes.annotation.rely.FieldType;
import cn.easyes.annotation.rely.IdType;
import lombok.Data;

@IndexName(value = "customer")
@Data
public class Doc {
    private static final long serialVersionUID = -1L;
    @IndexId(type = IdType.CUSTOMIZE)
    private Long id;
    private int accountNumber;
    private int balance;
    private String firstName;
    private String lastName;
    private int age;
    private String gender;
    private String address;
    private String employer;

}
