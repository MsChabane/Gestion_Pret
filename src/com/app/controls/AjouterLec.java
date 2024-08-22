package com.app.controls;

import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.geometry.Pos;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import org.controlsfx.control.Notifications;
import org.controlsfx.control.textfield.CustomTextField;

import com.app.model.Lecteur;

import com.app.utis.LecteurDB;

public class AjouterLec implements Initializable {
	private double x,y;
	   @FXML
	    private DatePicker dateInsPicker;
	   private Stage stage;
	    @FXML
	    private DatePicker datenaissance;

	    @FXML
	    private CustomTextField txtNom;

	    @FXML
	    private CustomTextField txtPrenom;

	    @FXML
	    void ajouter(ActionEvent event)   {
	    	String nom = txtNom.getText();
	    	String prenom = txtPrenom.getText();
	    	if(verify(nom, prenom)) {
	    		try {
					Lecteur lecteur = new Lecteur("", nom, prenom,new SimpleDateFormat("dd-MM-yyyy").parse(datenaissance.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
							, new SimpleDateFormat("dd-MM-yyyy").parse(dateInsPicker.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
							);
					LecteurDB.add(lecteur);		
					Notifications.create().hideAfter(Duration.seconds(3)).owner(stage)
					.graphic(new ImageView(new Image("/com/app/img/icons8_information_55px.png"))).position(Pos.TOP_RIGHT).text( "Lecteur ajouter avec succes ").show();
					lecteur.setNombreSection(0);
					controle.setNew(lecteur);
					clean();
				} catch (ParseException | SQLException e) {
					e.printStackTrace();
				}
	    	}
	    }
	    
	    
	    private void clean() {
			txtNom.setText(null);
			txtPrenom.setText(null);
			datenaissance.setValue(null);
		}
		private LecteurControl controle;
	    public void setOwnerControl(LecteurControl control) {
	    	this.controle=control;
	    }
	    @FXML
	    void returnToOwner(ActionEvent event) {
	    	Stage s = (Stage) ((Button)event.getSource()).getScene().getWindow();
	    	s.getOwner().setOpacity(1);
	    	// Stage stage = (Stage) s.getOwner();
	    	// stage.show();
			s.close();
	    }
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}
	
	private boolean verify(String nom ,String prenom){
		stage=	(Stage) (((Stage) txtNom.getScene().getWindow())).getOwner();
		
		if(dateInsPicker.getValue()==null) {
    		Notifications.create().hideAfter(Duration.seconds(3)).owner(stage).graphic(new ImageView(new Image("/com/app/img/icons8_warning_shield_55px.png"))).onAction(e->dateInsPicker.requestFocus()).position(Pos.TOP_RIGHT).text("DATE INSCRIPTION INVALIDE ").show();
    		return false;
    	}
    	else if(nom ==null || nom.isBlank()) {
    		Notifications.create().hideAfter(Duration.seconds(3)).owner(stage).graphic(new ImageView(new Image("/com/app/img/icons8_warning_shield_55px.png"))).onAction(e->txtNom.requestFocus()).position(Pos.TOP_RIGHT).text("NOM INVALID !!! ").show();
    		return false;
    	}else if(prenom ==null || prenom.isBlank()) {
    		Notifications.create().hideAfter(Duration.seconds(3)).owner(stage).graphic(new ImageView(new Image("/com/app/img/icons8_warning_shield_55px.png"))).onAction(e->txtPrenom.requestFocus()).position(Pos.TOP_RIGHT).text("PRENOM INVALID !!! ").show();
    		return false;
    	}else if(datenaissance.getValue()==null) {
    		Notifications.create().hideAfter(Duration.seconds(3)).owner(stage).graphic(new ImageView(new Image("/com/app/img/icons8_warning_shield_55px.png"))).onAction(e->datenaissance.requestFocus()).position(Pos.TOP_RIGHT).text("DATE NAISSANCE INVALIDE ").show();
    		return false;
    	} 
    	else if(dateInsPicker.getValue().compareTo(datenaissance.getValue())<=0) {
    		Notifications.create().hideAfter(Duration.seconds(3)).owner(stage).graphic(new ImageView(new Image("/com/app/img/icons8_warning_shield_55px.png"))).onAction(e->datenaissance.requestFocus()).position(Pos.TOP_RIGHT).text("LES DATE SONT INCOMPATIBLE ").show();
    		return false;
    	}
    	return true;
    	
    }
	
	


	@FXML
	    void drag(MouseEvent event) {
			  Stage stage = (Stage) dateInsPicker .getScene().getWindow();
			  stage.setX(event.getScreenX()-x);
			  stage.setY(event.getScreenY()-y);
	    }

	    @FXML
	    void press(MouseEvent event) {
		 	Stage stage = (Stage) dateInsPicker .getScene().getWindow();
		 	stage.setOpacity(0.8);
	    	x= event.getX();
	    	y= event.getY();
	    }
	    @FXML
	    void releas(MouseEvent event) {
	    	 Stage stage = (Stage) dateInsPicker .getScene().getWindow();
	    	 stage.setOpacity(1);
	    }

}
