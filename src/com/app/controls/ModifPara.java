package com.app.controls;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.textfield.CustomTextField;

import com.app.model.Categorie;
import com.app.model.Lecteur;
import com.app.model.Sanction;
import com.app.utis.CategorieDB;
import com.app.utis.LecteurDB;
import com.app.utis.SanctionDB;
public class ModifPara  implements Initializable{
	private double x,y;
	
   

    @FXML
    private CustomTextField txtIntit;

   
    private Stage stage ;
    @FXML
    void modifer(ActionEvent event) {
    	stage=	(Stage) (((Stage) txtIntit.getScene().getWindow())).getOwner();
    	if(txtIntit.getText().isBlank()) {
    		Notifications.create().hideAfter(Duration.seconds(3))
    		.owner(stage).graphic(new ImageView(new Image("/com/app/img/icons8_warning_shield_55px.png"))).onAction(e->txtIntit.requestFocus()).position(Pos.TOP_RIGHT).text("INTITULE INVALIDE ").show();
    	}else {
    		if(data instanceof Categorie) {
    			try {
					if(!CategorieDB.alreadyExist(txtIntit.getText())) {
						Categorie cat = (Categorie) data;
						cat.setintitule(txtIntit.getText());
						CategorieDB.modify(cat);
						control.setUpdate(cat,index,true);
						
					}else
						Notifications.create().hideAfter(Duration.seconds(3))
			    		.owner(stage).graphic(new ImageView(new Image("/com/app/img/icons8_warning_shield_55px.png"))).onAction(e->txtIntit.requestFocus()).position(Pos.TOP_RIGHT).text("INTITULE EXISTE DIJA ").show();
			    	
				} catch (SQLException e) {
					
					e.printStackTrace();
				}
    		}
    		else {
    			try {
					if(!SanctionDB.alreadyExist(txtIntit.getText())) {
						Sanction san = (Sanction) data;
						san.setIntitule(txtIntit.getText());
						SanctionDB.modify(san);
						control.setUpdate(san,index,false);
						
					}else
						Notifications.create().hideAfter(Duration.seconds(3))
			    		.owner(stage).graphic(new ImageView(new Image("/com/app/img/icons8_warning_shield_55px.png"))).onAction(e->txtIntit.requestFocus()).position(Pos.TOP_RIGHT).text("INTITULE EXISTE DIJA ").show();
			    	
				} catch (SQLException e) {
					e.printStackTrace();
				}
    		}
    	}
    	retournToOwner(event);
       }
   
    @FXML
    void retournToOwner(ActionEvent event) {
    	Stage s = (Stage) ((Button)event.getSource()).getScene().getWindow();
    	s.getOwner().setOpacity(1);
		s.close();
    }
    private Parameter control;
    private Object data;
    private int index;
    public void setControl(Parameter cont ,Object data,int index) {
    	this.control=cont;
    	this.data=data;
    	this.index=index;
    	txtIntit.setText(data instanceof Categorie ? ((Categorie)data).getIntitule():((Sanction)data).getIntitule());
    }
  
    
	
	 @FXML
	    void drag(MouseEvent event) {
		  Stage stage = (Stage) txtIntit .getScene().getWindow();
		  stage.setX(event.getScreenX()-x);
		  stage.setY(event.getScreenY()-y);
	    }

	    @FXML
	    void press(MouseEvent event) {
			Stage stage = (Stage) txtIntit .getScene().getWindow();
			stage.setOpacity(0.8);
	    	x= event.getX();
	    	y= event.getY();
	    }
	    @FXML
	    void releas(MouseEvent event) {
	    	 Stage stage = (Stage) txtIntit .getScene().getWindow();
	    	 stage.setOpacity(1);
	    }
	   
		@Override
		public void initialize(URL arg0, ResourceBundle arg1) {
			
			
		}

}
