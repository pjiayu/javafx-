package com.xwintop.xJavaFxTool.model.mysql;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("baseinfo")

public class InfoEntity {

    @TableId("linkname")
    private String linkname;
    private String linkurl;
    private String isshow;
    private Date addtime;

    public InfoEntity(String linkname, String linkurl, String isshow, Date addtime) {
        this.linkname = linkname;
        this.linkurl = linkurl;
        this.isshow = isshow;
        this.addtime = new Date();
    }

    public InfoEntity() {
    }
    //注册用

}
