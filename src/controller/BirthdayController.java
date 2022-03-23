
package controller;

import com.jfoenix.controls.JFXDatePicker;
import databaseConnect.DbConnect;
import dto.Children;
import dto.Employee;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;


public class BirthdayController implements Initializable {

    @FXML
    private TableView<Employee> listParentTable;
    @FXML
    private TableColumn<Employee, String> fioParentTable;
    @FXML
    private TableColumn<Employee, String> dateParentTable;
    @FXML
    private TableView<Children> listChildTable;
    @FXML
    private TableColumn<Children, String> fioChildTable;
    @FXML
    private TableColumn<Children, String> dateChildTable;
    @FXML
    private Label fioLable;
    @FXML
    private Label lavozimLable;
    @FXML
    private Label telefonLable;
    @FXML
    private Label sanaLable;
    @FXML
    private JFXDatePicker dateChooseFIeld;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
     Employee employee;
     Children children;
    private final Connection con;
    
    public BirthdayController(){
        con = DbConnect.getConnection(true);
    } 
    
    private boolean validateEmployee(String employeeDate) throws ParseException{
        Date date1=new SimpleDateFormat("dd-MM-yyyy").parse(employeeDate);
        Date date2 = new Date();
        return date1.getMonth()==date2.getMonth() && date1.getDate()==date2.getDate();
    }
    
     public ObservableList<Employee> getEmployeeList() throws ParseException{
        ObservableList<Employee> employeesList = FXCollections.observableArrayList();
        String query = "select * from employee";
        Statement st;
        ResultSet rs;
        
        
        try{
            st = con.createStatement();
            rs = st.executeQuery(query);
        while(rs.next()){
            
            employee = new Employee(rs.getInt("id"),rs.getString("fio"),rs.getString("position"),rs.getString("dob"),rs.getString("phone"));
            if(!isValidated()){
               if(validateEmployee(employee.getDob())){
               employeesList.add(employee);
            } 
            }else{
               if(validateEmployeeSorted(employee.getDob())){
               employeesList.add(employee);
            } 
            }
           
        }
        }catch(SQLException e){
            e.printStackTrace();
            System.out.println("Error in getting data");
        }
        return employeesList;
    }
   
    
    public void showEmployees() throws ParseException{
        
        ObservableList<Employee> list = getEmployeeList();
        
        fioParentTable.setCellValueFactory(new PropertyValueFactory<>("fio"));
        dateParentTable.setCellValueFactory(new PropertyValueFactory<>("dob"));
        
        listParentTable.setItems(list);
        
    }
    
    
     private boolean validateChildren(String childrenDate) throws ParseException{
        Date date1=new SimpleDateFormat("dd-MM-yyyy").parse(childrenDate);
        Date date2 = new Date();
        return date1.getMonth()==date2.getMonth() && date1.getDate()==date2.getDate();
    }
    
     public ObservableList<Children> getChildrenList() throws ParseException{
        ObservableList<Children> childrenList = FXCollections.observableArrayList();
        String query = "select * from children";
        Statement st;
        ResultSet rs;
        
        
        try{
            st = con.createStatement();
            rs = st.executeQuery(query);
        while(rs.next()){
            
            children = new Children(rs.getInt("id"), rs.getInt("idemployee"), rs.getString("fio"), rs.getString("dob"));
            
            
           if(!isValidated()){
               if(validateChildren(children.getDob())){
               childrenList.add(children);
            } 
            }else{
               if(validateChildrenSorted(children.getDob())){
               childrenList.add(children);
            } 
            }
        }
        }catch(SQLException e){
            e.printStackTrace();
            System.out.println("Error in getting data");
        }
        return childrenList;
    }
   
    
    public void showChildren() throws ParseException{
        
        ObservableList<Children> list = getChildrenList();
        
        fioChildTable.setCellValueFactory(new PropertyValueFactory<>("fio"));
        dateChildTable.setCellValueFactory(new PropertyValueFactory<>("dob"));
        
        listChildTable.setItems(list);
        
    }

    @FXML
    private void getParentSelected(MouseEvent event) {
        employee = listParentTable.getSelectionModel().getSelectedItem();
        if(employee!=null){
        fioLable.setText(employee.getFio());
        lavozimLable.setText(employee.getPosition());
        telefonLable.setText(employee.getPhone());
        sanaLable.setText(employee.getDob());
        }
    }

    @FXML
    private void getChildSelected(MouseEvent event) {
        
        children = listChildTable.getSelectionModel().getSelectedItem();
        if(children!=null){
        employee = getParent(children.getEmployeeId());
        fioLable.setText(employee.getFio());
        lavozimLable.setText(employee.getPosition());
        telefonLable.setText(employee.getPhone());
        sanaLable.setText(employee.getDob());
        }
        
    }
    
    
     public Employee getParent(int id){
        Statement st;
        ResultSet rs;
        String query;
            try {
                 st = con.createStatement();
                 query = "select * from employee where id="+id;
                 rs = st.executeQuery(query);
                 rs.next();
                  employee =new Employee(rs.getInt("id"),rs.getString("fio"),rs.getString("position"),rs.getString("dob"),rs.getString("phone"));
            } catch (SQLException ex) {
                ex.printStackTrace();
             }
            return employee;
    }

