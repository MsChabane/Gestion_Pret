package com.app.controls;


import java.io.IOException;
import java.net.URL;
import java.util.Date;

import java.util.ResourceBundle;

import org.controlsfx.control.textfield.CustomTextField;

import com.app.model.Emprunt;
import com.app.model.Lecteur;
import com.app.model.Ouvrage;
import com.app.utis.EmpruntDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
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
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

public class RevenueControl implements Initializable {
	private double x,y;
    @FXML
    private TableColumn<Emprunt, Date> datePretCol;

    @FXML
    private TableColumn<Emprunt, Date> dateRemisCol;

    @FXML
    private TableColumn<Emprunt, String> idCol;

    @FXML
    private TableColumn<Emprunt, Lecteur> lectCol;

    @FXML
    private TableColumn<Emprunt, Ouvrage> ouvgCol;
    @FXML
    private TableColumn<Emprunt, Integer> nbrCol;
    @FXML
    private TableColumn<Emprunt, String> edit;
    @FXML
    private CustomTextField txtRecherche;
    @FXML
    private TableView<Emprunt> table;
    private ObservableList<Emprunt>emprunts;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		idCol.setCellValueFactory(new PropertyValueFactory<Emprunt, String>("id"));
		datePretCol.setCellValueFactory(new PropertyValueFactory<Emprunt, Date>("datePret"));
		dateRemisCol.setCellValueFactory(new PropertyValueFactory<Emprunt, Date>("dateRmis"));
		ouvgCol.setCellValueFactory(new PropertyValueFactory<Emprunt, Ouvrage>("ouvrage"));
		lectCol.setCellValueFactory(new PropertyValueFactory<Emprunt, Lecteur>("lecteur"));
		nbrCol.setCellValueFactory(new PropertyValueFactory<Emprunt, Integer>("joursRest"));
		ouvgCol.setCellFactory(col->{
			return new TableCell<Emprunt, Ouvrage>(){
				protected void updateItem(Ouvrage item,boolean isEmpty) {
					if(item ==null || isEmpty ) {
						setText(null);
						setStyle(null);
					}else {
						setText(item.getTitre());
						
					}
				}
			};
		});
		nbrCol.setCellFactory(col->{
			return new TableCell<Emprunt, Integer>(){
				protected void updateItem(Integer item,boolean isEmpty) {
					if(item ==null || isEmpty ) {
						setText(null);
						setStyle(null);
					}else {
						setText(item+"");
						if(item <=0){
							setStyle("-fx-text-fill:rgb(255,0,0);");
						}
					}
				}
			};
		});
		lectCol.setCellFactory(col->{
			return new TableCell<Emprunt, Lecteur>(){
				protected void updateItem(Lecteur item,boolean isEmpty) {
					if(item ==null || isEmpty ) {
						setText(null);
						setStyle(null);
					}else {
						setText(item.getNom().toUpperCase().charAt(0)+"."+item.getPrenom());
					}
				}
			};
		});
		
