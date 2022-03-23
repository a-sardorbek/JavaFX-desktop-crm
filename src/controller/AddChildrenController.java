
package controller;

import com.jfoenix.controls.JFXDatePicker;
import databaseConnect.DbConnect;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import dto.Children;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
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


public class AddChildrenController implements Initializable {

    @FXML
    private Label employeeFiolabel;
    @FXML
    private TextField fioField;
    @FXML
    private JFXDatePicker dateField;
    @FXML
    private TableView<Children> listViewTable;
    @FXML
    private TableColumn<Children, String> fioTable;
    @FXML
    private TableColumn<Children, String> dobTable;
    @FXML
    private TableColumn<Children, String> actionTable;

    
    int employeeId;
    int idchange;
    Children children;
    
    private final Connection con;
    
    public AddChildrenController(){
        con = DbConnect.getConnection(true);
    }  
   
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showChildren();
    }    

    @FXML
    private void addClicked(MouseEvent event) {
        if(isValidated()){
            saveRecord();
            clearFields();
            showChildren();
        }
    }


    void setTextField(int id, String fio) {
        employeeId = id;
        employeeFiolabel.setText(fio);
        showChildren();
    }
    
     private boolean isValidated(){
        
        if(fioField.getText().equals("")){
            
            AlertHelper.showAlert(AnimationType.POPUP, "Ism, familiya kiritmadiz", "", NotificationType.ERROR, 2000);
            
            fioField.requestFocus();
        } else if(dateField.getValue()==null){
            
            AlertHelper.showAlert(AnimationType.POPUP, "Tugulgan sana kiritmadiz yoki xato", "", NotificationType.ERROR, 2000);
            
            dateField.requestFocus();
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
          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = dateField.getValue().format(formatter);
        
              String query = "insert into children (fio,dob,idemployee) values ("+
                    
                                                "'"+checkString(fioField.getText())+
                                                "','"+formattedDate+
                                                "',"+employeeId+
                                                ")";
        
        executeSave(query);
        AlertHelper.showAlert(AnimationType.SLIDE, "Qo'shildi", "", NotificationType.SUCCESS, 2000);
         
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
      
       public ObservableList<Children> getChildrenList(){
        ObservableList<Children> childrenList = FXCollections.observableArrayList();
        String query = "select * from children where idemployee="+employeeId;
        Statement st;
        ResultSet rs;
        
          
          
        try{
            st = con.createStatement();
            rs = st.executeQuery(query);
        while(rs.next()){
            
            
           children = new Children(rs.getInt("id"),rs.getString("fio"),rs.getString("dob"));
            childrenList.add(children);
        }
        }catch(SQLException e){
            e.printStackTrace();
            System.out.println("Error in getting data");
        }
        return childrenList;
    }
    
     public void showChildren(){
      
        ObservableList<Children> list = getChildrenList();
        
        fioTable.setCellValueFactory(new PropertyValueFactory<>("fio"));
        dobTable.setCellValueFactory(new PropertyValueFactory<>("dob"));
       
         Callback<TableColumn<Children, String>, TableCell<Children, String>> cellFoctory = (TableColumn<Children, String> param) -> {
            
            final TableCell<Children, String> cell = new TableCell<Children, String>() {
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
                                children = listViewTable.getSelectionModel().getSelectedItem();
                                String query = "delete from children where id="+children.getId();
                                PreparedStatement prs = con.prepareStatement(query);
                                prs.execute();
                                AlertHelper.showAlert(AnimationType.POPUP, "Ma'lumot o'chdi", "", NotificationType.INFORMATION, 2000);
                                showChildren();
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
        fioField.setText("");
        dateField.getEditor().clear();
    }
    
}
