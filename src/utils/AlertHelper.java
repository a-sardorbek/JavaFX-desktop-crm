
package utils;

import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;


public class AlertHelper {
   
            
       public static void showAlert(AnimationType atype
                                         ,String title
                                         ,String message
                                         ,NotificationType ntype
                                         ,int time){
                
           TrayNotification tray = new TrayNotification();
            
        
            tray.setAnimationType(atype);
            tray.setTitle(title);
            tray.setMessage(message);
            tray.setNotificationType(ntype);
            tray.showAndDismiss(Duration.millis(time));
       }
            
}
