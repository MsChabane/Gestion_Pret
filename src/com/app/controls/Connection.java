package com.app.controls;

import java.io.IOException;

import org.controlsfx.control.textfield.CustomPasswordField;
import org.controlsfx.control.textfield.CustomTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Connection {
	private double x,y;
	  @FXML
	    private CustomPasswordField txtModePas;

	    @FXML
	    private CustomTextField txtUsetName;

	    @FXML
	    void close(ActionEvent event) {
	    	Stage s = (Stage) ((Button)event.getSource()).getScene().getWindow();
			s.close();
	    }

	    @FXML
	    void connect(ActionEvent event) {
	    	if(txtUsetName.getText().equals("admin") && txtModePas.getText().equals("admin")) {
	    		FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/com/app/fxml/PanneauControl.fxml"));
				try {
					loader.load();
					Parent root = loader.getRoot();
					Stage stage = new Stage();
					Scene scene = new Scene(root);
					stage.setScene(scene);
					stage.getIcons().add(new Image("/com/app/img/img.png"));
					stage.initStyle(StageStyle.UNDECORATED);
					stage.show();
					((Stage) txtModePas.getScene().getWindow()).hide();
				} catch (IOException e) {
					e.printStackTrace();
				}
	    	}
	    }
    @FXML
    void drag(MouseEvent event) {
	 Stage stage = (Stage) txtModePas .getScene().getWindow();
	  stage.setX(event.getScreenX()-x);
	  stage.setY(event.getScreenY()-y);
    }

    @FXML
    void press(MouseEvent event) {
	 Stage stage = (Stage) txtModePas .getScene().getWindow();
	 stage.setOpacity(0.8);
    	x= event.getX();
    	y= event.getY();
    }
    @FXML
    void releas(MouseEvent event) {
    	 Stage stage = (Stage) txtModePas .getScene().getWindow();
    	 stage.setOpacity(1);
    }
   
}
