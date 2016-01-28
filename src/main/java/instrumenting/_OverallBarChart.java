package instrumenting;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import com.sun.javafx.charts.Legend;
import com.sun.javafx.charts.Legend.LegendItem;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class _OverallBarChart extends Application {
  private BarChart<String, Number> chart;

  private Map<String, Map<String, Integer>> retMap;

  private Random random;

  private void init(Stage primaryStage) {
    random = new Random();
    retMap = getAllSubPackages(Optional.empty());
    StackPane root = new StackPane();
    primaryStage.setScene(new Scene(root));
    primaryStage.setTitle("CoCoSpoon");

    Series<String, Number> series = new Series<>();
    List<LegendItem> legendItems = new ArrayList<>();
    CategoryAxis xAxis = new CategoryAxis();
    xAxis.setTickLabelsVisible(false);
    NumberAxis yAxis = new NumberAxis("Coverage (%)", 0.0, 100.0, 5.0);
    chart = new BarChart<>(xAxis, yAxis);
    root.getChildren().add(chart);

    retMap.entrySet().forEach(entry -> {
      Color c = Color.hsb(
        random.nextDouble() * 360, // random hue, color
        1.0d, // full saturation, 1.0 for 'colorful' colors, 0.0 for grey
        1.0d // 1.0 for bright, 0.0 for black
      );
      entry.getValue().keySet().forEach(key -> {
        Data<String, Number> data = new Data<>(key, 0, String.format("#%02x%02x%02x",
          (int) (c.getRed() * 255),
          (int) (c.getGreen() * 255),
          (int) (c.getBlue() * 255)));
        series.getData().add(data);
      });
      legendItems.add(new Legend.LegendItem(entry.getKey(), new Rectangle(10, 4, c)));
    });

    chart.getData().add(series);

    chart.setAnimated(false);
    chart.setTitle("Real time code coverage");

    installTooltip(chart.getData().get(0).getData());
    installListener(_Instrumenting.lines.keySet());

    Legend legend = (Legend) chart.lookup(".chart-legend");
    legend.getItems().clear();
    legend.getItems().addAll(legendItems);

  }

  private void installListener(Set<String> keySet) {
    for (String map : keySet) {
      ObservableMap<Integer, Boolean> observedMap = FXCollections.observableMap(_Instrumenting.lines.get(map));
      observedMap.addListener(new MapChangeListener<Integer, Boolean>() {
        @Override
        public void onChanged(MapChangeListener.Change<? extends Integer, ? extends Boolean> changed) {
          for (Entry<String, Map<String, Integer>> entry : retMap.entrySet()) {
            for (String s2 : entry.getValue().keySet()) {
              for (Data<String, Number> data : chart.getData().get(0).getData()) {
                if (s2.contains(data.getXValue())) {
                  data.setYValue(computeCoverageForClass(s2));
                  data.getNode().setStyle("-fx-background-color: " + data.getExtraValue());
                }
              }
            }

          }
        }
      });
      _Instrumenting.lines.put(map, observedMap);
    }
  }

  private void installTooltip(ObservableList<Data<String, Number>> serie) {
    for (Data<String, Number> data : serie) {
      Tooltip tooltip = new Tooltip(data.getXValue());
      hackTooltipStartTiming(tooltip);
      Tooltip.install(data.getNode(), tooltip);
    }
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    init(primaryStage);
    primaryStage.show();
  }

  private Integer computeCoverageForClass(String className) {
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

  private Map<String, Map<String, Integer>> getAllSubPackages(Optional<String> optionalPackageName) {
    String commonPackage = optionalPackageName.orElseGet(() -> {
      String tmp = getLongestCommonPrefix(_Instrumenting.lines.keySet().toArray(new String[_Instrumenting.lines.keySet().size()]));
      return tmp.substring(0, ((tmp.length()) - 1));
    });
    Map<String, Map<String, Integer>> subDirectPackages = new HashMap<>();
    for (Map.Entry<String, Map<Integer, Boolean>> entry : _Instrumenting.lines.entrySet()) {
      if ((entry.getKey().substring(0, entry.getKey().lastIndexOf(".")).contains(commonPackage))
        && ((entry.getKey().substring(0, entry.getKey().lastIndexOf(".")).split("\\.").length) > (commonPackage.split("\\.").length))) {
        String subDirectPackage = String.format("%s.%s", commonPackage, entry.getKey().split("\\.")[commonPackage.split("\\.").length]);
        subDirectPackages.put(subDirectPackage, getAllClasses(Optional.of(subDirectPackage), Optional.empty()));
      }
    }
    return subDirectPackages;
  }

  private Map<String, Integer> getAllClasses(Optional<String> optionalPackageName, Optional<Map<String, Integer>> optionalRetMap) {
    Map<String, Integer> retMap = optionalRetMap.orElseGet(() -> new HashMap<String, Integer>());
    String commonPackage = optionalPackageName.orElseGet(() -> {
      String tmp = getLongestCommonPrefix(_Instrumenting.lines.keySet().toArray(new String[_Instrumenting.lines.keySet().size()]));
      return tmp.substring(0, ((tmp.length()) - 1));
    });

    Set<String> subDirectPackage = new HashSet<String>();
    for (Map.Entry<String, Map<Integer, Boolean>> entry : _Instrumenting.lines.entrySet()) {
      if ((entry.getKey().substring(0, entry.getKey().lastIndexOf(".")).contains(commonPackage))
        && ((entry.getKey().substring(0, entry.getKey().lastIndexOf(".")).split("\\.").length) > (commonPackage.split("\\.").length))) {
        subDirectPackage.add(String.format("%s.%s", commonPackage, entry.getKey().split("\\.")[commonPackage.split("\\.").length]));
      }
      if ((entry.getKey().contains(commonPackage)) && (((entry.getKey().split("\\.").length) - 1) == (commonPackage.split("\\.").length))) {
        retMap.put(entry.getKey(), computeCoverageForClass(entry.getKey()));
      }
    }
    for (String s : subDirectPackage) {
      getAllClasses(Optional.of(s), Optional.of(retMap));
    }
    return retMap;
  }

  private String getLongestCommonPrefix(String[] strings) {
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

  public void hackTooltipStartTiming(Tooltip tooltip) {
    try {
      Field fieldBehavior = tooltip.getClass().getDeclaredField("BEHAVIOR");
      fieldBehavior.setAccessible(true);
      Object objBehavior = fieldBehavior.get(tooltip);

      Field fieldTimer = objBehavior.getClass().getDeclaredField("activationTimer");
      fieldTimer.setAccessible(true);
      Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);

      objTimer.getKeyFrames().clear();
      objTimer.getKeyFrames().add(new KeyFrame(new Duration(250)));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