    @FXML
    private void qidirishClicked(MouseEvent event) {
          
            try {
            showChildren();
            showEmployees();
        } catch (ParseException ex) {
            Logger.getLogger(BirthdayController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void todayClicked(MouseEvent event) {
        dateChooseFIeld.setValue(null);
        try {
            showChildren();
            showEmployees();
        } catch (ParseException ex) {
            Logger.getLogger(BirthdayController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

     private boolean isValidated(){
        
        if(dateChooseFIeld.getValue()==null){
            dateChooseFIeld.requestFocus();
        }else {
            return true;
        }
        return false;
    }
     
      private boolean validateEmployeeSorted(String employeeDate) throws ParseException{
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = dateChooseFIeld.getValue().format(formatter);
        Date dateChoosen = new SimpleDateFormat("dd-MM-yyyy").parse(formattedDate);
        Date date1=new SimpleDateFormat("dd-MM-yyyy").parse(employeeDate);
        return date1.getMonth()==dateChoosen.getMonth() && date1.getDate()==dateChoosen.getDate();
    }
      
      private boolean validateChildrenSorted(String childrenDate) throws ParseException{
         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = dateChooseFIeld.getValue().format(formatter);
        Date dateChoosen = new SimpleDateFormat("dd-MM-yyyy").parse(formattedDate);
        Date date1=new SimpleDateFormat("dd-MM-yyyy").parse(childrenDate);
        return date1.getMonth()==dateChoosen.getMonth() && date1.getDate()==dateChoosen.getDate();
    }
      
      
      

    @FXML
    private void tomorrowClicked(MouseEvent event) {
        dateChooseFIeld.setValue(null);
        try {
            showChildrenToomorrow();
            showEmployeesToomorrow();
        } catch (ParseException ex) {
            Logger.getLogger(BirthdayController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
      
    
        public void showChildrenToomorrow() throws ParseException{
        
        ObservableList<Children> list = getChildrenListToomorrow();
        
        fioChildTable.setCellValueFactory(new PropertyValueFactory<>("fio"));
        dateChildTable.setCellValueFactory(new PropertyValueFactory<>("dob"));
        
        listChildTable.setItems(list);
        
    }
        
          public void showEmployeesToomorrow() throws ParseException{
        
        ObservableList<Employee> list = getEmployeeListToomorrow();
        
        fioParentTable.setCellValueFactory(new PropertyValueFactory<>("fio"));
        dateParentTable.setCellValueFactory(new PropertyValueFactory<>("dob"));
        
        listParentTable.setItems(list);
        
    }
          
           public ObservableList<Children> getChildrenListToomorrow() throws ParseException{
        ObservableList<Children> childrenList = FXCollections.observableArrayList();
        String query = "select * from children";
        Statement st;
        ResultSet rs;
        
        
        try{
            st = con.createStatement();
            rs = st.executeQuery(query);
        while(rs.next()){
            
            children = new Children(rs.getInt("id"), rs.getInt("idemployee"), rs.getString("fio"), rs.getString("dob"));
            
            
           if(!isValidated()){
               if(validateChildrenToomorrow(children.getDob())){
               childrenList.add(children);
            } 
            }else{
               if(validateChildrenSorted(children.getDob())){
               childrenList.add(children);
            } 
            }
        }
        }catch(SQLException e){
            e.printStackTrace();
            System.out.println("Error in getting data");
        }
        return childrenList;
    }
           
           
            public ObservableList<Employee> getEmployeeListToomorrow() throws ParseException{
        ObservableList<Employee> employeesList = FXCollections.observableArrayList();
        String query = "select * from employee";
        Statement st;
        ResultSet rs;
        
        
        try{
            st = con.createStatement();
            rs = st.executeQuery(query);
        while(rs.next()){
            
            employee = new Employee(rs.getInt("id"),rs.getString("fio"),rs.getString("position"),rs.getString("dob"),rs.getString("phone"));
            if(!isValidated()){
               if(validateEmployeeToomorrow(employee.getDob())){
               employeesList.add(employee);
            } 
            }else{
               if(validateEmployeeSorted(employee.getDob())){
               employeesList.add(employee);
            } 
            }
           
        }
        }catch(SQLException e){
            e.printStackTrace();
            System.out.println("Error in getting data");
        }
        return employeesList;
    }
    
            
            private boolean validateChildrenToomorrow(String childrenDate) throws ParseException{
        Date date1=new SimpleDateFormat("dd-MM-yyyy").parse(childrenDate);
         Date dt = new Date();
        Date tomorrow = new Date(dt.getTime() + (1000 * 60 * 60 * 24));
        return date1.getMonth()==tomorrow.getMonth() && date1.getDate()==tomorrow.getDate();
    }
            
             private boolean validateEmployeeToomorrow(String employeeDate) throws ParseException{
        Date date1=new SimpleDateFormat("dd-MM-yyyy").parse(employeeDate);
         Date dt = new Date();
       Date tomorrow = new Date(dt.getTime() + (1000 * 60 * 60 * 24));
   
        return date1.getMonth()==tomorrow.getMonth() && date1.getDate()==tomorrow.getDate();
    }
}
