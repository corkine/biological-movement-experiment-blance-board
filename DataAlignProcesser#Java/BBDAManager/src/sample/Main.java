package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;

public class Main extends Application {
    public static final String VERSION = "0.1.1";
    ObservableList<User> data = FXCollections.observableArrayList(
            new User("2018-06-22 10:35:20.868380", "2018-06-22 10:35:25.018380" ,"2018-06-22 10:53:39.280380" ,"2018-06-22 10:53:41.380380" ,
                    "01_jiangwanyue_scr.csv", "01_jiangwanyue_scr_201806221153.log" ,"jiangwenyue","scr","01"),
            new User("2018-06-22 10:35:20.868380", "2018-06-22 10:35:25.018380" ,"2018-06-22 10:53:39.280380" ,"2018-06-22 10:53:41.380380" ,
                    "01_jiangwanyue_scr.csv", "01_jiangwanyue_scr_201806221153.log" ,"corkine","scr","02"),
            new User("2018-06-22 10:35:20.868380", "2018-06-22 10:35:25.018380" ,"2018-06-22 10:53:39.280380" ,"2018-06-22 10:53:41.380380" ,
                    "01_jiangwanyue_scr.csv", "01_jiangwanyue_scr_201806221153.log" ,"test","scr","03")
    );

    @Override
    public void start(Stage primaryStage) throws Exception{
        getData();

        TableColumn<User,String> name = new TableColumn("Name"); name.setCellValueFactory(new PropertyValueFactory<>("name")); name.setCellFactory(TextFieldTableCell.forTableColumn());
        name.setOnEditCommit((TableColumn.CellEditEvent<User,String> t) ->  t.getTableView().getItems().get(t.getTablePosition().getRow()).setName(t.getNewValue()));
        TableColumn<User,String> id = new TableColumn("Id"); id.setCellValueFactory(new PropertyValueFactory<>("id")); id.setCellFactory(TextFieldTableCell.forTableColumn());
        id.setOnEditCommit((TableColumn.CellEditEvent<User,String> t2) ->  t2.getTableView().getItems().get(t2.getTablePosition().getRow()).setId(t2.getNewValue()));
        TableColumn<User,String> cond = new TableColumn("Condition"); cond.setCellValueFactory(new PropertyValueFactory<>("cond")); cond.setCellFactory(TextFieldTableCell.forTableColumn());
        cond.setOnEditCommit((TableColumn.CellEditEvent<User,String> t3) ->  t3.getTableView().getItems().get(t3.getTablePosition().getRow()).setCond(t3.getNewValue()));
        TableColumn<User,String> start_i = new TableColumn("Start I"); start_i.setCellValueFactory(new PropertyValueFactory<>("startTime1")); start_i.setCellFactory(TextFieldTableCell.forTableColumn());
        start_i.setOnEditCommit((TableColumn.CellEditEvent<User,String> t4) ->  t4.getTableView().getItems().get(t4.getTablePosition().getRow()).setStartTime1(t4.getNewValue()));
        TableColumn<User,String> start_ii = new TableColumn("Start II"); start_ii.setCellValueFactory(new PropertyValueFactory<>("startTime2")); start_ii.setCellFactory(TextFieldTableCell.forTableColumn());
        start_ii.setOnEditCommit((TableColumn.CellEditEvent<User,String> t5) ->  t5.getTableView().getItems().get(t5.getTablePosition().getRow()).setStartTime2(t5.getNewValue()));
        TableColumn<User,String> end_i = new TableColumn("End I"); end_i.setCellValueFactory(new PropertyValueFactory<>("endTime1")); end_i.setCellFactory(TextFieldTableCell.forTableColumn());
        end_i.setOnEditCommit((TableColumn.CellEditEvent<User,String> t6) ->  t6.getTableView().getItems().get(t6.getTablePosition().getRow()).setEndTime1(t6.getNewValue()));
        TableColumn<User,String> end_ii = new TableColumn("End II"); end_ii.setCellValueFactory(new PropertyValueFactory<>("endTime2")); end_ii.setCellFactory(TextFieldTableCell.forTableColumn());
        end_ii.setOnEditCommit((TableColumn.CellEditEvent<User,String> t7) ->  t7.getTableView().getItems().get(t7.getTablePosition().getRow()).setEndTime2(t7.getNewValue()));
        TableColumn<User,String> mPath = new TableColumn("mPath"); mPath.setCellValueFactory(new PropertyValueFactory<>("mPath")); mPath.setCellFactory(TextFieldTableCell.forTableColumn());
        mPath.setOnEditCommit((TableColumn.CellEditEvent<User,String> t8) ->  t8.getTableView().getItems().get(t8.getTablePosition().getRow()).setmPath(t8.getNewValue()));
        TableColumn<User,String> bPath = new TableColumn("bPath"); bPath.setCellValueFactory(new PropertyValueFactory<>("bPath")); bPath.setCellFactory(TextFieldTableCell.forTableColumn());
        bPath.setOnEditCommit((TableColumn.CellEditEvent<User,String> t9) ->  t9.getTableView().getItems().get(t9.getTablePosition().getRow()).setbPath(t9.getNewValue()));
        TableView<User> tableView = new TableView();

        tableView.getColumns().addAll(name,id,cond,start_i,start_ii,end_i,end_ii,mPath,bPath);
        tableView.setItems(data);

        VBox root = new VBox();
        tableView.setPrefWidth(1320);
        tableView.setEditable(true);
        root.setAlignment(Pos.CENTER);
        Button add = new Button("Add");
        Button remove = new Button("Remove");
        Button batch = new Button("Batch");
        HBox group = new HBox(); group.getChildren().addAll(add,remove,batch); group.setAlignment(Pos.CENTER_LEFT); group.setSpacing(15); group.setPadding(new Insets(20,0,0,0));

        root.getChildren().addAll(tableView,group); root.setPadding(new Insets(20,20,20,20));
        VBox.setVgrow(tableView,Priority.ALWAYS);
        HBox.setHgrow(tableView,Priority.ALWAYS);

        primaryStage.setTitle("BBDA Manager - " + VERSION);
        Scene scene = new Scene(root, 1360, 720);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest((event -> {
            String result = saveData();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Data Saved");
            if (result != null) alert.setContentText(result); else alert.setContentText("Successful!");
            alert.showAndWait().ifPresent((buttonType -> System.exit(0)));
        }));
        add.setOnAction((event -> {
            data.add(new User("","","","","","","User","int",""));
            tableView.scrollTo(tableView.getItems().size());
            tableView.getSelectionModel().select(tableView.getItems().get(tableView.getItems().size()-1));
            tableView.requestFocus();
        }));
        remove.setOnAction((event -> {
            data.remove(tableView.getSelectionModel().getSelectedItem());
            tableView.requestFocus();
        }));
        batch.setOnAction(event -> drawInput());
    }

