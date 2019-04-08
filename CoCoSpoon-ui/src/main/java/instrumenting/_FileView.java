package instrumenting;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

public class _FileView extends Application {
  private String path;
  private String classToDisplay;

  List<String> lines = new ArrayList<>();

  public _FileView(String path, String classToDisplay) {
    this.path = path;
    this.classToDisplay = classToDisplay;
  }

  public static void run() {
    Application.launch();
  }

  @Override
  public void init() throws Exception {
    try (BufferedReader br = new BufferedReader(new FileReader(path))) {
      String sCurrentLine;
      while ((sCurrentLine = br.readLine()) != null) {
        lines.add(sCurrentLine);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void start(Stage stage) throws Exception {
    init();
    stage.setTitle(("Couverture de la classe " + (path)));
    ListView<String> list = new ListView(FXCollections.observableArrayList(lines));
    list.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
      @Override
      public ListCell<String> call(ListView<String> listView) {
        return new FruitFlowCell();
      }
    });
    list.setPrefSize(900, 600);
    // stage.setFullScreen(true);
    Scene scene = new Scene(list);
    stage.setScene(scene);
    stage.show();
  }

  class FruitFlowCell extends ListCell<String> {
    @Override
    protected void updateItem(String s, boolean empty) {
      super.updateItem(s, empty);
      if (!(isEmpty())) {

        setGraphic(createTextFlow(s));
        if ((getGraphic()) != null) {
          setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        } else {
          setContentDisplay(ContentDisplay.LEFT);
        }
      }
    }

    private Node createTextFlow(String msg) {
      FlowPane flow = new FlowPane();
      Random r = new Random();
      int tmp = r.nextInt(2);
      Text text = new Text(msg);

      if (_Instrumenting.lines.get(classToDisplay).get(this.getIndex() + 1) == null) {
        // NOT INSTRUMENTED
        flow.setStyle("-fx-background-color: #FFFFFF;");
      } else if (_Instrumenting.lines.get(classToDisplay).get(this.getIndex() + 1)) {
        // COVER
        flow.setStyle("-fx-background-color: #C0FFC0;");
      } else {
        // NOT COVER
        flow.setStyle("-fx-background-color: #FFA0A0;");
      }

      flow.getChildren().add(text);
      return flow;
    }
  }
}
