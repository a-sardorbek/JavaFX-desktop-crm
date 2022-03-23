/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import databaseConnect.DbConnect;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import dto.Position;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import utils.AlertHelper;

/**
 * FXML Controller class
 *
 * @author User
 */
public class AddPosition implements Initializable {

    @FXML
    private Label employeeFiolabel;
    @FXML
    private TextField lavozimField;
    @FXML
    private TableView<Position> listViewTable;
    @FXML
    private TableColumn<Position, String> lavozimTable;
    @FXML
    private TableColumn<Position, String> actionTable;

    Position position;
     private final Connection con;
    
    public AddPosition(){
        con = DbConnect.getConnection(true);
    } 
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showPositions();
    }    

    @FXML
    private void addClicked(MouseEvent event) {
        if(isValidated()){
            saveRecord();
            clearFields();
            showPositions();
        }
    }
   
    private boolean isValidated(){
        
        if(lavozimField.getText().equals("")){
            
            AlertHelper.showAlert(AnimationType.POPUP, "Lavozim  kiritmadiz", "", NotificationType.ERROR, 2000);
            
            lavozimField.requestFocus();
        }else{
            return true;
        }
        return false;
    }
    
    private String checkString(String s){
        
        String last=s;
        
        if(s.contains("'") || s.contains(",") || s.contains(";")){
        String s1 = s.replace("'", "");
        String s2 = s1.replace(",", " ");
        String s3 = s2.replace(";", "");
        last = s3;
        }
        return last;
    }
    
    private void saveRecord(){
         
              String query = "insert into workposition (positionname) values ("+
                    
                                                "'"+checkString(lavozimField.getText())+"')";
        
        executeSave(query);
        AlertHelper.showAlert(AnimationType.POPUP, "Qo'shildi", "", NotificationType.SUCCESS, 2000);
         
     }
     
      private void executeSave(String query) {
        Statement st;
        try {
            st = con.createStatement();
            st.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
      
       public ObservableList<Position> getPositionList(){
        ObservableList<Position> positionList = FXCollections.observableArrayList();
        String query = "select * from workposition";
        Statement st;
        ResultSet rs;
        
          
          
        try{
            st = con.createStatement();
            rs = st.executeQuery(query);
        while(rs.next()){
            
            
           position = new Position(rs.getInt("id"), rs.getString("positionname"));
            
            positionList.add(position);
        }
        }catch(SQLException e){
            e.printStackTrace();
            System.out.println("Error in getting data");
        }
        return positionList;
    }
    
     public void showPositions(){
      
        ObservableList<Position> list = getPositionList();
        
        lavozimTable.setCellValueFactory(new PropertyValueFactory<>("position"));
       
         Callback<TableColumn<Position, String>, TableCell<Position, String>> cellFoctory = (TableColumn<Position, String> param) -> {
            
            final TableCell<Position, String> cell = new TableCell<Position, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {

                        FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
                        
                         deleteIcon.setStyle(
                                " -fx-cursor: hand ;"
                                + "-glyph-size:28px;"
                                + "-fx-fill:#ff1744;"
                        );
                         
                        
                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                            try {
                                position = listViewTable.getSelectionModel().getSelectedItem();
                                String query = "delete from workposition where id="+position.getId();
                                PreparedStatement prs = con.prepareStatement(query);
                                prs.execute();
                                AlertHelper.showAlert(AnimationType.POPUP, "Ma'lumot o'chdi", "", NotificationType.INFORMATION, 2000);
                                showPositions();
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        });
                        
                        HBox managebtn = new HBox(deleteIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 3));
                        setGraphic(managebtn);
                        setText(null);

                    }
                }

            };

            return cell;
        };
         actionTable.setCellFactory(cellFoctory);
        
        listViewTable.setItems(list);
     }

    private void clearFields() {
        lavozimField.setText("");
    }
}