    String saveData(){
        try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream("data.dta"))) {
            ArrayList<SerialUser> list = new ArrayList<>();
            for (User user:data) {
                SerialUser suser = new SerialUser();
                suser.transUser(user);
                list.add(suser);
            }
            stream.writeObject(list);
            return null;
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            return sw.toString();
        }
    }
    void getData() {
        try (ObjectInputStream stream = new ObjectInputStream(new FileInputStream("data.dta"))) {
            ArrayList<User> list = new ArrayList<>();
            for (SerialUser suser :(ArrayList<SerialUser>)stream.readObject()) {
                list.add(suser.toUser());
            }
            data.clear();
            data.addAll(list);
        } catch (FileNotFoundException e1) {}
        catch (Exception e2) {
            StringWriter sw = new StringWriter();
            e2.printStackTrace(new PrintWriter(sw));
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Data restore failed!");
            alert.setContentText(sw.toString());
            alert.show();
        }
    }
    void drawInput() {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setHeaderText("Input batch command");
        TextArea area = new TextArea(); VBox root = new VBox(); root.getChildren().addAll(new Label("程序会自动在命令后添加各个单元格的字符串，并用空格隔开，" +
                "第一个单元格前自动加了空格。\n添加的字符串的顺序和当前表格呈现单元格的顺序相同。按下确定会自动生成命令行，复制到CMD中执行" +
                "\n批处理命令执行需要BBDA.jar DataProcess模块0.3.0及以上版本"),area);
        root.setPadding(new Insets(20,10,10,10)); root.setSpacing(10);
        area.setText("java -cp BBDA.jar com.mazhangjing.lab.BBDA.DataProcess");
        dialog.getDialogPane().setContent(root);
        dialog.showAndWait().filter(buttonType -> buttonType == ButtonType.OK).ifPresent(buttonType -> {
           String fx = area.getText().trim();
           StringBuilder sb = new StringBuilder();
           for (User user : data) {
               sb.append(fx).append(" ");
               sb.append("\"").append(user.getName()).append("\"").append(" ");
               sb.append("\"").append(user.getId()).append("\"").append(" ");
               sb.append("\"").append(user.getCond()).append("\"").append(" ");
               sb.append("\"").append(user.getStartTime1()).append("\"").append(" ");
               sb.append("\"").append(user.getStartTime2()).append("\"").append(" ");
               sb.append("\"").append(user.getEndTime1()).append("\"").append(" ");
               sb.append("\"").append(user.getEndTime2()).append("\"").append(" ");
               sb.append("\"").append(user.getmPath()).append("\"").append(" ");
               sb.append("\"").append(user.getbPath()).append("\"").append(" ");
               sb.append("\n");
           }
           showResult(sb.toString());
        });
    }

    void showResult(String result) {
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setHeaderText("Result");
        TextArea area = new TextArea(); VBox root = new VBox(); root.getChildren().addAll(new Label("复制到CMD中执行" +
                "\n批处理命令执行需要BBDA.jar DataProcess模块0.3.0及以上版本"),area);
        root.setPadding(new Insets(20,10,10,10)); root.setSpacing(10);
        dialog.getDialogPane().setContent(root);
        area.setText(result);
        dialog.show();

    }
    public static void main(String[] args) {
        launch(args);
    }
}