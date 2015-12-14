package instrumenting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sun.javafx.charts.Legend;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class _BarChart extends Application {
  public static ObservableList<XYChart.Series<String, Number>> barChartData;

  private ArrayList<String> classes;

  List<String> currentList;

  List<XYChart.Data> dataList;

  BarChart chart;

  String current = "";

  @SuppressWarnings(value = {"unchecked", "rawtypes"})
  private void init(Stage primaryStage) {
    currentList = new ArrayList<>();
    StackPane root = new StackPane();
    primaryStage.setScene(new Scene(root));
    primaryStage.setTitle("CoCoSpoon");
    classes = new ArrayList<String>();
    String tmp = _BarChart.getLongestCommonPrefix(_Instrumenting.lines.keySet().toArray(new String[_Instrumenting.lines.keySet().size()]));
    if ((tmp.charAt(((tmp.length()) - 1))) == '.') {
      tmp = tmp.substring(0, ((tmp.length()) - 1));
    }
    classes.add(tmp);
    currentList.add(tmp);
    current = tmp;
    CategoryAxis xAxis = new CategoryAxis();
    xAxis.setCategories(FXCollections.<String>observableArrayList(classes));
    NumberAxis yAxis = new NumberAxis("Coverage (%)", 0.0, 100.0, 5.0);
    dataList = new ArrayList<>();
    XYChart.Data data = new XYChart.Data(classes.get(0), 0);
    dataList.add(data);
    _BarChart.barChartData = FXCollections.observableArrayList(new XYChart.Series("Code coverage", FXCollections.observableArrayList(dataList)));
    chart = new BarChart(xAxis, yAxis, _BarChart.barChartData, 25.0);
    chart.getXAxis().setAutoRanging(true);
    chart.setAnimated(false);
    root.getChildren().add(chart);
    Button bouton = new Button("..");
    root.getChildren().add(bouton);

    chart.setTitle("Real time code coverage");
    StackPane.setAlignment(bouton, Pos.TOP_RIGHT);

    Legend legend = (Legend) chart.lookup(".chart-legend");
    Legend.LegendItem li1 = new Legend.LegendItem("Package", new javafx.scene.shape.Rectangle(10, 4, Color.ORANGE));
    Legend.LegendItem li2 = new Legend.LegendItem("Class", new javafx.scene.shape.Rectangle(10, 4, Color.rgb(101, 153, 255)));
    legend.getItems().setAll(li1, li2);

    for (String map : _Instrumenting.lines.keySet()) {
      ObservableMap<Integer, Boolean> observedMap = FXCollections.observableMap(_Instrumenting.lines.get(map));
      observedMap.addListener(new MapChangeListener<Integer, Boolean>() {
        public void onChanged(MapChangeListener.Change<? extends Integer, ? extends Boolean> changed) {
          for (String s : currentList) {
            for (XYChart.Data<String, Number> data : _BarChart.barChartData.get(0).getData()) {
              if (s.contains(data.getXValue())) {
                data.setYValue(display(s));
              }
            }
          }
        }
      });
      _Instrumenting.lines.put(map, observedMap);
    }
    chart.setOnMouseClicked(new EventHandler<Event>() {
      @Override
      public void handle(Event event) {
        for (XYChart.Series<String, Number> serie : _BarChart.barChartData) {
          for (XYChart.Data<String, Number> item : serie.getData()) {
            item.getNode().setOnMousePressed((Event event2) -> {
              if (!(isClass(item.getXValue()))) {
                retMap = new HashMap<String, Integer>();
                current = item.getXValue();
                Map<String, Integer> changeTo = getPackageResult(item.getXValue());
                changeTo.remove(item.getXValue());
                currentList = new ArrayList<>();
                for (String s : changeTo.keySet()) {
                  currentList.add(s);
                }
                refresh(changeTo);
              } else {
                Platform.runLater(new Runnable() {
                  @Override
                  public void run() {
                    try {
                      new _FileView(String.format(((_Instrumenting.CURRENT_DIR) + "/src/main/java/%s.java"), item.getXValue().replaceAll("\\.", "/")), item.getXValue())
                        .start(new Stage());
                    } catch (Exception e) {
                      throw new RuntimeException(e);
                    }
                  }
                });
              }
            });
          }
        }
      }
    });
    bouton.setOnMouseClicked(new EventHandler<Event>() {
      @Override
      public void handle(Event arg0) {
        retMap = new HashMap<String, Integer>();
        current = current.substring(0, current.lastIndexOf("."));
        Map<String, Integer> changeTo = getPackageResult(current);
        changeTo.remove(current);
        currentList = new ArrayList<>();
        for (String s : changeTo.keySet()) {
          currentList.add(s);
        }
        refresh(changeTo);
      }
    });
  }

  private void refresh(Map<String, Integer> changeTo) {
    dataList.clear();
    classes.clear();
    _BarChart.barChartData.get(0).getData().clear();
    for (String s : changeTo.keySet()) {
      classes.add(s);
      XYChart.Data<String, Number> barTmp = new XYChart.Data<>();
      barTmp.setXValue(s);
      barTmp.setYValue(changeTo.get(s));
      _BarChart.barChartData.get(0).getData().add(barTmp);
      if (isClass(s)) {
        barTmp.getNode().setStyle("-fx-bar-fill: #6599FF;");
      }
    }
  }

  public boolean isToRefresh(String key) {
    return currentList.contains(key);
  }

  private boolean isClass(String s) {
    return _Instrumenting.lines.keySet().contains(s);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    init(primaryStage);
    primaryStage.show();
  }

  public Integer display(String className) {
    Float nbLineExecuted = 0.0F;
    Float nbLineTotal = 0.0F;
    for (Map.Entry<String, Map<Integer, Boolean>> entry : _Instrumenting.lines.entrySet()) {
      if (entry.getKey().startsWith(className)) {
        for (boolean isExecuted : entry.getValue().values()) {
          if (isExecuted) {
            nbLineExecuted++;
          }
        }
        nbLineTotal += entry.getValue().size();
      }
    }
    Float covergeRatio = nbLineExecuted / nbLineTotal;
    return Math.round((covergeRatio * 100));
  }

  public Integer displayForCLass(String className) {
    Map<Integer, Boolean> map = _Instrumenting.lines.get(className);
    Integer total = 0;
    Integer exec = 0;
    for (Integer i : map.keySet()) {
      if (map.get(i)) {
        ++exec;
      }
      ++total;
    }
    return ((int) ((((double) (exec)) / total) * 100));
  }

  Map<String, Integer> retMap = new HashMap<String, Integer>();

  public Map<String, Integer> getPackageResult(String packageName) {
    String commonPackage;
    if (packageName == null) {
      commonPackage = _BarChart.getLongestCommonPrefix(_Instrumenting.lines.keySet().toArray(new String[_Instrumenting.lines.keySet().size()]));
      commonPackage = commonPackage.substring(0, ((commonPackage.length()) - 1));
    } else {
      commonPackage = packageName;
    }
    Set<String> subDirectPackage = new HashSet<String>();
    for (Map.Entry<String, Map<Integer, Boolean>> entry : _Instrumenting.lines.entrySet()) {
      if ((entry.getKey().substring(0, entry.getKey().lastIndexOf(".")).contains(commonPackage))
        && ((entry.getKey().substring(0, entry.getKey().lastIndexOf(".")).split("\\.").length) > (commonPackage.split("\\.").length))) {
        subDirectPackage.add(String.format("%s.%s", commonPackage, entry.getKey().split("\\.")[commonPackage.split("\\.").length]));
      }
      if ((entry.getKey().contains(commonPackage)) && (((entry.getKey().split("\\.").length) - 1) == (commonPackage.split("\\.").length))) {
        retMap.put(entry.getKey(), displayForCLass(entry.getKey()));
      }
    }
    for (String s : subDirectPackage) {
      Float nbLineExecuted = 0.0F;
      Float nbLineTotal = 0.0F;
      for (Map.Entry<String, Map<Integer, Boolean>> entry : _Instrumenting.lines.entrySet()) {
        if (entry.getKey().startsWith(commonPackage)) {
          for (boolean isExecuted : entry.getValue().values()) {
            if (isExecuted) {
              nbLineExecuted++;
            }
          }
          nbLineTotal += entry.getValue().size();
        }
      }
      Float covergeRatio = nbLineExecuted / nbLineTotal;
      retMap.put(s, Math.round((covergeRatio * 100)));
    }
    return retMap;
  }

  private static String getLongestCommonPrefix(String[] strings) {
    if ((strings.length) == 0) {
      return "";
    }
    for (int prefixLen = 0; prefixLen < (strings[0].length()); prefixLen++) {
      char c = strings[0].charAt(prefixLen);
      for (int i = 1; i < (strings.length); i++) {
        if ((prefixLen >= (strings[i].length())) || ((strings[i].charAt(prefixLen)) != c)) {
          return strings[i].substring(0, prefixLen);
        }
      }
    }
    return strings[0];
  }

  public static void run() {
    Application.launch();
  }
}
