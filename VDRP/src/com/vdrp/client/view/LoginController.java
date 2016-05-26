package com.vdrp.client.view;

import com.vdrp.client.MainApp;
import com.vdrp.client.model.User;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LoginController {
	@FXML
	private TextField userName;
	@FXML
	private TextField password;
	@FXML
	private Button loginButton;

	private MainApp mainApp;

	public LoginController(){

	}
	@FXML
	private void initialize(){
		loginButton.setOnAction((event) ->{
			User currentUser = mainApp.getUser(userName.getText());
			if( currentUser.IsPassWordOk(password.getText())){
				mainApp.setTabpanel();
			}
		});
	}
	public void setmainApp(MainApp mainApp){
		this.mainApp = mainApp;
	}



}
