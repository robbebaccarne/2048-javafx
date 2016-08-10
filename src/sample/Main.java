package sample;

import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        final Game game = new Game();
        final GameView gameView = new GameView(primaryStage, game);
        gameView.setup();


//        Group root = new Group();
//        primaryStage.setResizable(false);
//        Scene scene = new Scene(root, 250, 250, Color.web("#BBADA0"));
//        primaryStage.setScene(scene);
//
//        scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
//            pt.play();
//            System.out.println(key.getCode() == KeyCode.UP);
//        });
//
//        Rectangle rect = new Rectangle(0, 0, 50, 50);
//        rect.setArcHeight(15);
//        rect.setArcWidth(15);
//        rect.setFill(Color.ORANGE);
////        root.getChildren().add(rect);
//
//        Text text = new Text("hello");
//
//        StackPane stack = new StackPane(rect, text);
//
//        stack.relocate(50, 50);
//
////        stack.setLayoutX(50);
////        stack.setLayoutY(50);
//        root.getChildren().add(stack);
//
//        ScaleTransition st = new ScaleTransition(Duration.millis(1000), stack);
//        st.setFromX(0);
//        st.setToX(1);
////        st.setByX(1.5f);
////        st.setByY(1.5f);
//        st.setCycleCount(1);
////        st.setAutoReverse(true);
//
//        TranslateTransition tt = new TranslateTransition(Duration.millis(1000), stack);
//        tt.setFromX(0);
//        tt.setToX(100);
//        tt.setCycleCount(1);
//
//        pt = new ParallelTransition(st, tt);
//
//
////        final KeyValue kv = new KeyValue(rect.xProperty(), 300,
////                Interpolator.EASE_BOTH);
////        final KeyFrame kf = new KeyFrame(Duration.millis(500), kv);
////        pt.getKeyFrames().add(kf);
//
//
//        pt.setOnFinished((event) -> {
////            System.out.println("hi");
////            root.getChildren().remove(rect);
//        });

    }

}