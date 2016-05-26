package com.vdrp.client;

import java.io.IOException;

import com.vdrp.client.model.User;
import com.vdrp.client.view.LoginController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {
	private Stage primaryStage;
	private BorderPane rootLayout;
	static User user1;
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("VDRP | Client API");
		initRootLayout();
	}

	private void setLogInPanel() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class.getResource("view/loginPanel.fxml"));
		AnchorPane loginPanel;
		try {
			loginPanel = (AnchorPane) loader.load();
			LoginController controller = loader.getController();
			controller.setmainApp(this);
			rootLayout.setCenter(loginPanel);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	public void setTabpanel(){
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class.getResource("view/clientTabPanel.fxml"));
		AnchorPane tabPanel;
		try {
			tabPanel = (AnchorPane) loader.load();
			rootLayout.setCenter(tabPanel);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public Stage getPrimaryStage(){
		return primaryStage;
	}

	private void initRootLayout() {


		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/mainPanel.fxml"));
			rootLayout = (BorderPane) loader.load();
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			setTabpanel();
			primaryStage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("in exception");
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		user1 = new User("kamrul","1234");
		launch(args);
	}

	public User getUser(String text) {
		return user1;
	}
}
