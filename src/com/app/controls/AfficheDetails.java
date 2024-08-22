package com.app.controls;

import com.app.model.Emprunt;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class AfficheDetails {
	private double x,y;
    @FXML
    private Label lblDatePret;

    @FXML
    private Label lblDateRemis;

    @FXML
    private Label lblIdLec;

    @FXML
    private Label lblIdOuv;

    @FXML
    private Label lblNom;

    @FXML
    private Label lblPrenom;

    @FXML
    private Label lblStatus;

    @FXML
    private Label lblTitre;

    @FXML
    private Label lblidEmp;

    @FXML
    void drag(MouseEvent event) {
    	Stage stage = (Stage) lblDatePret .getScene().getWindow();
		  stage.setX(event.getScreenX()-x);
		  stage.setY(event.getScreenY()-y);
    }

    @FXML
    void press(MouseEvent event) {
   	 Stage stage = (Stage) lblDatePret .getScene().getWindow();
 	 stage.setOpacity(0.8);
	    	x= event.getX();
	    	y= event.getY();
    }

    @FXML
    void releas(MouseEvent event) {
    	Stage stage = (Stage) lblDatePret .getScene().getWindow();
   	 stage.setOpacity(1);
    }

    @FXML
    void retournToOwner(ActionEvent event) {
    	Stage s = (Stage) ((Button)event.getSource()).getScene().getWindow();
    	s.getOwner().setOpacity(1);
		s.close();
    }
    public void set(Emprunt emprunt) {
    	lblidEmp.setText(emprunt.getId());
    	lblDatePret.setText(emprunt.getDatePret().toString());
    	lblDateRemis.setText(emprunt.getDateRmis().toString());
    	lblStatus.setText(emprunt.getEtat());
    	lblIdOuv.setText(emprunt.getOuvrage().getId());
    	lblTitre.setText(emprunt.getOuvrage().getTitre());
    	lblIdLec.setText(emprunt.getLecteur().getId());
    	lblNom.setText(emprunt.getLecteur().getNom());
    	lblPrenom.setText(emprunt.getLecteur().getPrenom());
    }

}

