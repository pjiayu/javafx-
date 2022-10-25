package com.xwintop.xJavaFxTool.services.index;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xwintop.xJavaFxTool.controller.IndexController;
import com.xwintop.xJavaFxTool.controller.index.WebManageController;
import com.xwintop.xJavaFxTool.utils.Config;
import com.xwintop.xJavaFxTool.utils.HttpRestUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import org.htmlparser.Parser;

import org.htmlparser.visitors.HtmlPage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

/**
 * @Title:
 * @Description:
 * @author: anthor
 * @date:2022/9/213:26
 */

@Slf4j
@Setter
public class WebManageService {

    private WebManageController webManageController;


    private String exportport="8090";


    public WebManageService(WebManageController webManageController) {
        this.webManageController = webManageController;
    }

    public void getWebList() {
        addDataRow();
    }

    private void addDataRow() {
        try {
            //api url地址
            String url = "http://127.0.0.1:" + exportport + "/InfoService/getBaseInfo";
            //post请求
            HttpMethod method = HttpMethod.POST;
            // 封装参数，千万不要替换为Map与HashMap，否则参数无法传递
            MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
            params.add("code", "receive");
            //发送http请求并返回结果
            String result = HttpRestUtils.post(url, params);
            JSONObject jsonObject = JSONObject.parseObject(result);
            String jsonObjectstr = jsonObject.getString("transmsg");
            String deleteString = "";
            for (int i = 0; i < jsonObjectstr.length(); i++) {
                if (jsonObjectstr.charAt(i) != '\\') {
                    deleteString += jsonObjectstr.charAt(i);
                }
            }
            JSONArray json = JSONArray.parseArray(deleteString);
            if (json.size() > 0) {
                for (int p = 0; p < json.size(); p++) {
                    JSONObject job = json.getJSONObject(p);
                    Map<String, String> dataRow = new HashMap<>();
                    dataRow.put("nameTableColumn", job.getString("linkname"));
                    dataRow.put("WeburlTableColumn", job.getString("linkurl"));
                    dataRow.put("TimeTableColumn", timeStamp2Date(job.getString("addtime")));
                    if (job.getString("isshow").equals("1")) {
                        dataRow.put("isEnableTableColumn", "true");
                    } else {
                        dataRow.put("isEnableTableColumn", "false");
                    }
                    dataRow.put("DeleteTableColumn", "删除");
                    webManageController.getOriginWebData().add(dataRow);

                }
            }
            return;
        } catch (Exception e) {
            log.error(e.toString());
            return;
        }


    }


    public static String timeStamp2Date(String time) {
        Long timeLong = Long.parseLong(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//要转换的时间格式
        Date date;
        try {
            date = sdf.parse(sdf.format(timeLong));
            return sdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setIsEnableTableColumn(Integer index) {
        Map<String, String> dataRow = webManageController.getOriginWebData().get(index);
        String Name = dataRow.get("nameTableColumn");
        try {
            //api url地址
            String url = "http://127.0.0.1:" + exportport + "/InfoService/setIsEnableTableColumn";
            //post请求
            HttpMethod method = HttpMethod.POST;
            // 封装参数，千万不要替换为Map与HashMap，否则参数无法传递
            MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
            params.add("code", Name);
            //发送http请求并返回结果
            HttpRestUtils.post(url, params);
            return;
        } catch (Exception e) {
            log.error(e.toString());
            return;
        }
    }

    public void deleteWebRow(Map<String, String> dataRow) {
        String WebName = dataRow.get("nameTableColumn");
        try {
            //api url地址
            String url = "http://127.0.0.1:" + exportport + "/InfoService/deleteTableColumn";
            //post请求
            HttpMethod method = HttpMethod.POST;
            // 封装参数，千万不要替换为Map与HashMap，否则参数无法传递
            MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
            params.add("code", WebName);
            //发送http请求并返回结果
            HttpRestUtils.post(url, params);
            return;
        } catch (Exception e) {
            log.error(e.toString());
            return;
        }
    }

    public void searchPlugin(String keyword) {
        webManageController.getWebDataTableData().setPredicate(map -> {
            if (StringUtils.isBlank(keyword)) {
                return true;
            } else {
                return isWebDataMatch(map, keyword);
            }
        });
    }

    private boolean isWebDataMatch(Map<String, String> map, String keyword) {
        return map.entrySet().stream().anyMatch(
                entry ->
                        !entry.getKey().equals("linkname") &&
                                entry.getValue().toLowerCase().contains(keyword.toLowerCase())
        );
    }

    public void addWebManagerService(String Weburl) {
        InputStream response = null;
        try {
            response = new URL(Weburl).openStream();
            Scanner scanner = new Scanner(response);
            String responseBody = scanner.useDelimiter("\\A").next();
            try {
                //api url地址
                String url = "http://127.0.0.1:" + exportport + "/InfoService/addTableColumn";
                //post请求
                HttpMethod method = HttpMethod.POST;
                // 封装参数，千万不要替换为Map与HashMap，否则参数无法传递
                MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
                Parser parser = new Parser(Weburl);
                parser.setEncoding(parser.getEncoding());
                HtmlPage htmlpage = new HtmlPage(parser);
                parser.visitAllNodesWith(htmlpage);
                String title = htmlpage.getTitle();
                params.add("webTitle", title);
                params.add("webUrl", Weburl);
                params.add("isshow", "1");
                params.add("dateTime", new Date().toString());
                //发送http请求并返回结果
                HttpRestUtils.post(url, params);
                return;
            } catch (Exception e) {
                log.error(e.toString());
                return;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
