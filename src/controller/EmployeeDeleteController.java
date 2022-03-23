/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.jfoenix.controls.JFXTextField;
import databaseConnect.DbConnect;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import dto.Employee;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
public class EmployeeDeleteController implements Initializable {

    @FXML
    private JFXTextField searcchField;
    @FXML
    private TableView<Employee> listViewTable;
    @FXML
    private TableColumn<Employee, String> fioTable;
    @FXML
    private TableColumn<Employee, String> positionTable;
    @FXML
    private TableColumn<Employee, String> phoneTable;
    @FXML
    private TableColumn<Employee, String> dateTable;
    @FXML
    private TableColumn<Employee, String> actionTable;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showEmployees();
    } 
    
     Employee employee;
    private final Connection con;
    
    public EmployeeDeleteController(){
        con = DbConnect.getConnection(true);
    } 
    
    public ObservableList<Employee> getPatientsList(){
        ObservableList<Employee> employeesList = FXCollections.observableArrayList();
        String query = "select * from employee order by id desc";
        Statement st;
        ResultSet rs;
        
        try{
            st = con.createStatement();
            rs = st.executeQuery(query);
        while(rs.next()){
            
            employee = new Employee(rs.getInt("id"),rs.getString("fio"),rs.getString("position"),rs.getString("dob"),rs.getString("phone"));
            
          employeesList.add(employee);
        }
        }catch(SQLException e){
            e.printStackTrace();
            System.out.println("Error in getting data");
        }
        return employeesList;
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
    
    public void showEmployees(){
        
        
        ObservableList<Employee> list = getPatientsList();
        
        fioTable.setCellValueFactory(new PropertyValueFactory<>("fio"));
        phoneTable.setCellValueFactory(new PropertyValueFactory<>("phone"));
        positionTable.setCellValueFactory(new PropertyValueFactory<>("position"));
        dateTable.setCellValueFactory(new PropertyValueFactory<>("dob"));
        
         Callback<TableColumn<Employee, String>, TableCell<Employee, String>> cellFoctory = (TableColumn<Employee, String> param) -> {
            
            final TableCell<Employee, String> cell = new TableCell<Employee, String>() {
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
                            PreparedStatement prs;
                            try {
                                employee = listViewTable.getSelectionModel().getSelectedItem();
                                String query = "delete from employee where id="+employee.getId();
                                String query1 = "delete from children where idemployee="+employee.getId();
                                 prs= con.prepareStatement(query);
                                 prs.execute();
                                 
                                  prs= con.prepareStatement(query1);
                                   prs.execute();
                                AlertHelper.showAlert(AnimationType.POPUP, "Ma'lumot o'chdi", "", NotificationType.INFORMATION, 2000);
                                showEmployees();
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                            
                        });
                         
                       
                        
                        
                       HBox  managebtn = new HBox(deleteIcon);
                        managebtn.setStyle("-fx-alignment:center");
                       HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 0));
                        setGraphic(managebtn);
                        setText(null);

                    }
                }

            };

            return cell;
        };
         actionTable.setCellFactory(cellFoctory);
       
        
        listViewTable.setItems(list);
        
        FilteredList<Employee> filteredData = new FilteredList<>(list, p -> true);
		
		
		searcchField.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(person -> {
				
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				
				
				String lowerCaseFilter = newValue.toLowerCase();
				
				if (person.getFio().toLowerCase().contains(lowerCaseFilter)) {
					return true; 
				} else if (person.getPosition().toLowerCase().contains(lowerCaseFilter)) {
					return true; 
				} else if (person.getDob().toLowerCase().contains(lowerCaseFilter)) {
					return true; 
				} else if (person.getPhone().toLowerCase().contains(lowerCaseFilter)) {
					return true; 
				}
				return false; 
			});
		});
		
		
		      SortedList<Employee> sortedData = new SortedList<>(filteredData);
		
		
		sortedData.comparatorProperty().bind(listViewTable.comparatorProperty());
		
		
		listViewTable.setItems(sortedData);
        
    }
    
}
