package com.app.controls;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;

import com.app.model.Emprunt;
import com.app.model.Sanction;
import com.app.model.Sanctionner;
import com.app.utis.EmpruntDB;
import com.app.utis.SanctionDB;
import com.app.utis.SanctionnerDB;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

public class RevenueEchec implements Initializable{
	private double x,y;
    @FXML
    private Label lblTitle;
    @FXML
    private ComboBox<Sanction> comboSec;
    @FXML
    private DatePicker dateRemis;
    
    @FXML
    void drag(MouseEvent event) {
	  Stage stage = (Stage) lblTitle .getScene().getWindow();
	  stage.setX(event.getScreenX()-x);
	  stage.setY(event.getScreenY()-y);
    }

    @FXML
    void press(MouseEvent event) {
	 Stage stage = (Stage) lblTitle .getScene().getWindow();
	 stage.setOpacity(0.8);
    	x= event.getX();
    	y= event.getY();
    }
    @FXML
    void releas(MouseEvent event) {
    	 Stage stage = (Stage) lblTitle .getScene().getWindow();
    	 stage.setOpacity(1);
    }
    @FXML
    void returnToOwner(ActionEvent event) {
    	Stage s = (Stage) ((Button)event.getSource()).getScene().getWindow();
    	s.getOwner().setOpacity(1);
		s.close();
    }
    private RevenueControl control ;
    private int index;
    private Emprunt emprunt;
    public void setControl(RevenueControl control,Emprunt emprunt,int index) {
    	this .control=control;
    	this.index=index;
    	this.emprunt=emprunt;   
    	lblTitle.setText("Lecteur : ("+emprunt.getLecteur().getNom().concat(" "+emprunt.getLecteur().getPrenom())+") est remis l'ouvrage :("+emprunt.getOuvrage().getTitre() +
    			" ) ");
		dateRemis.setValue(LocalDate.parse(emprunt.getDateRmis().toString()));
    }

    

    @FXML
    void returnEchec(ActionEvent event) {
    	try {
    		Stage stage =(Stage) comboSec.getScene().getWindow();
    		if(comboSec.getSelectionModel().getSelectedIndex()<0) {
    			Notifications.create().hideAfter(Duration.seconds(3)).owner(stage).graphic(new ImageView(new Image("/com/app/img/icons8_warning_shield_55px.png"))).onAction(e->comboSec.requestFocus()).position(Pos.TOP_RIGHT).text("Selectioner Sanction de Lecteur").show();
	    		return ;
    		}else {
    			emprunt.setDateRmis(new SimpleDateFormat("dd-MM-yyyy").parse(dateRemis.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
    			SanctionnerDB.add(new Sanctionner(0, emprunt.getDateRmis(),emprunt.getOuvrage(), emprunt.getLecteur(), comboSec.getSelectionModel().getSelectedItem()));
				EmpruntDB.remis(emprunt);
				control.setUpdate(index);
				returnToOwner(event);
			}
		} catch (SQLException | ParseException e) {
			e.printStackTrace();
		}
    }
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		dateRemis.setOnAction(e->{
			if(dateRemis.getValue().isBefore(LocalDate.parse(emprunt.getDatePret().toString()))) {
				dateRemis.setValue(LocalDate.parse(emprunt.getDatePret().toString()));
			}
		});
		try {
			comboSec.setItems(FXCollections.observableArrayList(SanctionDB.getData()));
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
}
