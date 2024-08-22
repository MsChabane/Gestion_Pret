package com.app.controls;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import javafx.scene.layout.StackPane;

import javafx.stage.Stage;

import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;


import org.controlsfx.control.textfield.CustomTextField;


import com.app.model.Lecteur;

import com.app.utis.LecteurDB;

public class LecteurCorbailleControl  implements Initializable{
	private double x,y;
	  @FXML
	    private TableColumn<Lecteur, Date> dateInsCol;

	    @FXML
	    private TableColumn<Lecteur, Date> dateNaCol;

	    @FXML
	    private TableColumn<Lecteur, String> idcol;

	    @FXML
	    private TableColumn<Lecteur, String> nomCol;

	    @FXML
	    private TableColumn<Lecteur, String> prenomCol;

	    @FXML
	    private TableColumn<Lecteur, Integer> sanCol;

	    @FXML
	    private TableView<Lecteur> table;
	    @FXML
	    private TableColumn<Lecteur, String> editCol;
	    @FXML
	    private CustomTextField txtRecherche;
	    
	   
	    private ObservableList<Lecteur>lecteurs;
	    public void loadData(){
			Task<Void>task = new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					
					lecteurs.setAll(LecteurDB.getData(true)) ;
					table.setItems(lecteurs);
					return null;
				}
			};
			task.setOnFailed(e->System.out.println("field"));
			Thread th = new Thread(task);
			th.setDaemon(true);
			th.start();
		}
		@Override
		public void initialize(URL arg0, ResourceBundle arg1) {
			lecteurs=FXCollections.observableArrayList();
			idcol.setCellValueFactory(new PropertyValueFactory<Lecteur, String>("id"));
			nomCol.setCellValueFactory(new PropertyValueFactory<Lecteur, String>("nom"));
			prenomCol.setCellValueFactory(new PropertyValueFactory<Lecteur, String>("prenom"));
			sanCol.setCellValueFactory(new PropertyValueFactory<Lecteur, Integer>("nombreSection"));
			dateInsCol.setCellValueFactory(new PropertyValueFactory<Lecteur, Date>("dateInscrire"));
			dateNaCol.setCellValueFactory(new PropertyValueFactory<Lecteur, Date>("dateNaissance"));
			
			
			Callback<TableColumn<Lecteur, String>,TableCell<Lecteur, String>>cellfactory= (TableColumn<Lecteur, String> col)->{
				
				final TableCell<Lecteur, String> cell = new TableCell<Lecteur, String>(){
					@Override
					public void updateItem(String item, boolean emplty) {
						if(emplty) {
							setGraphic(null);
							setText(null);
						}else {
							ImageView restoreIcon = new ImageView(new Image("/com/app/img/icons8_trash_restore_25px.png"));
							restoreIcon.prefWidth(25);
							restoreIcon.prefHeight(25);
							
							Button btnRestor = new Button(null,restoreIcon);
							btnRestor.setOnAction(e->{
								Lecteur lecteur = table.getSelectionModel().getSelectedItem();
								if(lecteur!=null) {
									try {
										LecteurDB.restor(lecteur);
										lecteurs.remove(table.getSelectionModel().getSelectedIndex());
									} catch (SQLException e1) {
										
									}
								}
							});
							StackPane v = new StackPane(btnRestor);

							setGraphic(v);
							setText(null);
						}
					}
				};
				return cell;
			};
			editCol.setCellFactory(cellfactory);
			sanCol.setCellFactory(col->{
				return new TableCell<Lecteur, Integer>(){
					protected void updateItem(Integer item,boolean isEmpty) {
						if(item ==null || isEmpty ) {
							setText(null);
							setStyle(null);
						}else {
							setText(item+"");	
							if(item>=3 ){
								setStyle("-fx-text-fill:rgb(255,0,0);");
							}
						}
					}
				};
			});
			loadData();
			FilteredList<Lecteur>filterData=new FilteredList<Lecteur>(lecteurs, e->true);
			txtRecherche.textProperty().addListener((O,V,newV)->{
				filterData.setPredicate(lecteur->{
					
					if(newV==null || newV.isEmpty())return true;
					String value=newV.toLowerCase();
					
					//if(emprunt.getId().contains(value))return true;
					if(lecteur.getNom().toLowerCase().contains(value))return true;
					if(lecteur.getPrenom().toLowerCase().contains(value))return true;
				//	if(lecteur.f().toLowerCase().contains(value))return true;
					return false;
				});
				
				SortedList<Lecteur>sortedList = new SortedList<Lecteur>(filterData);
				sortedList.comparatorProperty().bind(table.comparatorProperty());
				table.setItems(sortedList);
				
			});
		
		}
		
		@FXML
	    void returnBack(ActionEvent event) {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/com/app/fxml/Lecteur.fxml"));
			try {
				loader.load();
				Parent root = loader.getRoot();
				Stage stage = (Stage) table.getScene().getWindow();
				Scene scene = new Scene(root);
				stage.setScene(scene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
		  @FXML
		    void drag(MouseEvent event) {
			  Stage stage = (Stage) table .getScene().getWindow();
			  stage.setX(event.getScreenX()-x);
			  stage.setY(event.getScreenY()-y);
		    }

		    @FXML
		    void press(MouseEvent event) {
	    	 Stage stage = (Stage) table .getScene().getWindow();
	    	 stage.setOpacity(0.8);
		    	x= event.getX();
		    	y= event.getY();
		    }
		    @FXML
		    void releas(MouseEvent event) {
		    	 Stage stage = (Stage) table .getScene().getWindow();
		    	 stage.setOpacity(1);
		    }
		   
}
