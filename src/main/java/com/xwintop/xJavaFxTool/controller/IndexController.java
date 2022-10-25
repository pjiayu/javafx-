package com.xwintop.xJavaFxTool.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xwintop.xJavaFxTool.controller.UsedTools.CommonToolsController;
import com.xwintop.xJavaFxTool.controller.UsedTools.HistoryToolsController;
import com.xwintop.xJavaFxTool.controller.index.PluginManageController;
import com.xwintop.xJavaFxTool.controller.index.WebManageController;
import com.xwintop.xJavaFxTool.model.ToolFxmlLoaderConfiguration;
import com.xwintop.xJavaFxTool.services.IndexService;
import com.xwintop.xJavaFxTool.services.index.PluginManageService;
import com.xwintop.xJavaFxTool.services.index.SystemSettingService;
import com.xwintop.xJavaFxTool.utils.Config;
import com.xwintop.xJavaFxTool.utils.HttpRestUtils;
import com.xwintop.xJavaFxTool.utils.SpringUtil;
import com.xwintop.xJavaFxTool.utils.XJavaFxSystemUtil;
import com.xwintop.xJavaFxTool.view.IndexView;
import com.xwintop.xcore.util.ConfigureUtil;
import com.xwintop.xcore.util.HttpClientUtil;
import com.xwintop.xcore.util.javafx.AlertUtil;
import com.xwintop.xcore.util.javafx.JavaFxSystemUtil;
import com.xwintop.xcore.util.javafx.JavaFxViewUtil;
import com.xwintop.xcore.util.javafx.TooltipUtil;
import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import static com.xwintop.xcore.util.javafx.JavaFxViewUtil.setControllerOnCloseRequest;


@FXMLController
@Slf4j
@Getter
@Setter

public class IndexController extends IndexView {

