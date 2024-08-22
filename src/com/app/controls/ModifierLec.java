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
import com.app.model.Lecteur;
import com.app.utis.LecteurDB;
public class ModifierLec  implements Initializable{
	private double x,y;
	
    @FXML
    private DatePicker datenaissance;

    @FXML
    private CustomTextField txtNom;

    @FXML
    private CustomTextField txtPrenom;

    @FXML
    void modifer(ActionEvent event) {
    	String nom = txtNom.getText();
    	String prenom = txtPrenom.getText();
    	if(verify(nom, prenom)) {
    		try {
				lecteur.setDateNaissance(new SimpleDateFormat("dd-MM-yyyy").parse(datenaissance.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
				lecteur.setNom(nom);
				lecteur.setPrenom(prenom);
				LecteurDB.modify(lecteur);
				control.setUpdadeItem(lecteur, index);
				retournToOwner(event);
			} catch (ParseException | SQLException e) {
				e.printStackTrace();
			}
    	}
    }
    private Lecteur lecteur;
    private LecteurControl control;
    private int index;
    @FXML
    void retournToOwner(ActionEvent event) {
    	Stage s = (Stage) ((Button)event.getSource()).getScene().getWindow();
    	s.getOwner().setOpacity(1);
		s.close();
    }
    private void setLecteur(Lecteur lecteur) {
    	this.lecteur=lecteur;
    	txtNom.setText(lecteur.getNom());
    	txtPrenom.setText(lecteur.getPrenom());
    	datenaissance.setValue(LocalDate.parse(lecteur.getDateNaissance().toString()));
    }
    private Stage stage;
    private boolean verify(String nom,String prenom) {
    	stage=	(Stage) (((Stage) txtNom.getScene().getWindow())).getOwner();
    	if(datenaissance.getValue()==null) {
    		Notifications.create().hideAfter(Duration.seconds(3))
    		.owner(stage).graphic(new ImageView(new Image("/com/app/img/icons8_warning_shield_55px.png"))).onAction(e->datenaissance.requestFocus()).position(Pos.TOP_RIGHT).text("DATE NAISSANCE INVALIDE ").show();
    		return false;
    	}
    	else if(nom ==null || nom.isBlank()) {
    		Notifications.create().hideAfter(Duration.seconds(3))
    		.owner(stage).graphic(new ImageView(new Image("/com/app/img/icons8_warning_shield_55px.png"))).onAction(e->txtNom.requestFocus()).position(Pos.TOP_RIGHT).text("NOM INVALID !!! ").show();
    		return false;
    	}else if(prenom ==null || prenom.isBlank()) {
    		Notifications.create().hideAfter(Duration.seconds(3))
    		.owner(stage).graphic(new ImageView(new Image("/com/app/img/icons8_warning_shield_55px.png"))).onAction(e->txtPrenom.requestFocus()).position(Pos.TOP_RIGHT).text("PRENOM INVALID !!! ").show();
    		return false;
    	}
    	return true;
    }
	public void setOwnerConrol(LecteurControl lecteurControl, int selectedIndex,Lecteur lecteur) {
		setLecteur(lecteur);
		this.index = selectedIndex;
		this.control=lecteurControl;
	}
	 @FXML
	    void drag(MouseEvent event) {
		  Stage stage = (Stage) txtNom .getScene().getWindow();
		  stage.setX(event.getScreenX()-x);
		  stage.setY(event.getScreenY()-y);
	    }

	    @FXML
	    void press(MouseEvent event) {
	 Stage stage = (Stage) txtNom .getScene().getWindow();
	 stage.setOpacity(0.8);
	    	x= event.getX();
	    	y= event.getY();
	    }
	    @FXML
	    void releas(MouseEvent event) {
	    	 Stage stage = (Stage) txtNom .getScene().getWindow();
	    	 stage.setOpacity(1);
	    }
	    @FXML
	    void test(ActionEvent event) {
	    	if(datenaissance.getValue().isAfter(LocalDate.parse(lecteur.getDateInscrire().toString()))) {
	    		Notifications.create().hideAfter(Duration.seconds(3)).onAction(e->datenaissance.requestFocus()).position(Pos.TOP_RIGHT).text("DATE NAISSANCE EST INNACCEPTABLE ").showWarning();
				datenaissance.setValue(LocalDate.parse(lecteur.getDateNaissance().toString()));
			}
	    }
		@Override
		public void initialize(URL arg0, ResourceBundle arg1) {
			
			
		}

}
