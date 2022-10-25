package com.xwintop.xJavaFxTool.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xwintop.xJavaFxTool.model.mysql.InfoEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InfoDao extends BaseMapper<InfoEntity> {
    
}
