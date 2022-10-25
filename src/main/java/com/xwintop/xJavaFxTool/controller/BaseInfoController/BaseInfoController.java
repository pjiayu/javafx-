package com.xwintop.xJavaFxTool.controller.BaseInfoController;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.xwintop.xJavaFxTool.model.mysql.InfoEntity;
import com.xwintop.xJavaFxTool.model.vo.MsgEntity;
import com.xwintop.xJavaFxTool.services.mybatisplusImpl.Info.InfoService;
import com.xwintop.xJavaFxTool.services.mybatisplusImpl.InfoServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/InfoService")
public class BaseInfoController {

    @Autowired
    InfoServiceImpl infoServiceImpl;

    @RequestMapping(value = "/getBaseInfo",method = RequestMethod.POST)
    public MsgEntity<InfoEntity> getUserBaseInfo()  throws JSONException {
        List<InfoEntity> infoEntitylist = infoServiceImpl.getAll();
        JSONArray array = new JSONArray();
        JSONObject jsonObject = null;
        InfoEntity info = null;
        for (int i = 0; i < infoEntitylist.size(); i++) {
            info = infoEntitylist.get(i);
            jsonObject = new JSONObject();
            jsonObject.put("linkname", info.getLinkname());
            jsonObject.put("linkurl", info.getLinkurl());
            jsonObject.put("isshow", info.getIsshow());
            jsonObject.put("addtime", info.getAddtime());
            array.add(jsonObject);
        }
        String json = JSON.toJSONString(array);
        return new MsgEntity<>("SUCCESS", "1",json);
    }
    @RequestMapping(value = "/setIsEnableTableColumn",method = RequestMethod.POST)
    public MsgEntity<String> changeIsEnableTable(@RequestBody String code)    {
        try {
            JSONObject jsonObject = JSONObject.parseObject(code);
            String jsonObjectstr=jsonObject.getString("code");
            JSONArray json = JSONArray.parseArray(jsonObjectstr);
            infoServiceImpl.updateByWebId(json.get(0).toString());
            return new MsgEntity<String>("success","1",null,null);
        }catch (Exception e)
        {
            log.error(e.toString());
            return new MsgEntity<String>("error","-1",null,null);
        }
    }
    @RequestMapping(value = "/deleteTableColumn",method = RequestMethod.POST)
    public MsgEntity<String> deleteTable(@RequestBody String code)    {
        try {
            JSONObject jsonObject = JSONObject.parseObject(code);
            String jsonObjectstr=jsonObject.getString("code");
            JSONArray json = JSONArray.parseArray(jsonObjectstr);
            infoServiceImpl.deleteByWebId(json.get(0).toString());
            return new MsgEntity<String>("success","1",null,null);
        }catch (Exception e)
        {
            log.error(e.toString());
            return new MsgEntity<String>("error","-1",null,null);
        }
    }
    @RequestMapping(value = "/addTableColumn",method = RequestMethod.POST)
    public MsgEntity<String> addWebTable(@RequestBody String code)    {
        try {
            JSONObject jsonObject = JSONObject.parseObject(code);
            String jsonObjectstr=jsonObject.getString("webTitle");
            JSONArray json = JSONArray.parseArray(jsonObjectstr);
            String jsonObjectstr2=jsonObject.getString("webUrl");
            JSONArray json2 = JSONArray.parseArray(jsonObjectstr2);

            infoServiceImpl.addWebService(json.get(0).toString(),json2.get(0).toString());
            return new MsgEntity<String>("success","1",null,null);
        }catch (Exception e)
        {
            log.error(e.toString());
            return new MsgEntity<String>("error","-1",null,null);
        }
    }





}
