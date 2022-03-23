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
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;


public class DashboardController implements Initializable {

    @FXML
    private BorderPane borderpane;
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

    private void loadUIInside(String ui){
        Parent root = null;
        try{
            root = FXMLLoader.load(getClass().getResource("/view/"+ui+".fxml"));
           
        }catch(IOException e){
            System.out.println("error in loadPAge");
        }
        
        borderpane.setCenter(root);
    }
    
     private void loadUiOutside(String ui){
         FXMLLoader loader = new FXMLLoader ();
         loader.setLocation(getClass().getResource("/view/"+ui+".fxml"));
         try {
            loader.load();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Parent parent = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(parent));
        stage.show();
    }
     
     
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        showEmployees();
    }   
    
    
    
    Employee employee;
    private final Connection con;
    
    public DashboardController(){
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

                        
                         FontAwesomeIconView viewIcon = new FontAwesomeIconView(FontAwesomeIcon.CHILD);
                         FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.EDIT);
                         
                       
                        viewIcon.setStyle(
                                " -fx-cursor: hand ;"
                                + "-glyph-size:28px;"
                                + "-fx-fill:#2D2CB8;"
                        );
                        
                         editIcon.setStyle(
                                " -fx-cursor: hand ;"
                                + "-glyph-size:28px;"
                                + "-fx-fill:#1FB146;"
                        );
                        
                         viewIcon.setOnMouseClicked((MouseEvent event) -> {
                           
                            employee = listViewTable.getSelectionModel().getSelectedItem();
                            FXMLLoader loader = new FXMLLoader ();
                            loader.setLocation(getClass().getResource("/view/AddChildren.fxml"));
                            try {
                                loader.load();
                            } catch (IOException ex) {
                            }
                            
                            AddChildrenController ac = loader.getController();
                             
                            ac.setTextField(employee.getId(), employee.getFio());
                            Parent parent = loader.getRoot();
                            Stage stage = new Stage();
                            stage.setScene(new Scene(parent));
                            stage.show();
                                });
                        
                         
                         editIcon.setOnMouseClicked((MouseEvent event) -> {
                           
                            employee = listViewTable.getSelectionModel().getSelectedItem();
                            FXMLLoader loader = new FXMLLoader ();
                            loader.setLocation(getClass().getResource("/view/UpdateEmployee.fxml"));
                            try {
                                loader.load();
                            } catch (IOException ex) {
                            }
                            
                            UpdateEmployeeController uc = loader.getController();
                             
                             
                             
                            
                            uc.setTextField(employee.getId(),employee.getFio(),employee.getDob(),employee.getPosition(),employee.getPhone());
                            Parent parent = loader.getRoot();
                            Stage stage = new Stage();
                            stage.setScene(new Scene(parent));
                            stage.show();
                                });
                        
                       
                        
                        
                       HBox  managebtn = new HBox(viewIcon,editIcon);
                        managebtn.setStyle("-fx-alignment:center");
                       HBox.setMargin(viewIcon, new Insets(2, 4, 0, 0));
                       HBox.setMargin(editIcon, new Insets(2, 2, 0, 2));
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
    

    @FXML
    private void iconifiedClicked(MouseEvent event) {
         Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
         stage.setIconified(true);
    }

    @FXML
    private void fullClicked(MouseEvent event) {
         Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
         stage.setFullScreen(true);
    }

    @FXML
    private void exitClicked(MouseEvent event) {
        DbConnect.getConnection(false);
         Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
         stage.close();
         System.exit(0);
    }

    @FXML
    private void dashboardPage(MouseEvent event) {
        loadUIInside("Birthday");
    }

    @FXML
    private void qoshishPage(MouseEvent event) {
        loadUIInside("Add");
    }

    @FXML
    private void malumotPage(MouseEvent event) {
        loadUIInside("Employee");
    }


    @FXML
    private void lavozimPage(MouseEvent event) {
        loadUIInside("AddPosition");
    }

    @FXML
    private void deletePAge(MouseEvent event) {
        loadUIInside("EmployeeDelete");
    }

    @FXML
    private void refreshClicked(MouseEvent event) {
        showEmployees();
    }
    
}
