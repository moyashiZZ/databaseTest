/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzukisqllite;

import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class FXMLDocumentController implements Initializable {
    
    private String file = "test.db";
    private String table = "tbl_adress";
    private Connection con;
    
    private IntegerProperty id;
    private StringProperty name;
    private StringProperty address;
    private StringProperty mail;
    private StringProperty tell;
    private StringProperty memo;
    
    private VBox input = new VBox();
    private TextField tfId = new TextField();
    private TextField tfName = new TextField();
    private TextField tfAddress = new TextField();
    private TextField tfMail = new TextField();
    private TextField tfTell = new TextField();
    private TextField tfMemo = new TextField();
   
    private TableView<Person> tableV = new TableView<>();
    private TableColumn idColumn = new TableColumn("ID");
    private TableColumn nameColumn = new TableColumn("NAME");
    private TableColumn adressColumn = new TableColumn("ADDRESS");
    private TableColumn mailColumn = new TableColumn("MAIL");;
    private TableColumn tellColumn = new TableColumn("TEL");
    private TableColumn memoColumn = new TableColumn("MEMO");
    
    private Person updataPerson;
    private int updataPersonIndex;

    private Image img = new Image( "topimg/topimg.png" );   
    private ImageView imgview = new  ImageView(img);
    
    private Statement smt;
    private ResultSet rs ;

    @FXML
    private Label label;
    @FXML
    private HBox main;
    @FXML
    private Label msg;
    @FXML
    private TextField field;
    @FXML   
    private Button btnIns;

    @FXML//メニューから一覧画面の呼び出し
    private void listScreen(ActionEvent event) {
        
        msg.setText("");
        dataSet();
        main.getChildren().clear();
        main.getChildren().add(tableV);
        btnIns.setVisible(false);
        editModeSwitch();

    }

    @FXML//メニューから削除画面の呼び出し
    private void deleteScreen(){

        msg.setText("");
        dataSet();
        main.getChildren().clear();
        main.getChildren().add(tableV);
        editModeSwitch();
         
        btnIns.setText("削除");
        btnIns.setVisible(true);

    }
    
    @FXML //メニューから編集可能な更新画面を呼び出し
    private void updateScreen(ActionEvent event){

        msg.setText("");
        btnIns.setText("更新");
        dataSet();
        main.getChildren().clear();
        main.getChildren().add(tableV);
        btnIns.setVisible(true);
        editModeSwitch();

    }
    
    @FXML//メニューから登録画面の呼び出し
    private void registrationScreen(ActionEvent event) {
        msg.setText("");                         
        inputScreen();                     
        btnIns.setText("登録");
        btnIns.setVisible(true);
    }
    
    @FXML
    private void closeAction(ActionEvent event) {
        Platform.exit();
        System.out.println("close");
    }
    
    //セルを編集可能にするスイッチ
    private void editModeSwitch(){
        
        //更新の時だけ編集できるようにする
        if (btnIns.getText().equals("更新")) {
            tableV.setEditable(true);
            
            nameColumn.setEditable(true);
            nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            
            adressColumn.setEditable(true);
            adressColumn.setCellFactory(TextFieldTableCell.forTableColumn());

            mailColumn.setEditable(true);
            mailColumn.setCellFactory(TextFieldTableCell.forTableColumn());

            tellColumn.setEditable(true);
            tellColumn.setCellFactory(TextFieldTableCell.forTableColumn());

            memoColumn.setEditable(true);
            memoColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        }
        
        
    }

    @FXML//テスト用仮登録
    private void testDataIn(ActionEvent event) {
      
        System.out.println("仮登録");
        label.setText("仮登録");
        
        try {
		PreparedStatement prep = con
		.prepareStatement("INSERT INTO " + table  +" VALUES (?, ?, ?, ?, ?, ?);"); 
                
                /////////////////////
		  prep.setInt(1, 1001);
                  prep.setString(2, "田中太郎");
                  prep.setString(3, "青森");
                  prep.setString(4, "xxxxx@yyy.com");
                  prep.setString(5, "0123-45-6789");
                  prep.setString(6, "自営業");
                  prep.addBatch();
                  
                  prep.setInt(1, 1002);
                  prep.setString(2, "山田太郎");
                  prep.setString(3, "秋田");
                  prep.setString(4, "xxxxx@yyy.com");
                  prep.setString(5, "090-1111-2222");
                  prep.setString(6, "既婚");                  
                  prep.addBatch();
                  
                  prep.setInt(1, 1003);
                  prep.setString(2, "佐藤恵子");
                  prep.setString(3, "宮城");
                  prep.setString(4, "xxxxx@yyy.com");
                  prep.setString(5, "080-4418-7812");
                  prep.setString(6, "8/31面談");                  
                  prep.addBatch();
                  
                  //prep.execute();
                     con.setAutoCommit(false);
			prep.executeBatch();

			con.commit();
                        
         } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        tableV.getItems();
        main.getChildren().clear();
        main.getChildren().add(tableV);
    }
 
    @FXML //画面毎のボタンアクション
    private void buttonAction(ActionEvent event) throws IOException{
        try {
            
            PreparedStatement prep = null; 
         
        ////////////登録画面を呼び出している場合
            if(btnIns.getText().equals("登録")){
              
                int addId = Integer.parseInt(tfId.getText());                
                String addName = tfName.getText();
                String addAddress = tfAddress.getText();
                String addMail = tfMail.getText();
                String addTell = tfTell.getText();
                String addMemo = tfMemo.getText();
		
                prep = con
		.prepareStatement("INSERT INTO " + table  +" VALUES (?, ?, ?, ?, ?, ?);"); 
                 con.setAutoCommit(false);
                /////////////////////
		  prep.setInt(1, addId);
                  prep.setString(2, addName);
                  prep.setString(3, addAddress);
                  prep.setString(4, addMail);
                  prep.setString(5, addTell);
                  prep.setString(6, addMemo);
                  msg.setText("登録完了");      
                  fieldClear();
        
        }
        
        ///////////////////////削除画面を呼び出している場合
        else if(btnIns.getText().equals("削除")){

                if (updataPerson != null) {
                    
                
            //クリックし選択されたperson(updataPerson)をtextFieldにセットする
            tfId.setText("" + updataPerson.idProperty().getValue()); 
            tfName.setText( updataPerson.nameProperty().getValue());
            tfAddress.setText( updataPerson.addressProperty().getValue());
            tfMail.setText( updataPerson.mailProperty().getValue());
            tfTell.setText( updataPerson.tellProperty().getValue());
            tfMemo.setText( updataPerson.memoProperty().getValue());
            
            inputScreen();  //ボタンを押すとtextFieldの画面へ推移する                        
            btnIns.setText("削除確認");
            btnIns.setVisible(true);
            return;
                }else {
                    System.out.println("データを選択してください");
                }
        }else if(btnIns.getText().equals("削除確認")){
              
                int addId = Integer.parseInt(tfId.getText());

                prep = con.prepareStatement("DELETE  FROM " + table + " WHERE id = ?"); 
              
                con.setAutoCommit(false);
                prep.setInt(1, addId);
                msg.setText("削除完了");  
                btnIns.setText("削除");
                fieldClear();
                updataPerson = null;
                prep.execute();
                con.commit();
                dataSet();

                main.getChildren().clear();
                tableV.getItems();
                main.getChildren().add(tableV);        
        }
        
        ///////////////////////////更新画面が呼び出されている場合
       
    else if(btnIns.getText().equals("更新")){
            
         prep = con.prepareStatement("UPDATE "+ table + 
           " SET NAME = '" + updataPerson.nameProperty().getValue()
         + "', ADRESS = '" + updataPerson.addressProperty().getValue()
         + "', MAIL = '" + updataPerson.mailProperty().getValue()
         + "', TELL = '" + updataPerson.tellProperty().getValue()
         + "', MEMO = '" + updataPerson.memoProperty().getValue()
         + "' WHERE ID = " + updataPerson.idProperty().getValue()
         + " ;"); 
     
        con.setAutoCommit(false);
        msg.setText("更新完了");
        System.out.println("更新完了");
        tfId.setEditable(true);
        
        }
         
        //updatePersonが選択されている場合と登録モードのときだけコミットする
        if(updataPerson != null || msg.getText() == "登録完了"){
        prep.execute();
        con.commit();
        }
        ///////////////////////////////
        } catch (SQLException e) {
            
            e.printStackTrace();
        }
     }
    
    //textFieldへの書き込みをクリアする
    private void fieldClear(){
        tfId.clear();
        tfName.clear();
        tfMail.clear();
        tfAddress.clear();
        tfTell.clear();
        tfMemo.clear();
    }
    
    //sqlのデータをperson rsにセットする
    private void dataSet(){  
       
        try {
            tableV.getItems().clear();
            rs = smt.executeQuery("SELECT * FROM " + table + ";");
            
            while (rs.next()) {
                /////////////////                
                Person person = new Person(
                rs.getInt("ID"), rs.getString("NAME"), rs.getString("ADRESS"),
                rs.getString("MAIL"), rs.getString("TELL"),  rs.getString("MEMO"));                       
                ///////////////////////
                tableV.getItems().add(person);
                System.out.println("ID = " + rs.getInt("ID"));
                System.out.println("NAME = " + rs.getString("NAME"));
                System.out.println("ADRESS = " + rs.getString("ADRESS"));
                System.out.println("MAIL = " + rs.getString("MAIL"));
                System.out.println("TELL = " + rs.getString("TELL"));
                System.out.println("MEMO = " + rs.getString("MEMO"));
            } 
                        
        } catch (SQLException e) {
            e.printStackTrace();
        }   
     }
     
    //textFieldの画面を生成
    private void  inputScreen(){
      
        main.getChildren().clear();
        input.getChildren().clear();
        
        input.getChildren().add(new Label("ID："));  
        input.getChildren().add(tfId);
        
        input.getChildren().add(new Label("NAME："));
        input.getChildren().add(tfName);
        
        input.getChildren().add(new Label("ADDRESS："));      
        input.getChildren().add(tfAddress);
        
        input.getChildren().add(new Label("MAIL："));
        input.getChildren().add(tfMail);
        
        input.getChildren().add(new Label("TELL："));
        input.getChildren().add(tfTell);
        
        input.getChildren().add(new Label("MEMO："));
        input.getChildren().add(tfMemo);
        
        main.getChildren().clear();
        main.getChildren().add(input);
        
  }
  
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
	
            Class.forName("org.sqlite.JDBC");
	
            con = DriverManager.getConnection("jdbc:sqlite:" + file);
            smt = con.createStatement();
            smt.execute("CREATE TABLE IF NOT EXISTS " + table +
                    "(ID INTEGER , NAME TEXT, ADRESS TEXT, MAIL TEXT, TELL TEXT, MEMO TEXT);");
	
        } catch (ClassNotFoundException | SQLException e) {}
                
        idColumn.setCellValueFactory(new PropertyValueFactory<Person, Integer>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("name"));
        adressColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("address"));
        mailColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("mail"));
        tellColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("tell"));
        memoColumn.setCellValueFactory(new PropertyValueFactory<Person, String>("memo"));
        
