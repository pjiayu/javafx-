package com.xwintop.xJavaFxTool.controller.UsedTools;

import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @auther 齿轮
 * @create 2022-09-02-15:02
 */
@Slf4j

public class HistoryToolsController {
    public static Map<Integer, String> getHistoryToolsMap() {
        Map<Integer, String> toolsMap = new HashMap<>();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        Document doc = null;
        try {
            db = dbf.newDocumentBuilder();
            doc = db.parse("src/main/resources/com/xwintop/xJavaFxTool/controller/UsedTools/HistoryTools.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Element root = doc.getDocumentElement();
        if (root != null && root.getNodeName().equals("tools")) {
            NodeList tools = root.getChildNodes();

            //遍历数组
            for (int i = 0; i < tools.getLength(); i++) {
                Node tool = tools.item(i);
                if (tool.getNodeName().equals("tool")) {
                    String name = tool.getAttributes().getNamedItem("name").getNodeValue();
                    Integer sort = Integer.valueOf(tool.getAttributes().getNamedItem("sort").getNodeValue());
                    toolsMap.put(sort, name);
                }
            }
        }
        return toolsMap;
    }

    public static void addHistoryTool(String toolName) throws IOException {
        if (getHistoryToolsMap().values().contains(toolName)) {
            Map<Integer, String> toolsMap = HistoryToolsController.getHistoryToolsMap();
            if (toolsMap.get(1).equals(toolName)) return;
            Map<Integer, String> toolsMap2 = new HashMap<>();
            int i = 1;
            for (; toolsMap.containsKey(i); i++) {
                if (toolsMap.get(i).equals(toolName)) {
                    System.out.println(i);
                    System.out.println(toolName);
                    break;
                }
                toolsMap2.put(i + 1, toolsMap.get(i));
            }
            toolsMap2.put(i, toolsMap.get(i - 1));
            toolsMap2.put(1, toolName);
            i++;
            for (; toolsMap.containsKey(i); i++) {
                toolsMap2.put(i, toolsMap.get(i));
            }
            FileWriter writer = null;
            try {
                writer = new FileWriter("src/main/resources/com/xwintop/xJavaFxTool/controller/UsedTools/HistoryTools.xml");
            } catch (IOException e) {
                e.printStackTrace();
            }
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<tools>\n");
            for (i = 1; toolsMap2.containsKey(i); i++) {
                writer.write("\t<tool sort=\"" + i + "\" name=\"" + toolsMap2.get(i) + "\"/>\n");
            }
            writer.write("</tools>");
            writer.close();
        } else {
            Map<Integer, String> toolsMap = HistoryToolsController.getHistoryToolsMap();
            Map<Integer, String> toolsMap2 = new HashMap<>();
            for (int i = 1; toolsMap.containsKey(i); i++) {
                toolsMap2.put(i + 1, toolsMap.get(i));
            }
            toolsMap2.put(1, toolName);
            FileWriter writer = null;
            try {
                writer = new FileWriter("src/main/resources/com/xwintop/xJavaFxTool/controller/UsedTools/HistoryTools.xml");
            } catch (IOException e) {
                e.printStackTrace();
            }
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<tools>\n");
            for (int i = 1; toolsMap2.containsKey(i); i++) {
                writer.write("\t<tool sort=\"" + i + "\" name=\"" + toolsMap2.get(i) + "\"/>\n");
            }
            writer.write("</tools>");
            writer.close();
        }
    }

    public static void deleteHistoryTool(String toolName) throws IOException {
        Map<Integer, String> toolsMap = getHistoryToolsMap();
        if(!toolsMap.values().contains(toolName)){
            System.out.println("已删除");
            return;
        }else{
            int i = 1;
            for (;toolsMap.containsKey(i);i++){
                if (toolsMap.get(i).equals(toolName))break;
            }
            for (;toolsMap.containsKey(i+1);i++){
                toolsMap.put(i,toolsMap.get(i+1));
            }
            toolsMap.remove(i);
        }
        FileWriter writer = null;
        try {
            writer = new FileWriter("src/main/resources/com/xwintop/xJavaFxTool/controller/UsedTools/HistoryTools.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<tools>\n");
        for (int i = 1; toolsMap.containsKey(i); i++) {
            writer.write("\t<tool sort=\"" + i + "\" name=\"" + toolsMap.get(i) + "\"/>\n");
        }
        writer.write("</tools>");
        writer.close();
    }
}
