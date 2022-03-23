package controller;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import databaseConnect.DbConnect;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import utils.AlertHelper;


public class UpdateEmployeeController implements Initializable {

    @FXML
    private JFXTextField fioField;
    @FXML
    private JFXTextField phoneField;
    @FXML
    private JFXDatePicker dateChooseField;
    @FXML
    private ComboBox<String> comboPosition;

   
    int employeeId;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void updateClicked(MouseEvent event) {
        if(isValidated()){
            saveRecord();
             Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
             stage.close();
        }
    }

    
    private void saveRecord(){
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = dateChooseField.getValue().format(formatter);
        
              String query = "update employee set fio='"+checkString(fioField.getText())+"',dob='"+formattedDate+"',position='"+comboPosition.getValue()+"',phone='"+phoneField.getText()+"'"
                      + " where id="+employeeId;
        
        executeSave(query);
        AlertHelper.showAlert(AnimationType.POPUP, "O'zgardi", "", NotificationType.SUCCESS, 2000);
         
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
    
     private void executeSave(String query) {
        Statement st;
        try {
            st = con.createStatement();
            st.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

   private final Connection con;
    
    public UpdateEmployeeController(){
        con = DbConnect.getConnection(true);
    } 

    void setTextField(int id, String fio, String dob, String position, String phone){
        employeeId = id;
        fioField.setText(fio);
        phoneField.setText(phone);
        comboPosition.setValue(position);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDate = LocalDate.parse(dob,formatter);
        dateChooseField.setValue(localDate);
        getPositionList();
    }
    
    public void getPositionList(){
        String query = "select positionname from workposition";
        Statement st;
        ResultSet rs;
       
        try{
            st = con.createStatement();
            rs = st.executeQuery(query);
        while(rs.next()){
            
            comboPosition.getItems().addAll(rs.getString("positionname"));
            
        }
        }catch(SQLException e){
            e.printStackTrace();
            System.out.println("Error in getting data");
        }
    }
    
    private boolean isValidated(){
        
        if(fioField.getText().equals("")){
            
            AlertHelper.showAlert(AnimationType.POPUP, "Ism, Familiya kiritmadiz", "", NotificationType.WARNING, 2000);
            
            fioField.requestFocus();
        } else if (dateChooseField.getValue() == null) {
             AlertHelper.showAlert(AnimationType.POPUP, "Tugulgan sana kiritmadiz yoki xato", "", NotificationType.WARNING, 2000);

            dateChooseField.requestFocus();
        }else if (comboPosition.getValue() == null) {
             AlertHelper.showAlert(AnimationType.POPUP, "Lavozim tanlamadiz", "", NotificationType.WARNING, 2000);

            comboPosition.requestFocus();
        } else {
            return true;
        }
        return false;
    }

    
}