		Callback<TableColumn<Emprunt, String>,TableCell<Emprunt, String>>cellfactory= (TableColumn<Emprunt, String> col)->{	
			final TableCell<Emprunt, String> cell = new TableCell<Emprunt, String>(){
				@Override
				public void updateItem(String item, boolean emplty) {
					if(emplty) {
						setGraphic(null);
						setText(null);
					}else {
						ImageView yesIcon = new ImageView(new Image("/com/app/img/icons8_checkmark_yes_25px.png"));
						yesIcon.prefWidth(25);
						yesIcon.prefHeight(25);
						ImageView noIcon = new ImageView(new Image("/com/app/img/icons8_no_entry_25px.png"));
						noIcon.prefWidth(25);
						noIcon.prefHeight(25);
						ImageView showIcon = new ImageView(new Image("/com/app/img/icons8_eye_25px_1.png"));
						showIcon.prefWidth(25);
						showIcon.prefHeight(25);
						Button btnNo = new Button(null,noIcon);
						btnNo .setOnAction(e->{
							Emprunt emprunt= table.getSelectionModel().getSelectedItem();
							if(emprunt!=null) {
								FXMLLoader loader = new FXMLLoader();
								loader.setLocation(getClass().getResource("/com/app/fxml/RevenusEchec.fxml"));
								try {
									loader.load();
									RevenueEchec controlle = loader.getController();						
									controlle.setControl(RevenueControl.this, emprunt, table.getSelectionModel().getSelectedIndex());
									Parent root = loader.getRoot();
									Stage stage = new Stage();
									Scene scene = new Scene(root);
									stage.setScene(scene);
									stage.initStyle(StageStyle.UNDECORATED);
									stage.initModality(Modality.WINDOW_MODAL);
									stage.initOwner(table.getScene().getWindow());
									stage.getOwner().setOpacity(0.4);
									((Stage )table.getScene().getWindow()).getScene().getStylesheets().add("/com/app/styling/Notif.css");
									stage.show();
								} catch (IOException e1) {
									e1.printStackTrace();
								}
							}
								
						});
						Button btnYes = new Button(null,yesIcon);
						btnYes.setOnAction(e->{
							Emprunt emprunt= table.getSelectionModel().getSelectedItem();
							if(emprunt!=null) {
								FXMLLoader loader = new FXMLLoader();
								loader.setLocation(getClass().getResource("/com/app/fxml/RevenusSucces.fxml"));
								try {
									loader.load();
									RevenueSucces controlle = loader.getController();						
									controlle.setControl(RevenueControl.this, emprunt, table.getSelectionModel().getSelectedIndex());
									Parent root = loader.getRoot();
									Stage stage = new Stage();
									Scene scene = new Scene(root);
									stage.setScene(scene);
									stage.initStyle(StageStyle.UNDECORATED);
									stage.initModality(Modality.WINDOW_MODAL);
									stage.initOwner(table.getScene().getWindow());
									stage.getOwner().setOpacity(0.4);
									((Stage )table.getScene().getWindow()).getScene().getStylesheets().add("/com/app/styling/Notif.css");
									stage.show();
								} catch (IOException e1) {
									e1.printStackTrace();
								}
							}
						});
						Button btnshow = new Button(null, showIcon);
						btnshow.setOnAction(e->{
							Emprunt emprunt = table.getSelectionModel().getSelectedItem();
							if(emprunt!=null) {
								FXMLLoader loader = new FXMLLoader();
								loader.setLocation(getClass().getResource("/com/app/fxml/showEmprunt.fxml"));
								try {
									loader.load();
									AfficheDetails controlle = loader.getController();						
									controlle.set(emprunt);
									Parent root = loader.getRoot();
									Stage stage = new Stage();
									Scene scene = new Scene(root);
									stage.setScene(scene);
									stage.initStyle(StageStyle.UNDECORATED);
									stage.initModality(Modality.WINDOW_MODAL);
									stage.initOwner(table.getScene().getWindow());
									stage.getOwner().setOpacity(0.4);
									stage.show();
									
								} catch (IOException e1) {
									e1.printStackTrace();
								}
							}else
								System.out.println("select a row ");
						});
						
						HBox v = new HBox(btnshow,btnYes,btnNo);
						v.setAlignment(Pos.CENTER);
						v.setSpacing(10);
						setGraphic(v);
						setText(null);
					}
				}
			};
			return cell;
		};
		edit.setCellFactory(cellfactory);
		emprunts= FXCollections.observableArrayList();
		loadData();
		FilteredList<Emprunt>filterData=new FilteredList<Emprunt>(emprunts, e->true);
		txtRecherche.textProperty().addListener((obsList,oldV,newV)->{
			filterData.setPredicate(emprunt->{
				if(newV==null || newV.isEmpty())return true;
				String value=newV.toLowerCase();
				
				//if(emprunt.getId().contains(value))return true;
				if(emprunt.getLecteur().getNom().toLowerCase().contains(value))return true;
				if(emprunt.getLecteur().getPrenom().toLowerCase().contains(value))return true;
				if(emprunt.getEtat().toLowerCase().contains(value))return true;
				return false;
			});
			SortedList<Emprunt>sortedList = new SortedList<Emprunt>(filterData);
			sortedList.comparatorProperty().bind(table.comparatorProperty());
			table.setItems(sortedList);
		});
		table.setItems(emprunts);
		
	}
	public void loadData(){
		Task<Void>task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				table.getItems().clear();
				emprunts.addAll(EmpruntDB.getDataNnReturn());
				table.setItems(emprunts);
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
    @FXML
    void retournToOwner(ActionEvent event) {
    	 FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/com/app/fxml/PanneauControl.fxml"));
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
    public void setUpdate(int index) {
    	emprunts.remove(index);
	}
    
    
    
	
}

