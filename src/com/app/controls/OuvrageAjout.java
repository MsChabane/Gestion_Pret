package com.app.controls;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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

import java.time.format.DateTimeFormatter;

import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;
import org.controlsfx.control.textfield.CustomTextField;

import com.app.model.Categorie;
import com.app.model.Ouvrage;
import com.app.utis.CategorieDB;
import com.app.utis.OuvrageDB;
public class OuvrageAjout implements Initializable{
	private OuvrageControl controle;
	public void setOwnerControl(OuvrageControl controle) {
		this. controle= controle;
	}
	@FXML
	private ComboBox<Categorie> comboCat;

	@FXML
	private DatePicker dateEntrePiker;

	@FXML
	private CustomTextField txtNbrExp;

	@FXML
	private CustomTextField txtPrix;

	@FXML
	private CustomTextField txtTitre;
	private int nbr;
	private double prix;
	ObservableList<Categorie>cat ;
	private Stage stage;
	@FXML
	void ajouter(ActionEvent event) {
		String titre = txtTitre.getText();
		String nbrEx= txtNbrExp.getText();
		String prixT=txtPrix.getText();
		if(verify(titre, prixT, nbrEx)) {
			try {
				Ouvrage nouveauOuvrage = new Ouvrage("", titre,new SimpleDateFormat("dd-MM-yyyy").parse(dateEntrePiker.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))),
						prix, nbr,comboCat.getSelectionModel().getSelectedItem());
				if(OuvrageDB.isExist(nouveauOuvrage)) {
					Notifications.create().hideAfter(Duration.seconds(3)).owner(stage)
					.graphic(new ImageView(new Image("/com/app/img/icons8_warning_shield_55px.png"))).position(Pos.TOP_RIGHT).text( "Ouvrage dija existe").show();
				}else {
					OuvrageDB.add(nouveauOuvrage);
					Notifications.create().hideAfter(Duration.seconds(6)).owner(stage)
					.graphic(new ImageView(new Image("/com/app/img/icons8_information_55px.png"))).position(Pos.TOP_RIGHT).text( "Ouvrage ajouter avec succes ").show();
					clean();
					controle.setNew(nouveauOuvrage);
				}
			} catch (ParseException |SQLException e) {
				Notifications.create().hideAfter(Duration.seconds(3)).owner(stage).graphic(new ImageView(new Image("/com/app/img/icons8_warning_shield_55px.png"))).position(Pos.TOP_RIGHT).text( "erreur"+e.getMessage()).show();
			}
		}	
	}
	private void clean() {
		txtNbrExp.setText(null);
		txtPrix.setText(null);
		txtTitre.setText(null);
	}
	private boolean verify(String titre ,String prixT,String nbrEx){
		stage=	(Stage) (((Stage) txtNbrExp.getScene().getWindow())).getOwner();
		if(comboCat.getSelectionModel().getSelectedIndex()<0) {
			Notifications.create().hideAfter(Duration.seconds(3)).owner(stage).graphic(new ImageView(new Image("/com/app/img/icons8_warning_shield_55px.png"))).onAction(e->comboCat.requestFocus())
			.position(Pos.TOP_RIGHT).text("AUCUNE CATEGORIE ETE SELECTIONNER").show();
			return false;
		}else
			if(titre ==null || titre.isBlank()) {
				Notifications.create().hideAfter(Duration.seconds(10)).owner(stage).graphic(new ImageView(new Image("/com/app/img/icons8_warning_shield_55px.png"))).onAction(e->txtTitre.requestFocus()).position(Pos.TOP_RIGHT).text("TITRE INVALID !!! ").show();
				return false;
			}else if(prixT==null ||prixT.isBlank() ||!prixT.matches("(\\d+.\\d+)||(\\d+)") ) {
				Notifications.create().hideAfter(Duration.seconds(3)).owner(stage).graphic(new ImageView(new Image("/com/app/img/icons8_warning_shield_55px.png"))).onAction(e->txtPrix.requestFocus()).position(Pos.TOP_RIGHT).text("PRIX INVALID !!! ").show();
				return false;
			}else if(nbrEx==null ||nbrEx.isBlank()|| !nbrEx.matches("\\d+")) {
				Notifications.create().hideAfter(Duration.seconds(3)).owner(stage).graphic(new ImageView(new Image("/com/app/img/icons8_warning_shield_55px.png"))).onAction(e->txtNbrExp.requestFocus()).position(Pos.TOP_RIGHT).text("nombre Examplaire invalide !!! ").show();
				return false;
			}else if(dateEntrePiker.getValue()==null) {
				Notifications.create().hideAfter(Duration.seconds(3)).owner(stage).graphic(new ImageView(new Image("/com/app/img/icons8_warning_shield_55px.png"))).onAction(e->dateEntrePiker.requestFocus()).position(Pos.TOP_RIGHT).text("DATE D'ENTREE INVALIDE ").show();
				return false;
			}
		nbr=Integer.parseInt(nbrEx);
		prix=Double.parseDouble(prixT);
		if(prix<=0) {
			Notifications.create().hideAfter(Duration.seconds(3)).owner(stage).graphic(new ImageView(new Image("/com/app/img/icons8_warning_shield_55px.png"))).onAction(e->txtPrix.requestFocus()).position(Pos.TOP_CENTER).text("PRIX INVALID !!! ").show();
			return false;
		}
		else if(nbr<=0) {
			Notifications.create().hideAfter(Duration.seconds(3)).owner(stage).graphic(new ImageView(new Image("/com/app/img/icons8_warning_shield_55px.png"))).onAction(e->txtNbrExp.requestFocus()).position(Pos.TOP_CENTER).text("nombre Examplaire invalide !!! ").show();
			return false;
		}
		return true;

	}
	public void returnToOwner(ActionEvent event) {
		Stage s = (Stage) ((Button)event.getSource()).getScene().getWindow();
		s.getOwner().setOpacity(1);
		s.close();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			cat=FXCollections.observableArrayList(CategorieDB.getData());
			comboCat.setItems(cat);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	@FXML
	void drag(MouseEvent event) {
		Stage stage = (Stage) txtNbrExp .getScene().getWindow();
		stage.setX(event.getScreenX()-x);
		stage.setY(event.getScreenY()-y);
	}

	@FXML
	void press(MouseEvent event) {
		Stage stage = (Stage) txtNbrExp .getScene().getWindow();
		stage.setOpacity(0.8);
		x= event.getX();
		y= event.getY();
	}
	private double x,y;
	@FXML
	void releas(MouseEvent event) {
		Stage stage = (Stage) txtNbrExp .getScene().getWindow();
		stage.setOpacity(1);
	}
}
