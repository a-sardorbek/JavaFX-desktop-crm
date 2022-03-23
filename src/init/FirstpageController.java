
package init;

import com.jfoenix.controls.JFXButton;
import databaseConnect.DbConnect;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.Window;


public class FirstpageController implements Initializable {

    @FXML
    private JFXButton kirishBtn1;
    @FXML
    private ImageView exitIBtn;
    
    Window window;

  
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DbConnect.setPosition();
        DbConnect.setChildren();
        DbConnect.setEmployee();
    } 
    
     Statement st;
    PreparedStatement prs;
     ResultSet rs;
    
    private final Connection con;
    
    public FirstpageController(){
        con =  DbConnect.getConnection(true);
    }
        private double x,y;
    

    @FXML
    private void kirishBtnPress(ActionEvent event) throws IOException {
        if(event.getSource() == kirishBtn1){
             Stage stage = (Stage) kirishBtn1.getScene().getWindow();
                stage.close();

                Parent root = FXMLLoader.load(getClass().getResource("/view/Dashboard.fxml"));
                Scene scene = new Scene(root);
                root.setOnMousePressed((MouseEvent event1) -> {
                x = event1.getSceneX();
                y= event1.getSceneY();
                 });


                root.setOnMouseDragged((MouseEvent event2) -> {
                    stage.setX(event2.getScreenX()-x);
                    stage.setY(event2.getScreenY()-y);
                });
                stage.setScene(scene);
                stage.setTitle("Admin Panel");
                stage.show();
            
                    }
    }

    @FXML
    private void exit(MouseEvent event) {
        DbConnect.getConnection(false);
        Stage stage = (Stage) exitIBtn.getScene().getWindow();
        stage.close();
        System.exit(0);
    }
    
}
