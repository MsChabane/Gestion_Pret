package com.app.controls;

import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;

import com.app.model.Emprunt;
import com.app.model.Lecteur;

import com.app.utis.EmpruntDB;
import com.app.utis.LecteurDB;


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

public class ModifierEmprunt implements Initializable {
	private double x,y;
    @FXML
    private ComboBox<Lecteur> comboLect;
    @FXML
    private DatePicker dateremisPkr;
    @FXML
    void modifier(ActionEvent event) {
    	if(verify()) {
    		try {
    			if (comboLect.getSelectionModel().getSelectedIndex()>=0)
    				emprunt.setLecteur(comboLect.getSelectionModel().getSelectedItem());
    			emprunt.setDateRmis(new SimpleDateFormat("yyyy-MM-dd").parse(dateremisPkr.getValue().toString()));
				EmpruntDB.modify(emprunt);
				control.setUpdate(emprunt, index);
				retournToOwner(event);
			} catch (ParseException|SQLException e) {
				
				e.printStackTrace();
			}	
    	}
    }
    private Stage stage;
    private boolean verify() {
    	stage=	(Stage) (((Stage) comboLect.getScene().getWindow())).getOwner();
    	 if(dateremisPkr.getValue()==null) {
    		Notifications.create().hideAfter(Duration.seconds(3))
    		.owner(stage).graphic(new ImageView(new Image("/com/app/img/icons8_warning_shield_55px.png"))).onAction(e->dateremisPkr.requestFocus()).position(Pos.TOP_RIGHT).text("DATE REMIS INVALIDE ").show();
    		return false;
    	}
    	return true;
    }
    private ObservableList<Lecteur>lecteurs;
    private Emprunt emprunt;
    private int index;
    private EmpruntControl control;
    public void setControl(EmpruntControl control,Emprunt emprunt,int index) {
    	this.control= control;
    	this .emprunt=emprunt;
    	this.index=index;
    	dateremisPkr.setValue(LocalDate.parse(emprunt.getDateRmis().toString()));
    }
    public void loadData(){
		Task<Void>task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				lecteurs.setAll(LecteurDB.getData(false));
				return null;
			}
		};
		task.setOnFailed(e->System.out.println("field"));
		task.setOnSucceeded(e->comboLect.getSelectionModel().select(emprunt.getLecteur()));
		Thread th = new Thread(task);
		th.setDaemon(true);
		th.start();
	}
    

    @FXML
    void drag(MouseEvent event) {
	  Stage stage = (Stage) dateremisPkr .getScene().getWindow();
	  stage.setX(event.getScreenX()-x);
	  stage.setY(event.getScreenY()-y);
    }

    @FXML
    void press(MouseEvent event) {
	 Stage stage = (Stage) dateremisPkr .getScene().getWindow();
	 stage.setOpacity(0.8);
    	x= event.getX();
    	y= event.getY();
    }
    @FXML
    void releas(MouseEvent event) {
    	 Stage stage = (Stage) dateremisPkr .getScene().getWindow();
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
		lecteurs=FXCollections.observableArrayList();
		loadData();
		comboLect.setItems(lecteurs);
		dateremisPkr.setOnAction(e->{
			if(dateremisPkr.getValue().isBefore(LocalDate.parse(emprunt.getDatePret().toString()))) {
				dateremisPkr.setValue(LocalDate.parse(emprunt.getDatePret().toString()));
			}
		});
	}
}

