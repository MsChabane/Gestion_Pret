package com.app.controls;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import com.app.model.Emprunt;

import com.app.utis.EmpruntDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class RevenueSucces implements Initializable{
	private double x,y;
    @FXML
    private Label lblTitle;
    
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
    			" } au bonne état");
		dateRemis.setValue(LocalDate.parse(emprunt.getDateRmis().toString()));
    }

    @FXML
    private DatePicker dateRemis;

    @FXML
    void returnSucces(ActionEvent event) {
    	try {
    		emprunt.setDateRmis(new SimpleDateFormat("dd-MM-yyyy").parse(dateRemis.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
			EmpruntDB.remis(emprunt);
			control.setUpdate(index);
			returnToOwner(event);
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
	}
}
