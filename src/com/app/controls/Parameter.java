package com.app.controls;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.controlsfx.control.Notifications;
import org.controlsfx.control.textfield.CustomTextField;

import com.app.model.Categorie;

import com.app.model.Sanction;
import com.app.utis.CategorieDB;
import com.app.utis.SanctionDB;

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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;

public class Parameter  implements Initializable{
	private double x,y;
	boolean isCatShow =true;

    @FXML
    private Button btnSwitch;

    @FXML
    private AnchorPane categoriePane;

    @FXML
    private TableColumn<Categorie, String> editColCat;

    @FXML
    private TableColumn<Categorie, Integer> idColCat;

    @FXML
    private TableColumn<Categorie, String> intColCat;

    @FXML
    private TableColumn<Categorie, Integer> nbrOuvColCat;

    @FXML
    private AnchorPane sanctionPane;

    @FXML
    private TableView<Categorie> tblCat;

    @FXML
    private CustomTextField txtNouvCat;

    @FXML
    private CustomTextField txtRechCat;
    private ObservableList<Categorie>categories;
    private ObservableList<Sanction>sanctiones;
    //--------------------------
    @FXML
    private TableColumn<Sanction, String> intSanCol;
    @FXML
    private TableColumn<Sanction, Integer> idSanCol;

    @FXML
    private TableColumn<Sanction, String> editSanCol;
    @FXML
    private TableView<Sanction> tableSan;
    @FXML
    private CustomTextField txtNewSan;
    @FXML
    private CustomTextField txtSanReche;
    @FXML
    private TableColumn<Sanction, Integer> nbrSanCol;
    @FXML
    void addSan(ActionEvent event) {
    	Stage stage = (Stage) txtNouvCat.getScene().getWindow();
  //  	stage.getScene().getStylesheets().add("/com/app/styling/Notif.css");
    	try {
	    	if(txtNewSan ==null || txtNewSan.getText().isBlank()) {
	    		Notifications.create().hideAfter(Duration.seconds(3)).owner(stage).graphic(new ImageView(new Image("/com/app/img/icons8_warning_shield_55px.png"))).onAction(e->txtNewSan.requestFocus()).position(Pos.TOP_RIGHT).text("INTITULE Sanction INVALIDE").show();
	    		return ;
	    	}
	    	else if(SanctionDB.alreadyExist(txtNewSan.getText())) {
	    		Notifications.create().hideAfter(Duration.seconds(3)).owner(stage).graphic(new ImageView(new Image("/com/app/img/icons8_warning_shield_55px.png"))).onAction(e->txtNewSan.requestFocus()).position(Pos.TOP_RIGHT).text("Sanction EXISTE DIJA").show();
	    		return ;
	    	}else {
	    		Sanction san = new Sanction(0, txtNewSan.getText());
	    		SanctionDB.add(san);
	    		Notifications.create().hideAfter(Duration.seconds(3)).owner(stage)
				.graphic(new ImageView(new Image("/com/app/img/icons8_information_55px.png"))).position(Pos.TOP_RIGHT).text( "Sanction ajouter avec succes ").show();
				sanctiones.add(san);
	    	}
    	} catch (SQLException e) {
    		
    		e.printStackTrace();
    	}
    }
  
    @FXML
    void addNewCat(ActionEvent event) {
    	Stage stage = (Stage) txtNouvCat.getScene().getWindow();
    	//stage.getScene().getStylesheets().add("/com/app/styling/Notif.css");
    	try {
	    	if(txtNouvCat ==null || txtNouvCat.getText().isBlank()) {
	    		Notifications.create().hideAfter(Duration.seconds(3)).owner(stage).graphic(new ImageView(new Image("/com/app/img/icons8_warning_shield_55px.png"))).onAction(e->txtNouvCat.requestFocus()).position(Pos.TOP_RIGHT).text("INTITULE CATEGORIE INVALIDE").show();
	    		return ;
	    	}
	    	else if(CategorieDB.alreadyExist(txtNouvCat.getText())) {
	    		Notifications.create().hideAfter(Duration.seconds(3)).owner(stage).graphic(new ImageView(new Image("/com/app/img/icons8_warning_shield_55px.png"))).onAction(e->txtNouvCat.requestFocus()).position(Pos.TOP_RIGHT).text("CATEGORIE EXISTE DIJA").show();
	    		return ;
	    	}else {
	    		Categorie cat = new Categorie(0, txtNouvCat.getText(), 0);
	    		CategorieDB.add(cat);
	    		Notifications.create().hideAfter(Duration.seconds(3)).owner(stage)
				.graphic(new ImageView(new Image("/com/app/img/icons8_information_55px.png"))).position(Pos.TOP_RIGHT).text( "Categorie ajouter avec succes ").show();
				categories.add(cat);
	    	}
    	} catch (SQLException e) {
    		
    		e.printStackTrace();
    	}
    }