    private Map<String, Menu> menuMap = new HashMap<String, Menu>();
    private Map<String, MenuItem> menuItemMap = new HashMap<String, MenuItem>();
    private IndexService indexService = new IndexService(this);
    private ContextMenu contextMenu = new ContextMenu();
    @Value("${server.port}")
    private String exportport;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.bundle = resources;
        initView();
        initCommonTools();//加载CommonTools工具栏
        initHistoryTools();//加载HistoryTools工具栏
        initEvent();
        try{
            //api url地址
            String url = "http://127.0.0.1:"+exportport+"/InfoService/getBaseInfo";
            //post请求
            HttpMethod method =HttpMethod.POST;
            // 封装参数，千万不要替换为Map与HashMap，否则参数无法传递
            MultiValueMap<String, String> params= new LinkedMultiValueMap<String, String>();
            params.add("code", "receive");
            //发送http请求并返回结果
            String result = HttpRestUtils.post(url,params);
            JSONObject jsonObject = JSONObject.parseObject(result);
            String jsonObjectstr=jsonObject.getString("transmsg");
            String deleteString = "";
            for (int i = 0; i < jsonObjectstr.length(); i++) {
                if (jsonObjectstr.charAt(i) != '\\') {
                    deleteString += jsonObjectstr.charAt(i);
                }
            }

            JSONArray json = JSONArray.parseArray(deleteString);
            if(json.size()>0){
                for(int p=0;p<json.size();p++) {
                    JSONObject job = json.getJSONObject(p);
                    if(job.getString("isshow").equals("1"))
                    this.indexService.addWebView(job.getString("linkname"),job.getString("linkurl"),null);
                }
            }
            return ;
        }catch (Exception e){
            return ;
        }

    }


    private void SetContextMenuOfHistory(Button button) {
        ContextMenu Menu = new ContextMenu();
        MenuItem item = new MenuItem("add to Common");
        MenuItem item2 = new MenuItem("delete from history");
        item.setOnAction(e -> {
            try {
                if (!CommonToolsController.getCommonToolsList().contains(button.getText())) {
                    Button newButton = new Button(button.getText());
                    newButton.setPrefWidth(100);
                    newButton.setPrefHeight(30);
                    newButton.setOnAction(button.getOnAction());
                    this.SetContextMenuOfCommon(newButton);
                    CommonTools.getChildren().add(newButton);
                    CommonToolsController.addCommonTool(button.getText());
                } else {
                    TooltipUtil.showToast("工具已存在");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        item2.setOnAction(e->{
            try {
                HistoryToolsController.deleteHistoryTool(button.getText());
                initHistoryTools();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        Menu.getItems().addAll(item,item2);
        button.setContextMenu(Menu);
        button.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {//右击触发
            @Override
            public void handle(ContextMenuEvent event) {
            }
        });

    }

    private void SetContextMenuOfCommon(Button button) {
        ContextMenu deleteMenu = new ContextMenu();
        MenuItem item = new MenuItem("delete from Common");
        item.setOnAction(e -> {
            try {
                CommonTools.getChildren().remove(button);
                CommonToolsController.deleteCommonTool(button.getText());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        deleteMenu.getItems().add(item);
        button.setContextMenu(deleteMenu);
        button.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {//右击触发
            @Override
            public void handle(ContextMenuEvent event) {
            }
        });
    }

    private void initCommonTools() {
        Label label = new Label(" 常用工具：");
        label.setFont(Font.font(18));
        label.setPrefHeight(50);
        CommonTools.getChildren().add(label);
        List<String> commonToolsList = CommonToolsController.getCommonToolsList();
        for (MenuItem menuItem : menuItemMap.values()) {
            String toolName = menuItem.getText();
            if (commonToolsList.contains(toolName)) {
                Button button = new Button(toolName);
                button.setPrefWidth(100);
                button.setPrefHeight(30);

                this.SetContextMenuOfCommon(button);

                button.setOnAction(menuItem.getOnAction());
                CommonTools.getChildren().add(button);
            }
        }
    }

    private void initHistoryTools() {
        HistoryTools.getChildren().removeAll(HistoryTools.getChildren());
        Label label = new Label(" 历史工具：");
        label.setFont(Font.font(18));
        label.setPrefHeight(50);
        HistoryTools.getChildren().add(label);
        Map<Integer, String> toolsMap = HistoryToolsController.getHistoryToolsMap();
        int i = 1;
        List<String> menuNames = new ArrayList<>();
        for (MenuItem menuItem : menuItemMap.values()) {
            menuNames.add(menuItem.getText());
        }
        for (; toolsMap.containsKey(i); i++) {
//            String toolName = toolsMap.get(i);
//            if (menuNames.contains(toolName)) {
//                Button button = new Button(toolName);
//                button.setPrefWidth(100);
//                button.setPrefHeight(30);
//                this.SetContextMenuOfHistory(button);
//                button.setOnAction(menuItemMap.get(toolName).getOnAction());
//                HistoryTools.getChildren().add(button);
//            }
            for (MenuItem menuItem:menuItemMap.values()){
                if (toolsMap.get(i).equals(menuItem.getText())){
                    Button button = new Button(menuItem.getText());
                    button.setPrefWidth(100);
                    button.setPrefHeight(30);
                    this.SetContextMenuOfHistory(button);
                    button.setOnAction(menuItem.getOnAction());
                    HistoryTools.getChildren().add(button);
                }
            }
        }
    }



    private void initView() {
        menuMap.put("toolsMenu", toolsMenu);
        menuMap.put("moreToolsMenu", moreToolsMenu);
        File libPath = new File("libs/");
        // 获取所有的.jar和.zip文件
        File[] jarFiles = libPath.listFiles((dir, name) -> name.endsWith(".jar"));
        if (jarFiles != null) {
            for (File jarFile : jarFiles) {
                if (!PluginManageService.isPluginEnabled(jarFile.getName())) {
                    continue;
                }
                try {
                    this.addToolMenu(jarFile);
                } catch (Exception e) {
                    log.error("加载工具出错：", e);
                }
            }
        }
    }

    private void initEvent() {
        myTextField.textProperty().addListener((observable, oldValue, newValue) -> selectAction(newValue));
        myButton.setOnAction(arg0 -> {
            selectAction(myTextField.getText());
        });
    }


    public void addToolMenu(File file) throws Exception {
        XJavaFxSystemUtil.addJarClass(file);
        Map<String, ToolFxmlLoaderConfiguration> toolMap = new HashMap<>();
        List<ToolFxmlLoaderConfiguration> toolList = new ArrayList<>();

        try (JarFile jarFile = new JarFile(file)) {
            JarEntry entry = jarFile.getJarEntry("config/toolFxmlLoaderConfiguration.xml");
            if (entry == null) {
                return;
            }
            InputStream input = jarFile.getInputStream(entry);
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(input);
            Element root = document.getRootElement();
            List<Element> elements = root.elements("ToolFxmlLoaderConfiguration");
            for (Element configurationNode : elements) {
                ToolFxmlLoaderConfiguration toolFxmlLoaderConfiguration = new ToolFxmlLoaderConfiguration();
                List<Attribute> attributes = configurationNode.attributes();
                for (Attribute configuration : attributes) {
                    BeanUtils.copyProperty(toolFxmlLoaderConfiguration, configuration.getName(), configuration.getValue());
                }
                List<Element> childrenList = configurationNode.elements();
                for (Element configuration : childrenList) {
                    BeanUtils.copyProperty(toolFxmlLoaderConfiguration, configuration.getName(), configuration.getStringValue());
                }
                if (StringUtils.isEmpty(toolFxmlLoaderConfiguration.getMenuParentId())) {
                    toolFxmlLoaderConfiguration.setMenuParentId("moreToolsMenu");
                }
                if (toolFxmlLoaderConfiguration.getIsMenu()) {
                    if (menuMap.get(toolFxmlLoaderConfiguration.getMenuId()) == null) {
                        toolMap.putIfAbsent(toolFxmlLoaderConfiguration.getMenuId(), toolFxmlLoaderConfiguration);
                    }
                } else {
                    toolList.add(toolFxmlLoaderConfiguration);
                }
            }
        }
        toolList.addAll(toolMap.values());
        this.addMenu(toolList);
    }

    private void addMenu(List<ToolFxmlLoaderConfiguration> toolList) {
        for (ToolFxmlLoaderConfiguration toolConfig : toolList) {
            try {
                if (StringUtils.isEmpty(toolConfig.getResourceBundleName())) {
                    if (StringUtils.isNotEmpty(bundle.getString(toolConfig.getTitle()))) {
                        toolConfig.setTitle(bundle.getString(toolConfig.getTitle()));
                    }
                } else {
                    ResourceBundle resourceBundle = ResourceBundle.getBundle(toolConfig.getResourceBundleName(), Config.defaultLocale);
                    if (StringUtils.isNotEmpty(resourceBundle.getString(toolConfig.getTitle()))) {
                        toolConfig.setTitle(resourceBundle.getString(toolConfig.getTitle()));
                    }
                }
            } catch (Exception e) {
                log.error("加载菜单失败", e);
            }
            if (toolConfig.getIsMenu()) {
                Menu menu = new Menu(toolConfig.getTitle());
                if (StringUtils.isNotEmpty(toolConfig.getIconPath())) {
                    ImageView imageView = new ImageView(new Image(toolConfig.getIconPath()));
                    imageView.setFitHeight(18);
                    imageView.setFitWidth(18);
                    menu.setGraphic(imageView);
                }
                menuMap.put(toolConfig.getMenuId(), menu);
            }
        }

        for (ToolFxmlLoaderConfiguration toolConfig : toolList) {
            if (toolConfig.getIsMenu()) {
                menuMap.get(toolConfig.getMenuParentId()).getItems().add(menuMap.get(toolConfig.getMenuId()));
            }
        }

        for (ToolFxmlLoaderConfiguration toolConfig : toolList) {
            if (toolConfig.getIsMenu()) {
                continue;
            }
            MenuItem menuItem = new MenuItem(toolConfig.getTitle());
            if (StringUtils.isNotEmpty(toolConfig.getIconPath())) {
                ImageView imageView = new ImageView(new Image(toolConfig.getIconPath()));
                imageView.setFitHeight(18);
                imageView.setFitWidth(18);
                menuItem.setGraphic(imageView);
            }
            if ("Node".equals(toolConfig.getControllerType())) {
                menuItem.setOnAction((ActionEvent event) -> {
                    indexService.addContent(menuItem.getText(), toolConfig.getUrl(), toolConfig.getResourceBundleName(), toolConfig.getIconPath());
                    try {
                        HistoryToolsController.addHistoryTool(menuItem.getText());
                        initHistoryTools();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                if (toolConfig.getIsDefaultShow()) {
                    indexService.addContent(menuItem.getText(), toolConfig.getUrl(), toolConfig.getResourceBundleName(), toolConfig.getIconPath());
                }
            } else if ("WebView".equals(toolConfig.getControllerType())) {
                menuItem.setOnAction((ActionEvent event) -> {
                    indexService.addWebView(menuItem.getText(), toolConfig.getUrl(), toolConfig.getIconPath());
                    try {
                        HistoryToolsController.addHistoryTool(menuItem.getText());
                        initHistoryTools();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                if (toolConfig.getIsDefaultShow()) {
                    indexService.addWebView(menuItem.getText(), toolConfig.getUrl(), toolConfig.getIconPath());
                }
            }
            menuMap.get(toolConfig.getMenuParentId()).getItems().add(menuItem);
            for (MenuItem menuItem1 : menuItemMap.values()) {

            }
            menuItemMap.put(menuItem.getText(), menuItem);
        }
    }

    public void selectAction(String selectText) {
        if (contextMenu.isShowing()) {
            contextMenu.hide();
        }
        contextMenu = indexService.getSelectContextMenu(selectText);
        contextMenu.show(myTextField, null, 0, myTextField.getHeight());
    }

    @FXML
    private void exitAction(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    private void closeAllTabAction(ActionEvent event) {
        tabPaneMain.getTabs().clear();
    }

    @FXML
    private void openAllTabAction(ActionEvent event) {
        for (MenuItem value : menuItemMap.values()) {
            value.fire();
        }
    }
    @FXML
    public void addWebviewAction(ActionEvent actionEvent)throws Exception {
        FXMLLoader fXMLLoader = WebManageController.getFXMLLoader();
        Parent root = fXMLLoader.load();
        WebManageController webManageController = fXMLLoader.getController();
        webManageController.setIndexController(this);
        JavaFxViewUtil.openNewWindow("常用Web管理",root);

    }

    @FXML
    private void addNodepadAction(ActionEvent event) {
        indexService.addNodepadAction(event);
    }

    @FXML
    private void addLogConsoleAction(ActionEvent event) {
        indexService.addLogConsoleAction(event);
    }

    @FXML
    private void pluginManageAction() throws Exception {
        FXMLLoader fXMLLoader = PluginManageController.getFXMLLoader();
        Parent root = fXMLLoader.load();
        PluginManageController pluginManageController = fXMLLoader.getController();
        pluginManageController.setIndexController(this);
        JavaFxViewUtil.openNewWindow(bundle.getString("plugin_manage"), root);
    }

    /**
     * @Title: addContent
     * @Description: 添加Content内容
     */
    private void addContent(String title, String className, String iconPath) {
        try {
//			Class<AbstractFxmlView> viewClass = (Class<AbstractFxmlView>) ClassLoader.getSystemClassLoader().loadClass(className);
            Class<AbstractFxmlView> viewClass = (Class<AbstractFxmlView>) Thread.currentThread().getContextClassLoader().loadClass(className);
            AbstractFxmlView fxmlView = SpringUtil.getBean(viewClass);
            if (singleWindowBootCheckBox.isSelected()) {
//				Main.showView(viewClass, Modality.NONE);
                Stage newStage = JavaFxViewUtil.getNewStage(title, iconPath, fxmlView.getView());
                newStage.setOnCloseRequest((WindowEvent event) -> {
                    setControllerOnCloseRequest(fxmlView.getPresenter(), event);
                });
                return;
            }
            Tab tab = new Tab(title);
            tab.setContent(fxmlView.getView());

            if (StringUtils.isNotEmpty(iconPath)) {
                ImageView imageView = new ImageView(new Image(iconPath));
                imageView.setFitHeight(18);
                imageView.setFitWidth(18);
                tab.setGraphic(imageView);
            }
            tabPaneMain.getTabs().add(tab);
            tabPaneMain.getSelectionModel().select(tab);
            tab.setOnCloseRequest((Event event) -> {
                setControllerOnCloseRequest(fxmlView.getPresenter(), event);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void SettingAction() {
        SystemSettingService.openSystemSettings(bundle.getString("Setting"));
    }



    @FXML
    private void setLanguageAction(ActionEvent event) throws Exception {
        MenuItem menuItem = (MenuItem) event.getSource();
        indexService.setLanguageAction(menuItem.getText());
    }

    @FXML
    private void openLogFileAction() {
        String filePath = "logs/logFile." + "log";
        JavaFxSystemUtil.openDirectory(filePath);
    }

    @FXML
    private void openLogFolderAction() {
        JavaFxSystemUtil.openDirectory("logs/");
    }

    @FXML
    private void openConfigFolderAction() {
        JavaFxSystemUtil.openDirectory(ConfigureUtil.getConfigurePath());
    }

    @FXML
    private void openPluginFolderAction() {
        JavaFxSystemUtil.openDirectory("libs/");
    }



}
