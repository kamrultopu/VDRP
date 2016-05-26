package com.vdrp.client.view;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.NoSuchPaddingException;

import com.vdrp.client.MainApp;
import com.vdrp.client.VdrpClient;
import com.vdrp.config.Mode;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ClientController {
	@FXML
	private Button uploadFileButton;
	@FXML
	private Button buttonSave;
	@FXML
	private Button fileChooseButton;
	@FXML
	private Button buttonDestination;
	@FXML
	private Button buttonVerify;
	@FXML
	private ChoiceBox choiceBoxNoOfCopy;
	@FXML
	private ChoiceBox verifyChoiceBox;
	@FXML
	private TextField uploadFilePath;
	@FXML
	private TextField downloadFileName;
	@FXML
	private TextField downloadFolderName;
	@FXML
	private TextField verifyFileName;
	@FXML
	private Label fileUploadStatus;
	@FXML
	private Label verificationStatus;
	@FXML
	private Label downloadStatus;

	private MainApp mainApp;
	private VdrpClient vd;

	File selectedFile;
	Stage stage;
	public ClientController(){
		stage = new Stage();
	}
	@FXML
	private void initialize() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException, NoSuchPaddingException, ClassNotFoundException, IOException{
		vd = new VdrpClient();
		choiceBoxNoOfCopy.getItems().addAll("1","2","3");
		verifyChoiceBox.getItems().addAll("1","2","3");
		fileChooseButton.setOnAction((event) ->{
			OpenFileChose();
		});
		uploadFileButton.setOnAction((event) -> {
			UploadFile();
		});
		buttonDestination.setOnAction((event)->{
			OpenFileSaveDialog();
		});
		buttonSave.setOnAction((event)->{
			try {
				SaveFile();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		buttonVerify.setOnAction((event)->{
			try {
				Verify();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

	}
	private void Verify() throws IOException, NoSuchAlgorithmException {
		String filename =  verifyFileName.getText();
		System.out.println(filename);
		int nCopy = Integer.parseInt((String) verifyChoiceBox.getValue());
		System.out.println(nCopy);
		int response = vd.verifyRedundancy(filename, nCopy);
		if( response > 0 ){
			verificationStatus.setText("Verified Redundancy: " + response);
			verificationStatus.setVisible(true);
		} else {
			verificationStatus.setText("Error");
			verificationStatus.setVisible(true);
		}

	}
	private void OpenFileSaveDialog() {
		Desktop desktop = Desktop.getDesktop();
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Save To");
		selectedFile = directoryChooser.showDialog(stage);
		downloadFolderName.setText(selectedFile.getAbsolutePath());

	}
	private void SaveFile() throws IOException {
		String filename = downloadFolderName.getText() + "\\" + downloadFileName.getText();
		System.out.println(filename);
		int response = vd.getFile(downloadFolderName.getText(),downloadFileName.getText(), 0, Mode.ACCESS_MODE);
		if( response == 200 ){
			downloadStatus.setText("Downloades SuccessFully");
			downloadStatus.setVisible(true);
		} else {
			downloadStatus.setText("Error");
			downloadStatus.setVisible(true);
		}
	}
	private void UploadFile() {
		/*
		if( choiceBoxNoOfCopy.valueProperty() == null ){
			return;
		}*/
		try {
			String filename = selectedFile.getCanonicalPath();
			int nCopy = Integer.parseInt((String) choiceBoxNoOfCopy.getValue());
			System.out.println(filename + " : " + nCopy);
			String response = vd.uploadFile(filename, nCopy);
			fileUploadStatus.setText(response);
			fileUploadStatus.setVisible(true);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//System.out.println(vd.getFile(filename,0,Mode.ACCESS_MODE));

	}
	private void OpenFileChose() {
		Desktop desktop = Desktop.getDesktop();
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Choose File to Upload");
		Stage stage = new Stage();
		selectedFile = fileChooser.showOpenDialog(stage);
		uploadFilePath.setText(selectedFile.getAbsolutePath());

	}
	public void setmainApp(MainApp mainApp){
		this.mainApp = mainApp;
	}
}
