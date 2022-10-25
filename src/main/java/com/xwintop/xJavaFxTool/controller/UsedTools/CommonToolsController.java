package com.xwintop.xJavaFxTool.controller.UsedTools;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @auther 齿轮
 * @create 2022-09-02-8:45
 */
@Getter
@Setter
@Slf4j
public class CommonToolsController {
    public static List<String> getCommonToolsList() {
        List<String> toolsList = new ArrayList<>();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        Document doc = null;
        try {
            db = dbf.newDocumentBuilder();
            doc = db.parse("src/main/resources/com/xwintop/xJavaFxTool/controller/UsedTools/CommonTools.xml");
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
                    String id = tool.getAttributes().getNamedItem("name").getNodeValue();
                    toolsList.add(id);
                }
            }
        }
        return toolsList;
    }

    public static void addCommonTool(String toolName) throws IOException {
        if (getCommonToolsList().contains(toolName)) {
            System.out.println("已存在");
            return;
        }
        Scanner scanner = new Scanner(new File("src/main/resources/com/xwintop/xJavaFxTool/controller/UsedTools/CommonTools.xml"));
        String temp = "";
        while (scanner.hasNext()) {
            String string = scanner.nextLine();
            if (string.equals("</tools>")) break;
            temp += string + "\n";
        }
        FileWriter writer = null;
        try {
            writer = new FileWriter("src/main/resources/com/xwintop/xJavaFxTool/controller/UsedTools/CommonTools.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        writer.write(temp);
        writer.write("\t<tool name=\"" + toolName + "\"/>");
        writer.write("\n</tools>");
        writer.close();
    }

    public static void deleteCommonTool(String toolName) throws IOException {
        if (!getCommonToolsList().contains(toolName)) {
            System.out.println("已不存在");
            return;
        }
        Scanner scanner = new Scanner(new File("src/main/resources/com/xwintop/xJavaFxTool/controller/UsedTools/CommonTools.xml"));
        String temp = "";
        while (scanner.hasNext()) {
            String string = scanner.nextLine();
            if (string.contains(toolName)) continue;
            temp += string + "\n";
        }
        FileWriter writer = null;
        try {
            writer = new FileWriter("src/main/resources/com/xwintop/xJavaFxTool/controller/UsedTools/CommonTools.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        writer.write(temp);
        writer.close();

    }
}
