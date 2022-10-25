package com.xwintop.xJavaFxTool.services.mybatisplusImpl.Info;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xwintop.xJavaFxTool.dao.InfoDao;
import com.xwintop.xJavaFxTool.model.mysql.InfoEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class InfoService extends ServiceImpl<InfoDao, InfoEntity> {

}
