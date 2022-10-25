package com.xwintop.xJavaFxTool.services.mybatisplusImpl;

import com.xwintop.xJavaFxTool.model.mysql.InfoEntity;
import com.xwintop.xJavaFxTool.services.mybatisplusImpl.Info.InfoService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class InfoServiceImpl {

    @Autowired
    private InfoService infoService;

    public List<InfoEntity> getAll() {
       return  infoService.list();
    }

    public void updateByWebId(String name) {
        InfoEntity infoEntity=infoService.getById(name);
        if(infoEntity.getIsshow().equals("1"))
        infoEntity.setIsshow("0");
        else
            infoEntity.setIsshow("1");
        infoService.updateById(infoEntity);
        return;
    }

    public void deleteByWebId(String name) {
        infoService.removeById(name);
        return;
    }

    public void addWebService(String WebName,String WebUrl) {
        infoService.saveOrUpdate(new InfoEntity(WebName,WebUrl,"1",new Date()));
        return;
    }
}
