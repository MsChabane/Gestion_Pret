package com.app.controls;

import java.net.URL;
import java.sql.SQLException;

import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.textfield.CustomTextField;
import com.app.model.Categorie;
import com.app.model.Ouvrage;
import com.app.utis.CategorieDB;
import com.app.utis.OuvrageDB;


public class OuvrageModifier implements Initializable{
	private double  x,y;

	@FXML
	private ComboBox<Categorie> comboCat;

	@FXML
	private CustomTextField txtNbrEx;

	@FXML
	private CustomTextField txtPrix;
	

	@FXML
	private CustomTextField txtTitre;
	private ObservableList<Categorie>cat ;
	@FXML
	void modifier(ActionEvent event) {
		String titre = txtTitre.getText();
		String nbrEx= txtNbrEx.getText();
		String prixT=txtPrix.getText();
		if(verify(titre, prixT, nbrEx)) {
			try {
				
					ouv.setTitre(titre);
					if(comboCat.getSelectionModel().getSelectedIndex()!=-1)
						ouv.setCategorie(comboCat.getSelectionModel().getSelectedItem());
					ouv.setNombreExamplaire(nbr);
					ouv.setPrix(prix);
					OuvrageDB.modify(ouv);
					control.setUpdadeItem(ouv, index);
				returnToOwner(event);
				
			} catch ( SQLException|SecurityException e) {
				Notifications.create().hideAfter(Duration.seconds(3)).owner(stage).graphic(new ImageView(new Image("/com/app/img/icons8_warning_shield_55px.png"))).position(Pos.TOP_RIGHT).text( "erreur"+e.getMessage()).show();
			}
		}
	}
	private Ouvrage  ouv;
	private int nbr;
	private double prix;
	public void setOuvrage(Ouvrage ouv) {
		this.ouv=ouv;
		txtNbrEx.setText(String.valueOf(ouv.getNombreExamplaire()));
		txtPrix.setText(String.valueOf(ouv.getPrix()));
		txtTitre.setText(ouv.getTitre());
		comboCat.getSelectionModel().select(ouv.getCategorie());	
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			cat=FXCollections.observableArrayList(CategorieDB.getData());
			comboCat.setItems(cat);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	private OuvrageControl control;
	private int index;
	public void setOwnerConrol(OuvrageControl cont,int index){
		this.control=cont;
		this.index =index;
	}
	private Stage stage;
	private boolean verify(String titre ,String prixT,String nbrEx){
		stage=	(Stage) (((Stage) txtNbrEx.getScene().getWindow())).getOwner();
		if(titre ==null || titre.isBlank()) {
			Notifications.create().hideAfter(Duration.seconds(3))
			.owner(stage).graphic(new ImageView(new Image("/com/app/img/icons8_warning_shield_55px.png"))).onAction(e->txtTitre.requestFocus()).position(Pos.TOP_RIGHT).text("TITRE INVALID !!! ").show();
			return false;
		}else if(prixT==null ||prixT.isBlank() ||!prixT.matches("(\\d+.\\d+)||(\\d+)") ) {
			Notifications.create().hideAfter(Duration.seconds(3))
			.owner(stage).graphic(new ImageView(new Image("/com/app/img/icons8_warning_shield_55px.png"))).onAction(e->txtPrix.requestFocus()).position(Pos.TOP_RIGHT).text("PRIX INVALID !!! ").show();
			return false;
		}else if(nbrEx==null ||nbrEx.isBlank()|| !nbrEx.matches("\\d+")) {
			Notifications.create().hideAfter(Duration.seconds(3))
			.owner(stage).graphic(new ImageView(new Image("/com/app/img/icons8_warning_shield_55px.png"))).onAction(e->txtNbrEx.requestFocus()).position(Pos.TOP_RIGHT).text("nombre Examplaire invalide !!! ").show();
			return false;
		}
		nbr=Integer.parseInt(nbrEx);
		prix=Double.parseDouble(prixT);
		if(prix<=0) {
			Notifications.create().hideAfter(Duration.seconds(3))
			.owner(stage).graphic(new ImageView(new Image("/com/app/img/icons8_warning_shield_55px.png"))).onAction(e->txtPrix.requestFocus()).position(Pos.TOP_RIGHT).text("PRIX INVALID !!! ").show();
			return false;
		}
		else if(nbr<=0) {
			Notifications.create().hideAfter(Duration.seconds(3))
			.owner(stage).graphic(new ImageView(new Image("/com/app/img/icons8_warning_shield_55px.png"))).onAction(e->txtNbrEx.requestFocus()).position(Pos.TOP_RIGHT).text("nombre Examplaire invalide !!! ").show();
			return false;
		}
		return true;

	}
	public void returnToOwner(ActionEvent event) {
		Stage s = (Stage) ((Button)event.getSource()).getScene().getWindow();
		s.getOwner().setOpacity(1);
		s.close();
	}

	@FXML
	void drag(MouseEvent event) {
		Stage stage = (Stage) txtNbrEx .getScene().getWindow();
		stage.setX(event.getScreenX()-x);
		stage.setY(event.getScreenY()-y);
	}

	@FXML
	void press(MouseEvent event) {
		Stage stage = (Stage) txtNbrEx .getScene().getWindow();
		stage.setOpacity(0.8);
		x= event.getX();
		y= event.getY();
	}
	@FXML
	void releas(MouseEvent event) {
		Stage stage = (Stage) txtNbrEx .getScene().getWindow();
		stage.setOpacity(1);
	}
}
