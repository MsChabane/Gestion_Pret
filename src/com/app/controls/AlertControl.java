package com.app.controls;
import java.sql.SQLException;

import com.app.model.Categorie;
import com.app.model.Emprunt;
import com.app.model.Lecteur;
import com.app.model.Ouvrage;
import com.app.model.Sanction;
import com.app.model.Sanctionner;
import com.app.utis.CategorieDB;
import com.app.utis.EmpruntDB;
import com.app.utis.LecteurDB;
import com.app.utis.OuvrageDB;
import com.app.utis.SanctionDB;
import com.app.utis.SanctionnerDB;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class AlertControl {
	private double x,y;
    @FXML
    private Label lblMessage;
    @FXML
    private Button btn;
    

    @FXML
    private Label lblTitle;
    private int index;
    private Object obj,controlObj;
    public void setControl(Object controlObj,Object obj,String title,String message ,int index,boolean isLec) {
		this.controlObj=controlObj;
    	this.obj=obj;
		this.lblMessage.setText(message);
		this.lblTitle.setText(title);
		this.index=index;
		if(isLec)
			btn.setText("Blocker");
	}
    
    @FXML
    void drag(MouseEvent event) {
	  Stage stage = (Stage) lblMessage .getScene().getWindow();
	  stage.setX(event.getScreenX()-x);
	  stage.setY(event.getScreenY()-y);
    }

    @FXML
    void press(MouseEvent event) {
	 Stage stage = (Stage) lblMessage .getScene().getWindow();
	 stage.setOpacity(0.8);
    	x= event.getX();
    	y= event.getY();
    }
    @FXML
    void releas(MouseEvent event) {
    	 Stage stage = (Stage) lblMessage .getScene().getWindow();
    	 stage.setOpacity(1);
    }
    @FXML
    void returnToOwner(ActionEvent event) {
    	Stage s = (Stage) ((Button)event.getSource()).getScene().getWindow();
    	s.getOwner().setOpacity(1);
		s.close();
    }

    @FXML
    void supprimer(ActionEvent event) {
    	if(controlObj instanceof EmpruntControl ) {
    		Emprunt emprunt =(Emprunt) obj;
    		EmpruntControl control = (EmpruntControl) controlObj;
    		try {
				EmpruntDB.delete(emprunt);
				control.delete(index);
			} catch (SQLException e) {
				e.printStackTrace();
			}
    	}else if(controlObj instanceof OuvrageControl) {
    		Ouvrage ouvg =(Ouvrage) obj;
    		OuvrageControl control = (OuvrageControl) controlObj;
    		try {
				OuvrageDB.delete(ouvg);
				control.delete(index);
			} catch (SQLException e) {
				e.printStackTrace();
			}
    	}else if(controlObj instanceof LecteurControl){
    		Lecteur lecteur =(Lecteur) obj;
    		LecteurControl control = (LecteurControl) controlObj;
    		try {
				LecteurDB.delete(lecteur);
				control.delete(index);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	else if(controlObj instanceof Parameter) {
    		Parameter control = (Parameter) controlObj;
    		if(obj instanceof Categorie ) {
    			Categorie cat = (Categorie) obj;
    			try {
					CategorieDB.delete(cat);
					control.delete(index,true);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}else {
    			Sanction san = (Sanction) obj;
    			try {
					SanctionDB.delete(san);
					control.delete(index,false);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	}
    	else if (controlObj instanceof SanctionnerCont ) {
    		SanctionnerCont control =(SanctionnerCont) controlObj;
    		Sanctionner snc = (Sanctionner) obj;
    		try {
				SanctionnerDB.delete(snc);
				control.delete(index);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	returnToOwner(event);
    }

}
