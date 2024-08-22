package com.app.controls;

import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;

import com.app.model.Emprunt;
import com.app.model.Lecteur;
import com.app.model.Ouvrage;
import com.app.utis.EmpruntDB;
import com.app.utis.LecteurDB;
import com.app.utis.OuvrageDB;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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

public class AjouterEmprunt implements Initializable {
	private double x,y;
    @FXML
    private DatePicker DatePretPkr;

    @FXML
    private ComboBox<Lecteur> comboLect;

    @FXML
    private ComboBox<Ouvrage> comboOuvg;

  
    @FXML
    void ajouter(ActionEvent event) {
    	Stage stage = (Stage) ((Stage) comboLect.getScene().getWindow()).getOwner();
    	if(verify(stage)) {
    		try {
    			if(EmpruntDB.canDo(	comboLect.getSelectionModel().getSelectedItem())) {
					Emprunt emprunt = new Emprunt("",new SimpleDateFormat("yyyy-MM-dd").parse(DatePretPkr.getValue().toString())
							, null
							, comboOuvg.getSelectionModel().getSelectedItem(),
							comboLect.getSelectionModel().getSelectedItem(), "EN ATTENTE");
					EmpruntDB.add(emprunt);
					OuvrageDB.doOpeartionOf(true, emprunt.getOuvrage());
					Notifications.create().hideAfter(Duration.seconds(3)).owner(stage)
					.graphic(new ImageView(new Image("/com/app/img/icons8_information_55px.png"))).onAction(e->comboOuvg.requestFocus()).position(Pos.TOP_RIGHT).text("OUVRAGE EST EMPRUNTE AVEC SUCCES").show();
					control.setNew(emprunt);
					ouvgs.setAll(ouvgs.stream().filter(e->e.getNombreExamplaire()>0).toList());
				}else
					Notifications.create().hideAfter(Duration.seconds(3)).onAction(e->comboOuvg.requestFocus()).position(Pos.TOP_RIGHT).text("LECTEUR DEPASSE 3 OUVRAGE NON REMIS ... ").showWarning();

			} catch (ParseException|SQLException e) {
				e.printStackTrace();
			}
    	}
    }
    private boolean verify(Stage stage) {
    	
    	if(comboOuvg.getSelectionModel().getSelectedIndex()<0) {
    		Notifications.create().hideAfter(Duration.seconds(3)).owner(stage).graphic(new ImageView(new Image("/com/app/img/icons8_warning_shield_55px.png"))).onAction(e->comboOuvg.requestFocus()).position(Pos.TOP_RIGHT).text("AUCUNEE OUVRAGE SELECTIONNER").show();
    		return false;
    	}
    	else if(comboLect.getSelectionModel().getSelectedIndex()<0) {
    		Notifications.create().hideAfter(Duration.seconds(3)).owner(stage).graphic(new ImageView(new Image("/com/app/img/icons8_warning_shield_55px.png"))).onAction(e->comboLect.requestFocus()).position(Pos.TOP_RIGHT).text("AUCUNEE LECTEUR SELECTIONNER ").show();
    		return false;
    	} 
    	else if(DatePretPkr.getValue()==null) {
    		Notifications.create().hideAfter(Duration.seconds(3)).owner(stage).graphic(new ImageView(new Image("/com/app/img/icons8_warning_shield_55px.png"))).onAction(e->DatePretPkr.requestFocus()).position(Pos.TOP_RIGHT).text("DATE PRET INVALIDE ").show();
    		return false;
    	}
    	return true;
    }
    private ObservableList<Ouvrage>ouvgs;
    private ObservableList<Lecteur>lecteurs;
    private EmpruntControl control;
    public void setControl(EmpruntControl control) {
    	this.control= control;
    }
    public void loadData(){
		Task<Void>task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				lecteurs.setAll(LecteurDB.getData(false));
				ouvgs.setAll(OuvrageDB.getData(false));
				return null;
			}
		};
		task.setOnFailed(e->System.out.println("field"));
		Thread th = new Thread(task);
		th.setDaemon(true);
		th.start();
	}
    

    @FXML
    void drag(MouseEvent event) {
	  Stage stage = (Stage) comboLect .getScene().getWindow();
	  stage.setX(event.getScreenX()-x);
	  stage.setY(event.getScreenY()-y);
    }

    @FXML
    void press(MouseEvent event) {
	 Stage stage = (Stage) comboLect .getScene().getWindow();
	 stage.setOpacity(0.8);
    	x= event.getX();
    	y= event.getY();
    }
    @FXML
    void releas(MouseEvent event) {
    	 Stage stage = (Stage) comboLect .getScene().getWindow();
    	 stage.setOpacity(1);
    }
    @FXML
    void retournToOwner(ActionEvent event) {
    	Stage s = (Stage) ((Button)event.getSource()).getScene().getWindow();
    	s.getOwner().setOpacity(1);
		s.close();
    }
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ouvgs=FXCollections.observableArrayList();
		lecteurs=FXCollections.observableArrayList();
		loadData();
		comboLect.setItems(lecteurs);
		comboOuvg.setItems(ouvgs);
		
	}
	

}

