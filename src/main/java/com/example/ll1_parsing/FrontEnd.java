package com.example.ll1_parsing;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;

public class FrontEnd extends Application {

    private Scene mainScene;
    private Scene ll1TableScene;
    private Scene scanScene;
    private  File file;

    private String resuilt=" please do the scan first";
    private ArrayList<Token>tokenArrayList =new ArrayList<>();
    private Scanner scanner = new Scanner();

    private Parser parser = new Parser();

    @Override
    public void start(Stage primaryStage) throws IOException {
        parser = new Parser();
        BackgroundImage backgroundImage = null;
        try {
            Image image = new Image(new FileInputStream("src/main/resources/image3.jpg"));
            backgroundImage = new BackgroundImage(image,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.DEFAULT,
                    BackgroundSize.DEFAULT);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BorderPane root = new BorderPane();
        root.setBackground(new Background(backgroundImage));

        VBox buttonBox = new VBox(20);
        buttonBox.setAlignment(Pos.CENTER);

        Button uploadButton = createStyledButton("Upload Code File",20);
        uploadButton.setOnAction(actionEvent -> openFile(primaryStage));
        Button showTableButton = createStyledButton("Show  LL1  Table",20);
        showTableButton.setOnAction(event -> primaryStage.setScene(ll1TableScene));

        buttonBox.getChildren().addAll(uploadButton, showTableButton);

        root.setCenter(buttonBox);

        mainScene = new Scene(root, 900, 600);
        VBox vbox = new VBox(20);
        vbox.setAlignment(Pos.CENTER);
        TextArea textArea = new TextArea();
        textArea.setMaxHeight(400);
        textArea.setStyle("-fx-font-size: 16px;");
        textArea.setEditable(false);
        Button scanButton = createStyledButton("List Of Token (Scan)",20);
        Button parse = createStyledButton("Parsing Code",20);
        Button backButton = createStyledButton("Back",20);
        parse.setOnAction(actionEvent -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Parse Action");
            alert.setHeaderText(null);
            alert.setContentText(resuilt);
            alert.showAndWait();

        });

        scanButton.setOnAction(actionEvent -> {
            try {
                tokensScene(primaryStage,file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        backButton.setOnAction(event -> {
            restartApplication(primaryStage);
        });
        vbox.getChildren().addAll(textArea, scanButton,parse,backButton);
        scanScene = new Scene(vbox,900,600);
        TextArea paringTable = new TextArea();
        paringTable.setText(parser.generateLL1Table());
        paringTable.setStyle("-fx-font-size: 16px;");
        Button backButton2 = createStyledButton("Back",20);

        VBox ll1TableBox = new VBox();
        ll1TableBox.setAlignment(Pos.CENTER);
        ll1TableBox.getChildren().addAll(paringTable, backButton2);

        backButton2.setOnAction(event -> primaryStage.setScene(mainScene));

        ll1TableScene = new Scene(ll1TableBox, 900, 600);

        primaryStage.setScene(mainScene);

        primaryStage.show();
    }

    private void tokensScene(Stage primaryStage, File file) throws IOException {
        TextArea tokenArea = new TextArea();
        tokenArea.setEditable(false);
        ArrayList<Token> tokens = scanner.scan(file.getPath());
        tokenArrayList=tokens;
        StringBuilder tokenStringBuilder = new StringBuilder();
        for (Token token : tokens) {
            tokenStringBuilder.append(token.token).append("---->").append(token.type).append("\n");
        }
        resuilt = parser.parsingCode(tokens);
        tokenArea.setText(tokenStringBuilder.toString());
        tokenArea.setStyle("-fx-font-size: 16px;");
        Button backButton = createStyledButton("Back", 20);
        backButton.setOnAction(event -> primaryStage.setScene(scanScene));

        VBox tokenBox = new VBox(20);
        tokenBox.setAlignment(Pos.CENTER);
        tokenBox.getChildren().addAll(tokenArea, backButton);

        Scene tokenScene = new Scene(tokenBox, 900, 600);
        primaryStage.setScene(tokenScene);
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void openFile(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Code File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.*")
        );
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        file = selectedFile;
        if (selectedFile != null) {
            updateScanScene(selectedFile, primaryStage);
            primaryStage.setScene(scanScene);
        }
    }

    private void updateScanScene(File selectedFile, Stage primaryStage) {
        TextArea textArea = (TextArea) scanScene.getRoot().getChildrenUnmodifiable().get(0);
        StringBuilder fileContent = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                fileContent.append("LINE ").append(lineNumber).append(" -> ").append(line).append("\n");
                lineNumber++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        textArea.setText(fileContent.toString());
    }



    private Button createStyledButton(String text, double fontSize) {
        Button button = new Button(text);
        button.setStyle("-fx-font-size: " + fontSize + "px;");
        return button;
    }

    private void restartApplication(Stage primaryStage) {
        primaryStage.close();
        Platform.runLater(() -> {
            try {
                new FrontEnd().start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
    }

}
