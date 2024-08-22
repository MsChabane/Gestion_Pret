package com.app.controls;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import com.app.utis.CategorieDB;
import com.app.utis.EmpruntDB;
import com.app.utis.LecteurDB;
import com.app.utis.OuvrageDB;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class PaneauControl implements Initializable{
	private double x,y;
	private String nbrOuv,nbrlec,nbrEmp,nbrD;
	Map <String ,Integer>dataBar;
	@FXML
    private BarChart<String, Integer> barCat;
	private int nbrRemis,nbrEnAtte;
	XYChart.Series<String, Integer>cheat;
	private ObservableList<PieChart.Data>data;
	@FXML
    private PieChart pieEmp;
	 @FXML
	    private Label lblShow;
	 @FXML
	    private Label lblDlPass;

	    @FXML
	    private Label lblTotEmp;

	    @FXML
	    private Label lblTotLec;

	    @FXML
	    private Label lblTotalOvg;
	@FXML
	void returnBack(ActionEvent event) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/com/app/fxml/Connection.fxml"));
		try {
			loader.load();
			Parent root = loader.getRoot();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.getIcons().add(new Image("/com/app/img/img.png"));
			stage.initStyle(StageStyle.UNDECORATED);
			stage.show();
			((Stage) lblTotalOvg.getScene().getWindow()).hide();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	  @FXML
	    void drag(MouseEvent event) {
		  Stage stage = (Stage) lblTotalOvg .getScene().getWindow();
		  stage.setX(event.getScreenX()-x);
		  stage.setY(event.getScreenY()-y);
	    }

	    @FXML
	    void press(MouseEvent event) {
    	 Stage stage = (Stage) lblTotalOvg .getScene().getWindow();
    	 stage.setOpacity(0.8);
	    	x= event.getX();
	    	y= event.getY();
	    }
	    @FXML
	    void releas(MouseEvent event) {
	    	 Stage stage = (Stage) lblTotalOvg .getScene().getWindow();
	    	 stage.setOpacity(1);
	    }
	    public void loadData(){
			Task<Void>task = new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					
					nbrOuv= String.valueOf(OuvrageDB.count());
					nbrlec = String.valueOf(LecteurDB.count());
					nbrEmp= String.valueOf(EmpruntDB.count());
					nbrD=  String.valueOf(EmpruntDB.countDepDelai());
					nbrRemis=EmpruntDB.countRemis();
					nbrEnAtte=EmpruntDB.countEnAtt();
					dataBar=CategorieDB.getbarData();
					return null;
				}
			};
			task.setOnFailed(e->System.out.println("field"));
			task.setOnSucceeded(e->{
				lblTotalOvg.setText(nbrOuv);
				lblTotEmp.setText(nbrEmp);
				lblTotLec.setText(nbrlec);
				lblDlPass.setText(nbrD);
				dataBar.forEach((k,v)->{
					cheat.getData().add(new XYChart.Data<String,Integer>(k,v));
				});
				barCat.getData().add(cheat);
				data.add(new PieChart.Data("REMIS",nbrRemis));
				if(nbrEnAtte!=0) {
					data.add(new PieChart.Data("EN ATTENTE",nbrEnAtte));
				}
				pieEmp.getData().addAll(data);
				pieEmp.getData().get(0).getNode().setStyle("-fx-background-color:rgb(25,25,255)");
				if(nbrEnAtte!=0) {
					pieEmp.getData().get(1).getNode().setStyle("-fx-background-color:rgb(2,0,52)");
				}
				
				pieEmp.getData().forEach(datas -> datas.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, event->{
					lblShow.setText(datas.getPieValue()+" emprunts");	
				}));
			});
			Thread th = new Thread(task);
			th.setDaemon(true);
			th.start();
		}
		@Override
		public void initialize(URL arg0, ResourceBundle arg1) {
			cheat= new XYChart.Series<String, Integer>();
			barCat.setAnimated(false);
			data =FXCollections.observableArrayList();
			loadData();
			pieEmp.setTitle("Status Emprunt");
			barCat.setTitle("Nombre d'ouvrage dans chaque catégorie");
			
			
			
			
		}
		  @FXML
		    void showEmprunt(ActionEvent event) {
			  FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/com/app/fxml/Emprunt.fxml"));
				try {
					loader.load();
					Parent root = loader.getRoot();
					Stage stage = (Stage) lblDlPass.getScene().getWindow();
					Scene scene = new Scene(root);
					stage.setScene(scene);
					stage.show();
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }

		    @FXML
		    void showLecteur(ActionEvent event) {
		    	FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/com/app/fxml/Lecteur.fxml"));
				try {
					loader.load();
					Parent root = loader.getRoot();
					Stage stage = (Stage) lblDlPass.getScene().getWindow();
					Scene scene = new Scene(root);
					stage.setScene(scene);
					stage.show();
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }

		    @FXML
		    void showOuvrage(ActionEvent event) {
		    	FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/com/app/fxml/Ouvrage.fxml"));
				try {
					loader.load();
					Parent root = loader.getRoot();
					Stage stage = (Stage) lblDlPass.getScene().getWindow();
					Scene scene = new Scene(root);
					stage.setScene(scene);
					stage.show();
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }

		    @FXML
		    void showParametre(ActionEvent event) {
		    	FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/com/app/fxml/Parameter.fxml"));
				try {
					loader.load();
					Parent root = loader.getRoot();
					Stage stage = (Stage) lblDlPass.getScene().getWindow();
					Scene scene = new Scene(root);
					scene.getStylesheets().add(getClass().getResource("/com/app/styling/Notif.css").toExternalForm());
					stage.setScene(scene);
					stage.show();
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }

		    @FXML
		    void showRevenue(ActionEvent event) {
		    	FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/com/app/fxml/Revenue.fxml"));
				try {
					loader.load();
					Parent root = loader.getRoot();
					Stage stage = (Stage) lblDlPass.getScene().getWindow();
					Scene scene = new Scene(root);
					stage.setScene(scene);
					stage.show();
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }
		    @FXML
		    void showSanctionnement(ActionEvent event) {
		    	FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/com/app/fxml/Sanctionner.fxml"));
				try {
					loader.load();
					Parent root = loader.getRoot();
					Stage stage = (Stage) lblDlPass.getScene().getWindow();
					Scene scene = new Scene(root);
					stage.setScene(scene);
					stage.show();
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }

}
