
package controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import databaseConnect.DbConnect;
import dto.Position;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import utils.AlertHelper;


public class AddController implements Initializable {

    @FXML
    private JFXTextField fioField;
    @FXML
    private JFXTextField phoneField;
    @FXML
    private JFXDatePicker dateField;
    @FXML
    private JFXComboBox<String> positionCombo;
    
    private final Connection con;
    
    public AddController(){
        con = DbConnect.getConnection(true);
    } 

    Position position;
  
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getPositionList();
    }    

    @FXML
    private void addClicked(MouseEvent event) {
        if(isValidated()){
            saveRecord();
        }
    }

    @FXML
    private void cancelClicked(MouseEvent event) {
        clearFields();
    }
    
    
      private void saveRecord(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = dateField.getValue().format(formatter);
         
        String query = "insert into employee (fio,dob,position,phone) values ("+
                    
                                                "'"+checkString(fioField.getText())+
                                                "','"+formattedDate+
                                                "','"+positionCombo.getValue()+
                                                "','"+phoneField.getText()+"')";
        
        executeSave(query);
        AlertHelper.showAlert(AnimationType.POPUP, "Qo'shildi", "", NotificationType.SUCCESS, 2000);
        clearFields();
     }
         
         private boolean checkPhone(){
        return  phoneField.getText().length() == 9 && phoneField.getText().chars().allMatch(x -> Character.isDigit(x));
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
    
     private boolean isValidated(){
        
        if(fioField.getText().equals("")){
            
            AlertHelper.showAlert(AnimationType.POPUP, "Ism, Familiya kiritmadiz", "", NotificationType.WARNING, 2000);
            
            fioField.requestFocus();
        } else if (dateField.getValue() == null) {
             AlertHelper.showAlert(AnimationType.POPUP, "Tugulgan sana kiritmadiz yoki xato", "", NotificationType.WARNING, 2000);

            dateField.requestFocus();
        }else if (positionCombo.getValue() == null) {
             AlertHelper.showAlert(AnimationType.POPUP, "Lavozim tanlamadiz", "", NotificationType.WARNING, 2000);

            positionCombo.requestFocus();
        } else {
            return true;
        }
        return false;
    }
    
    
     public void getPositionList(){
        String query = "select positionname from workposition";
        Statement st;
        ResultSet rs;
       
        try{
            st = con.createStatement();
            rs = st.executeQuery(query);
        while(rs.next()){
            
            positionCombo.getItems().addAll(rs.getString("positionname"));
            
        }
        }catch(SQLException e){
            e.printStackTrace();
            System.out.println("Error in getting data");
        }
    }
     
      private void clearFields() {
        fioField.setText("");
        phoneField.setText("");
        dateField.getEditor().clear();
        positionCombo.setValue(null);
    }

    
}
