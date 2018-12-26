package cn.com.clm.param;


import cn.com.clm.util.annotation.RowIndex;
import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * describe: reflection
 *
 * @author liming.cao
 */
public class People {

    private Long id;

    @RowIndex(value = 1, header = "UUID")
    private String uuid;

    @RowIndex(value = 3, header = "姓名")
    private String name;

    @RowIndex(value = 2, type = Date.class, header = "出生日期")
    private Date birth;

    @RowIndex(value = 4, type = Integer.class, header = "性别")
    private int sex;

    @RowIndex(value = 5, header = "邮箱")
    private String email;

    @RowIndex(value = 6, header = "地址")
    private String address;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
