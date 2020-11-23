package com.mazhangjing.lab.BBDA;

import com.mazhangjing.lab.BBDA.DataProcess;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;


public class Main extends Application {
    final static String VERSION = "0.2.0";
    Button btn;

    public Parent initWindow() {
        HBox box = new HBox(); VBox root = new VBox();

        TextField ts = new TextField(); ts.setMinWidth(300); ts.setPromptText("Input Start Time I");
        TextField ts2 = new TextField(); ts2.setPromptText("Input Start Time II");
        TextField te = new TextField(); te.setPromptText("Input End Time I");
        TextField te2 = new TextField(); te2.setPromptText("Input End Time II");
        TextField mp = new TextField(); mp.setPromptText("MATLAB file Path (Must be csv file!!!)");
        TextField bp = new TextField(); bp.setPromptText("Balance Board Log file Path");
        TextField name = new TextField(); name.setPromptText("Input Name");
        TextField id = new TextField(); id.setPromptText("Input ID");
        TextField cond = new TextField(); cond.setPromptText("Input Condition");

        ProgressBar pb = new ProgressBar(100); pb.setMinSize(300,30);
        Label info = new Label();
        Button about = new Button("About");

        btn = new Button("Process");
        btn.setOnAction((event -> {
            btn.setText("Processing");
            btn.setDisable(true);
            root.getChildren().addAll(info,pb);

            Task task = new DataProcess().cook(
                    ts.getText(),ts2.getText(),te.getText(),te2.getText(),
                    mp.getText(),bp.getText(),
                    name.getText(),id.getText(),cond.getText()
            );
            pb.progressProperty().bind(task.progressProperty());

            info.textProperty().bind(task.messageProperty());
            //如果出现错误，则显示错误提示框，写入错误堆栈信息。删除进度条，恢复按钮，不清空文本框。
            task.exceptionProperty().addListener((e)-> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText("处理数据过程中出现错误");
                String err = "";
                err += "在处理过程中出现错误。请检查文件格式、字段填写是否有误,是否正在被别的程序读写，然后重试。";
                if (task.getException() != null && task.getException().getMessage() != null) {
                    StringWriter sw = new StringWriter();
                    task.getException().printStackTrace(new PrintWriter(sw));
                    err += "\n" + sw.toString();
                }
                alert.setContentText(err);
                alert.showAndWait().ifPresent((response)->{
                    btn.setDisable(false);
                    btn.setText("Process");
                    root.getChildren().removeAll(info,pb);
                });
            });
            //如果值变化，则弹出对话框，提示结束，并且清空信息，恢复按钮，隐藏进度条
            task.valueProperty().addListener((e) -> {
                //注意，开始时需要判断 null 不能直接转型
                if (task.getValue()!= null &&
                        Integer.parseInt(task.getValue().toString()) == 1){
                    Alert res = new Alert(Alert.AlertType.INFORMATION);
                    res.setTitle("Succeed");
                    res.setHeaderText("文件保存在： "+
                            name.getText().trim().toLowerCase() +
                            "_" + id.getText().trim().toLowerCase() + "_" +
                            cond.getText().trim().toLowerCase() +"_output.csv 中。");
                    res.showAndWait().filter((response)-> response == ButtonType.OK)
                            .ifPresent(
                                (response) -> {
                                ts.clear(); ts2.clear(); te.clear(); te2.clear();
                                mp.clear(); bp.clear(); name.clear(); id.clear(); cond.clear();
                                btn.setDisable(false);
                                btn.setText("Process");
                                root.getChildren().removeAll(info,pb);
                            }
                    );
                }
            });
            //稍后执行此线程
            EventQueue.invokeLater(()->new Thread(task).run());
        }));

        about.setOnAction((event -> {
            Alert adlg = new Alert(Alert.AlertType.NONE);
            adlg.setTitle("About");
            adlg.setHeaderText(String.format(
                    "Balance Board Data Analysis - version %s\nWritten by Corkine Ma",VERSION));
            adlg.setContentText(String.format("Model %s - version %s\nPowered by JAVA Fx & Java Platform 1.8"
            ,DataProcess.class.getSimpleName(),DataProcess.VERSION));
            adlg.getButtonTypes().addAll(new ButtonType("Support",ButtonBar.ButtonData.APPLY),
                    new ButtonType("Close",ButtonBar.ButtonData.CANCEL_CLOSE));
            adlg.showAndWait().ifPresent(
                    (response) -> {
                        if (response.getButtonData() == ButtonBar.ButtonData.APPLY)
                            try {
                                Desktop.getDesktop().browse(new URI("http://www.mazhangjing.com"));
                            } catch (Exception e) {}
                        else adlg.close();
                    });
        }));

        HBox btnBox = new HBox(); btnBox.setStyle("-fx-spacing: 10;");
        btnBox.getChildren().addAll(btn,about);
        root.getChildren().addAll(name,id,cond,ts,ts2,te,te2,mp,bp,btnBox);
        root.setStyle("-fx-alignment: center-left;-fx-padding: 10;-fx-spacing: 10");
        box.setAlignment(Pos.CENTER);
        box.getChildren().addAll(root);
        return box;

    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Balance Board Data Analysis - " + VERSION);
        primaryStage.setScene(new Scene(initWindow(), 400,500));
        primaryStage.show();

        btn.requestFocus();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