//        idColumn.setMaxWidth(Double.MAX_EXPONENT);
//        nameColumn.setMaxWidth(Double.MAX_EXPONENT);
//        adressColumn.setMaxWidth(Double.MAX_EXPONENT);
//        mailColumn.setMaxWidth(Double.MAX_EXPONENT);
//        tellColumn.setMaxWidth(Double.MAX_EXPONENT);
//        memoColumn.setMaxWidth(Double.MAX_EXPONENT);
//        
        tableV.autosize();
//        tableV.columnResizePolicyProperty();
//        tableV.setMaxSize(main.getMaxHeight(), main.getMaxWidth());
        
        tableV.getColumns().add(idColumn);
        tableV.getColumns().add(nameColumn);
        tableV.getColumns().add(adressColumn);
        tableV.getColumns().add(mailColumn);
        tableV.getColumns().add(tellColumn);
        tableV.getColumns().add(memoColumn);
        
        
        
 //////////////////////////////////////////       
 
        tableV.addEventHandler(MouseEvent.MOUSE_CLICKED,(event)->{
            
            if (tableV.getSelectionModel().getSelectedIndex() == -1){
                System.out.println("データが存在しません");
            }else{ 
                updataPersonIndex = tableV.getSelectionModel().getSelectedIndex();
   
                //クリックしたpersonのindexが保存される
                updataPerson = tableV.getItems().get(updataPersonIndex);
            }
        });
        
        main.setAlignment(Pos.CENTER);        
        main.getChildren().add(imgview);
        btnIns.setVisible(false);    
	}
}
