package com.xwintop.xJavaFxTool.controller.index;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xwintop.xJavaFxTool.controller.IndexController;
import com.xwintop.xJavaFxTool.plugin.PluginManager;
import com.xwintop.xJavaFxTool.services.index.PluginManageService;
import com.xwintop.xJavaFxTool.services.index.WebManageService;
import com.xwintop.xJavaFxTool.utils.HttpRestUtils;
import com.xwintop.xJavaFxTool.view.index.WebManageView;
import com.xwintop.xcore.util.javafx.AlertUtil;
import com.xwintop.xcore.util.javafx.JavaFxViewUtil;
import com.xwintop.xcore.util.javafx.TooltipUtil;
import de.felixroske.jfxsupport.FXMLController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.util.Callback;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @Title:
 * @Description:
 * @author: anthor
 * @date:2022/9/210:04
 */
@FXMLController
@Getter
@Setter
@Slf4j
public class WebManageController extends WebManageView {


    public static final String FXML = "/com/xwintop/xJavaFxTool/fxmlView/index/WebManage.fxml";
    private IndexController indexController;
    private WebManageService webManageService = new WebManageService(this);
    private ObservableList<Map<String, String>> originWebData = FXCollections.observableArrayList();
    private FilteredList<Map<String, String>> webDataTableData = new FilteredList<>(originWebData, m -> true);


    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initView();
        initEvent();
        initService();

    }

    private void initView() {
        JavaFxViewUtil.setTableColumnMapValueFactory(nameTableColumn, "nameTableColumn");
        JavaFxViewUtil.setTableColumnMapValueFactory(WeburlTableColumn, "WeburlTableColumn");
        JavaFxViewUtil.setTableColumnMapValueFactory(TimeTableColumn, "TimeTableColumn");
        JavaFxViewUtil.setTableColumnMapAsCheckBoxValueFactory(isEnableTableColumn, "isEnableTableColumn",
                (mouseEvent, index) -> {
                    webManageService.setIsEnableTableColumn(index);
                });
        DeleteTableColumn.setCellFactory(
                new Callback<TableColumn<Map<String, String>, String>, TableCell<Map<String, String>, String>>() {
                    @Override
                    public TableCell<Map<String, String>, String> call(TableColumn<Map<String, String>, String> param) {
                        return new TableCell<Map<String, String>, String>() {
                            @Override
                            protected void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                this.setText(null);
                                this.setGraphic(null);
                                if (!empty) {
                                    Map<String, String> dataRow = webDataTableData.get(this.getIndex());
                                    Button deleteButton = new Button("??????");
                                    this.setContentDisplay(ContentDisplay.CENTER);
                                    deleteButton.setOnMouseClicked((me) -> {
                                        try {
                                            webManageService.deleteWebRow(dataRow);
                                            AlertUtil.showConfirmAlert("??????????????????");
                                        } catch (Exception e) {
                                            log.error("??????Web?????????", e);
                                        }
                                    });   this.setGraphic(deleteButton);
                                }
                            }
                        };
                    }
                });


        // TODO ???????????????????????????
        WebDataTableView.setItems(webDataTableData);

    }

    private void initEvent() {
        // ????????????
        MenuItem mnuSavePluginConfig = new MenuItem("????????????");
        mnuSavePluginConfig.setOnAction(ev -> {
            try {
                PluginManager.getInstance().saveToFile();
                TooltipUtil.showToast("??????????????????");
            } catch (Exception ex) {
                log.error("????????????????????????", ex);
            }
        });

        ContextMenu contextMenu = new ContextMenu(mnuSavePluginConfig);
        WebDataTableView.setContextMenu(contextMenu);

        // ??????
        selectWebTextField.textProperty().addListener((_ob, _old, _new) -> {
//            webManageService.searchWeb(_new);
        });


    }

    private void initService() {
        webManageService.getWebList();
    }

    @FXML
    private void selectPluginAction(ActionEvent event) {

    }
    @FXML
    void selectWebAction(ActionEvent event) {
        webManageService.searchPlugin(selectWebTextField.getText());
    }
    private void getWebResource() {

    }

    public static FXMLLoader getFXMLLoader() {
        return new FXMLLoader(IndexController.class.getResource(FXML));
    }
    @FXML
    public void addWebAction(ActionEvent actionEvent) {
        try {
            webManageService.addWebManagerService(AddWebTextField.getText());
            AlertUtil.showConfirmAlert("??????????????????????????????");
        }
        catch (Exception e)
        {
            AlertUtil.showConfirmAlert("????????????");
            log.error(e.toString());
        }

    }
}
