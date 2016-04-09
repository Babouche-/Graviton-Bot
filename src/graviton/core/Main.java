package graviton.core;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.getIcons().add(new Image("file:data/icon/icon.png"));
        primaryStage.setTitle("Graviton - Bot");
        Parent parent = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Group root = new Group(parent.getChildrenUnmodifiable());
        root.getChildren().add(generateImage());
        primaryStage.setScene(new Scene(root, 391, 470));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public ImageView generateImage() {
        ImageView view = new ImageView();
        view.setFitWidth(323);
        view.setFitHeight(154);
        view.setLayoutX(39);
        view.setLayoutY(28);
        view.setImage(new Image("file:data/images/graviton.png"));
        return view;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
