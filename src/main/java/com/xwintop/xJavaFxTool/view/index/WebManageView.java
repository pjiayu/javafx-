package com.xwintop.xJavaFxTool.view.index;

/**
 * @Title:
 * @Description:
 * @author: anthor
 * @date:2022/9/210:19
 */


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public abstract class WebManageView implements Initializable {

    @FXML
    protected Button AddWebButton;

    @FXML
    protected TextField AddWebTextField;

    @FXML
    protected TableColumn<Map<String, String>, String> TimeTableColumn;

    @FXML
    protected TableView<Map<String, String>> WebDataTableView;

    @FXML
    protected TableColumn<Map<String, String>, String> WeburlTableColumn;

    @FXML
    protected TableColumn<Map<String, String>, String> isEnableTableColumn;

    @FXML
    protected TableColumn<Map<String, String>, String> nameTableColumn;

    @FXML
    protected TableColumn<Map<String, String>, String> DeleteTableColumn;

    @FXML
    protected Button selectWebButton;

    @FXML
    protected TextField selectWebTextField;



}