	@FXML
    public void switchPane(ActionEvent event) {
		if(isCatShow) {
			sanctionPane.setVisible(true);
			categoriePane.setVisible(false);
			btnSwitch.setText("affiche Categorie");
		}else {
			categoriePane.setVisible(true);
			sanctionPane.setVisible(false);
			btnSwitch.setText("affiche Sanction");
		}
		isCatShow=!isCatShow;
	}
	@FXML
	 void returnBack(ActionEvent event) {
		 FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/com/app/fxml/PanneauControl.fxml"));
			try {
				loader.load();
				Parent root = loader.getRoot();
				Stage stage = (Stage) txtNouvCat.getScene().getWindow();
				Scene scene = new Scene(root);
				stage.setScene(scene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	 @FXML
	    void drag(MouseEvent event) {
		 Stage stage = (Stage) ((HBox)event.getSource()).getScene().getWindow();
		  stage.setX(event.getScreenX()-x);
		  stage.setY(event.getScreenY()-y);
	    }

	    @FXML
	    void press(MouseEvent event) {
			 Stage stage = (Stage) ((HBox)event.getSource()) .getScene().getWindow();
			 stage.setOpacity(0.8);
	    	x= event.getX();
	    	y= event.getY();
	    }
	    @FXML
	    void releas(MouseEvent event) {
	    	Stage stage = (Stage) ((HBox)event.getSource()) .getScene().getWindow();
	    	 stage.setOpacity(1);
	    }
		@Override
		public void initialize(URL arg0, ResourceBundle arg1) {
			categories=FXCollections.observableArrayList();
			sanctiones=FXCollections.observableArrayList();
			idColCat.setCellValueFactory(new PropertyValueFactory<Categorie, Integer>("id"));
			nbrOuvColCat.setCellValueFactory(new PropertyValueFactory<Categorie, Integer>("nbr"));
			intColCat.setCellValueFactory(new PropertyValueFactory<Categorie, String>("intitule"));
			idSanCol.setCellValueFactory(new PropertyValueFactory<Sanction, Integer>("id"));
			intSanCol.setCellValueFactory(new PropertyValueFactory<Sanction, String>("intitule"));
			nbrSanCol.setCellValueFactory(new PropertyValueFactory<Sanction, Integer>("nbr"));
			nbrOuvColCat.setCellFactory(col->{
				return new TableCell<Categorie, Integer>(){
					protected void updateItem(Integer item,boolean isEmpty) {
						if(item ==null || isEmpty ) {
							setText(null);
							setStyle(null);
						}else {
							setText(item+"");	
							if(item==0 ){
								setStyle("-fx-text-fill:rgb(255,0,0);");
							}
						}
					}
				};
			});
			nbrSanCol.setCellFactory(col->{
				return new TableCell<Sanction, Integer>(){
					protected void updateItem(Integer item,boolean isEmpty) {
						if(item ==null || isEmpty ) {
							setText(null);
							setStyle(null);
						}else {
							setText(item+"");	
							if(item==0 ){
								setStyle("-fx-text-fill:rgb(255,0,0);");
							}
						}
					}
				};
			});
			
	Callback<TableColumn<Categorie, String>,TableCell<Categorie, String>>cellfactory= (TableColumn<Categorie, String> col)->{
					
					final TableCell<Categorie, String> cell = new TableCell<Categorie, String>(){
						@Override
						public void updateItem(String item, boolean emplty) {
							if(emplty) {
								setGraphic(null);
								setText(null);
							}else {
								ImageView editIcon = new ImageView(new Image("/com/app/img/icons8_edit_25px.png"));
								editIcon.prefWidth(25);
								editIcon.prefHeight(25);
								ImageView deleteIcon = new ImageView(new Image("/com/app/img/icons8_trash_25px.png"));
								deleteIcon.prefWidth(25);
								deleteIcon.prefHeight(25);
								
								Button btnDelete = new Button(null,deleteIcon);
								btnDelete .setOnAction(e->{
									Categorie categorie = tblCat.getSelectionModel().getSelectedItem();
									if(categorie!=null) {
										if(categorie.getNbr()==0) {
											FXMLLoader loader = new FXMLLoader();
											loader.setLocation(getClass().getResource("/com/app/fxml/CustomAlert.fxml"));
											try {
													loader.load();
													AlertControl controlle = loader.getController();
													controlle.setControl(Parameter.this, categorie, "Categorie", "Voulais-vous vraiment supprimer La Categorie ?",tblCat.getSelectionModel().getSelectedIndex(),false );	
													Parent root = loader.getRoot();
													Stage stage = new Stage();
													Scene scene = new Scene(root);
													stage.setScene(scene);
													stage.initStyle(StageStyle.UNDECORATED);
													stage.initModality(Modality.WINDOW_MODAL);
													stage.initOwner(tableSan.getScene().getWindow());
													stage.getOwner().setOpacity(0.4);
													stage.show();
											} catch (IOException e1) {
													e1.printStackTrace();
											}
										}
									}
									else
										System.out.println("select a row ");				
								});
								Button btnUpdate = new Button(null,editIcon);
								btnUpdate.setOnAction(e->{
									Categorie cat = tblCat.getSelectionModel().getSelectedItem();
									if(cat!=null) {
										
										FXMLLoader loader = new FXMLLoader();
										loader.setLocation(getClass().getResource("/com/app/fxml/ModifPara.fxml"));
										try {
											loader.load();
											ModifPara control =loader.getController();
											control.setControl(Parameter.this, cat, tblCat.getSelectionModel().getSelectedIndex());
											Parent root = loader.getRoot();
											Stage stage = new Stage();
											Scene scene = new Scene(root);
											scene.getStylesheets().add(getClass().getResource("/com/app/styling/Notif.css").toExternalForm());
											stage.setScene(scene);
											stage.initStyle(StageStyle.UNDECORATED);
											stage.initModality(Modality.WINDOW_MODAL);
											stage.initOwner(tblCat.getScene().getWindow());
											stage.getOwner().setOpacity(0.4);
											stage.show();
											
										} catch (IOException e1) {
											e1.printStackTrace();
										}
									}
								});
								
								HBox v = new HBox(btnUpdate,btnDelete);
								v.setAlignment(Pos.CENTER);
								v.setSpacing(10);
								setGraphic(v);
							setText(null);
						}
					}
				};
				return cell;
			};
			editColCat.setCellFactory(cellfactory);
			Callback<TableColumn<Sanction, String>,TableCell<Sanction, String>>cellfactorySan= (TableColumn<Sanction, String> col)->{
				
				final TableCell<Sanction, String> cell = new TableCell<Sanction, String>(){
					@Override
					public void updateItem(String item, boolean emplty) {
						if(emplty) {
							setGraphic(null);
							setText(null);
						}else {
							ImageView editIcon = new ImageView(new Image("/com/app/img/icons8_edit_25px.png"));
							editIcon.prefWidth(25);
							editIcon.prefHeight(25);
							ImageView deleteIcon = new ImageView(new Image("/com/app/img/icons8_trash_25px.png"));
							deleteIcon.prefWidth(25);
							deleteIcon.prefHeight(25);
							
							Button btnDelete = new Button(null,deleteIcon);
							btnDelete .setOnAction(e->{
								Sanction san = tableSan.getSelectionModel().getSelectedItem();
								if(san!=null) {
									if(san.getNbr()==0) {
										FXMLLoader loader = new FXMLLoader();
										loader.setLocation(getClass().getResource("/com/app/fxml/CustomAlert.fxml"));
										try {
											loader.load();
											AlertControl controlle = loader.getController();
											controlle.setControl(Parameter.this, san, "Sanction", "Voulais-vous vraiment supprimer La Sanction ?",tableSan.getSelectionModel().getSelectedIndex(),false );	
											Parent root = loader.getRoot();
											Stage stage = new Stage();
											Scene scene = new Scene(root);
											
											stage.setScene(scene);
											stage.initStyle(StageStyle.UNDECORATED);
											stage.initModality(Modality.WINDOW_MODAL);
											stage.initOwner(tableSan.getScene().getWindow());
											stage.getOwner().setOpacity(0.4);
											stage.show();
											
										} catch (IOException e1) {
											e1.printStackTrace();
										}
									}
								}
							});
							Button btnUpdate = new Button(null,editIcon);
							btnUpdate.setOnAction(e->{
								Sanction san = tableSan.getSelectionModel().getSelectedItem();
								if(san!=null) {
									FXMLLoader loader = new FXMLLoader();
									loader.setLocation(getClass().getResource("/com/app/fxml/ModifPara.fxml"));
									try {
										loader.load();
										ModifPara control =loader.getController();
										control.setControl(Parameter.this, san, tableSan.getSelectionModel().getSelectedIndex());
										Parent root = loader.getRoot();
										Stage stage = new Stage();
										Scene scene = new Scene(root);
										scene.getStylesheets().add(getClass().getResource("/com/app/styling/Notif.css").toExternalForm());
										stage.setScene(scene);
										stage.initStyle(StageStyle.UNDECORATED);
										stage.initModality(Modality.WINDOW_MODAL);
										stage.initOwner(tblCat.getScene().getWindow());
										stage.getOwner().setOpacity(0.4);
										stage.show();
										
									} catch (IOException e1) {
										e1.printStackTrace();
									}
								}
							});
							
							HBox v = new HBox(btnUpdate,btnDelete);
							v.setAlignment(Pos.CENTER);
							v.setSpacing(10);
							setGraphic(v);
							setText(null);
						}
					}
				};
				return cell;
			};
			
			editSanCol.setCellFactory(cellfactorySan);
			loadDataCat();
			FilteredList<Categorie>filtedDataCat = new FilteredList<Categorie>(categories, p->true);
			txtRechCat.textProperty().addListener((Obs,oldV,newV)->{
				filtedDataCat.setPredicate(cat->{
					if(newV==null || newV.isEmpty())return true;
					String value = newV.toLowerCase();
					if(cat.getIntitule().toLowerCase().contains(value))return true;
					return false;
				});
				SortedList<Categorie>sorteddata = new SortedList<Categorie>(filtedDataCat);
				sorteddata.comparatorProperty().bind(tblCat.comparatorProperty());
				tblCat.setItems(sorteddata);
			});
			FilteredList<Sanction>filtedDataSan = new FilteredList<Sanction>(sanctiones, p->true);
			txtSanReche.textProperty().addListener((Obs,oldV,newV)->{
				filtedDataSan.setPredicate(san->{
					if(newV==null || newV.isEmpty())return true;
					String value = newV.toLowerCase();
					if(san.getIntitule().toLowerCase().contains(value))return true;
					return false;
				});
				SortedList<Sanction>sorteddata = new SortedList<Sanction>(filtedDataSan);
				sorteddata.comparatorProperty().bind(tableSan.comparatorProperty());
				tableSan.setItems(sorteddata);
			});
		}
		private void loadDataCat(){
			Task<Void>task = new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					categories.setAll(CategorieDB.getData()) ;
					tblCat.setItems(categories);
					sanctiones.setAll(SanctionDB.getData());
					tableSan.setItems(sanctiones);
					return null;
				}
			};
			task.setOnFailed(e->System.out.println("field"));
			Thread th = new Thread(task);
			th.setDaemon(true);
			th.start();
		}
		public void setUpdate(Object data, int index, boolean isCat) {
			if(isCat)
				categories.set(index,(Categorie)data);
			else 
				sanctiones.set(index,(Sanction) data);
		}

		public void delete(int index, boolean isCat) {
			if(isCat) {
				categories.remove(index);
			}else
				sanctiones.remove(index);
			
		}
}
