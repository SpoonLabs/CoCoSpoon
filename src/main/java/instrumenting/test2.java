package instrumenting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**

 * Copyright (c) 2008, 2012 Oracle and/or its affiliates.

 * All rights reserved. Use is subject to license terms.

 */

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**

 * A chart that displays rectangular bars with heights indicating data values

 * for categories. Used for displaying information when at least one axis has

 * discontinuous or discrete data.

 *

 * @see javafx.scene.chart.BarChart

 * @see javafx.scene.chart.Chart

 * @see javafx.scene.chart.Axis

 * @see javafx.scene.chart.CategoryAxis

 * @see javafx.scene.chart.NumberAxis

 *

 */

public class test2 extends Application {

  private ObservableList<BarChart.Series<String, Number>> barChartData;
  private ArrayList<String> classes;
  Map<String, Map<Integer, Boolean>> classMap = new HashMap<String, Map<Integer, Boolean>>();
  List<String> currentList;
  List<BarChart.Data> dataList;
  BarChart chart;
  String current = "";

  @SuppressWarnings({"unchecked", "rawtypes"})
  private void init(Stage primaryStage) {
    currentList = new ArrayList<>();

    Map tmp1 = new HashMap<Integer, Boolean>();
    tmp1.put(1, true);
    tmp1.put(2, false);
    tmp1.put(3, false);
    tmp1.put(4, false);

    Map tmp2 = new HashMap<Integer, Boolean>();
    tmp2.put(1, false);
    tmp2.put(2, true);
    tmp2.put(3, true);
    tmp2.put(4, true);

    Map tmp3 = new HashMap<Integer, Boolean>();
    tmp3.put(1, true);
    tmp3.put(2, false);
    tmp3.put(3, false);
    tmp3.put(4, false);

    Map tmp4 = new HashMap<Integer, Boolean>();
    tmp4.put(1, true);
    tmp4.put(2, false);
    tmp4.put(3, false);
    tmp4.put(4, false);

    classMap.put("org.fr.fil.iagl.pack1.yolo1.class1", tmp1);
    classMap.put("org.fr.fil.iagl.pack1.yolo2.class1", tmp3);
    classMap.put("org.fr.fil.iagl.pack1.class2", tmp2);

    classMap.put("org.fr.fil.iagl.pack1.autrePack.class3", tmp3);
    classMap.put("org.fr.fil.iagl.class4", tmp4);

    Group root = new Group();
    primaryStage.setScene(new Scene(root));

    // Init de la classe la plus haute dans la hierarchie
    classes = new ArrayList<String>();
    String tmp = getLongestCommonPrefix(classMap.keySet().toArray(new String[classMap.keySet().size()]));
    if (tmp.charAt(tmp.length() - 1) == '.') {
      tmp = tmp.substring(0, tmp.length() - 1);
    }
    classes.add(tmp);
    currentList.add(tmp);
    current = tmp;

    CategoryAxis xAxis = new CategoryAxis();

    xAxis.setCategories(FXCollections.<String>observableArrayList(classes));

    NumberAxis yAxis = new NumberAxis("pourcentage de couverture", 0.0d, 100.0d, 5.0d);

    dataList = new ArrayList<>();

    BarChart.Data data = new BarChart.Data(classes.get(0), 25);

    dataList.add(data);

    barChartData = FXCollections.observableArrayList(

      new BarChart.Series("Code coverage", FXCollections.observableArrayList(

        dataList

    )));

    chart = new BarChart(xAxis, yAxis, barChartData, 25.0d);
    chart.getXAxis().setAutoRanging(true);
    chart.setAnimated(true);
    chart.getYAxis().setAutoRanging(true);
    root.getChildren().add(chart);
    Button bouton = new Button("Retour");
    root.getChildren().add(bouton);

    for (String map : classMap.keySet()) {
      ObservableMap<Integer, Boolean> observedMap = FXCollections.observableMap(classMap.get(map));
      observedMap.addListener(new MapChangeListener<Integer, Boolean>() {

        public void onChanged(javafx.collections.MapChangeListener.Change<? extends Integer, ? extends Boolean> changed) {
          System.out.println(changed);
          System.out.println(currentList);
          System.out.println(barChartData.get(0).getData());

          for (String s : currentList) {
            for (Data<String, Number> data : barChartData.get(0).getData()) {
              if (s.contains(data.getXValue())) {
                data.setYValue(display(s));
              }
            }
          }

        }

      });
      classMap.put(map, observedMap);
    }

    chart.setOnMouseClicked(new EventHandler<Event>() {
      @Override
      public void handle(Event event) {

        // opti dans cette boucle ?
        for (Series<String, Number> serie : barChartData) {
          for (XYChart.Data<String, Number> item : serie.getData()) {
            item.getNode().setOnMousePressed((Event event2) -> {
              System.out.println("you clicked " + item.getXValue());
              retMap = new HashMap<String, Integer>();
              current = item.getXValue();
              Map<String, Integer> changeTo = getPackageResult(item.getXValue());
              changeTo.remove(item.getXValue());

              currentList = new ArrayList<>();
              for (String s : changeTo.keySet()) {
                currentList.add(s);
              }

              refresh(changeTo);
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
    // observedMap.remove("org.fr.fil.iagl.class4");
    // observedMap.put("org.fr.fil.iagl.class4", tmp3);
    // classMap.get("org.fr.fil.iagl.class4").remove(1);
    Thread thread = new Thread() {

      public void run() {
        while (true) {
          if (classMap.get("org.fr.fil.iagl.class4").get(1) == true) {
            classMap.get("org.fr.fil.iagl.class4").put(1, false);
          } else {
            classMap.get("org.fr.fil.iagl.class4").put(1, true);
          }
          // System.out.println(classMap);
          try {
            Thread.sleep(500); // 1 second
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    };
    thread.start();
  }

  private void refresh(Map<String, Integer> changeTo) {
    System.out.println(changeTo);
    dataList.clear();
    classes.clear();
    // currentList.clear();
    // observedMap.clear();
    // classMap.clear();
    barChartData.get(0).getData().clear();
    for (String s : changeTo.keySet()) {
      classes.add(s);
      // dataList.add(new BarChart.Data(s, changeTo.get(s)));
      barChartData.get(0).getData().add(new XYChart.Data<String, Number>(s, changeTo.get(s)));
    }

  }

  public boolean isToRefresh(String key) {
    return (currentList.contains(key));
  }

  @Override
  public void start(Stage primaryStage) throws Exception {

    init(primaryStage);
    primaryStage.show();
  }

  public Integer display(String className) {
    Float nbLineExecuted = 0f;
    Float nbLineTotal = 0f;
    for (Entry<String, Map<Integer, Boolean>> entry : classMap.entrySet()) {
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
    return Math.round(covergeRatio * 100);
  }

  public Integer displayForCLass(String className) {
    Map<Integer, Boolean> map = classMap.get(className);
    Integer total = 0;
    Integer exec = 0;
    for (Integer i : map.keySet()) {
      if (map.get(i)) {
        ++exec;
      }
      ++total;
    }
    return (int) ((double) exec / total * 100);
  }

  Map<String, Integer> retMap = new HashMap<String, Integer>();

  public Map<String, Integer> getPackageResult(String packageName) {
    String commonPackage;
    if (packageName == null) {
      commonPackage = getLongestCommonPrefix(classMap.keySet().toArray(new String[classMap.keySet().size()]));
      commonPackage = commonPackage.substring(0, commonPackage.length() - 1);
    } else {
      commonPackage = packageName;
    }

    // Float nbLineExecuted = 0f;
    // Float nbLineTotal = 0f;
    // for (Entry<String, Map<Integer, Boolean>> entry : classMap.entrySet()) {
    // if (entry.getKey().startsWith(commonPackage)) {
    // for (boolean isExecuted : entry.getValue().values()) {
    // if (isExecuted) {
    // nbLineExecuted++;
    // }
    // }
    // nbLineTotal += entry.getValue().size();
    // }
    // }
    //
    // Float covergeRatio = nbLineExecuted / nbLineTotal;
    // // retMap.put(commonPackage, Math.round(covergeRatio * 100));
    // System.out.printf("%s -> %.2f%%\n", commonPackage, covergeRatio);

    // Calculer couverture du package
    Set<String> subDirectPackage = new HashSet<String>();
    for (Entry<String, Map<Integer, Boolean>> entry : classMap.entrySet()) {
      if (entry.getKey().substring(0, entry.getKey().lastIndexOf(".")).contains(commonPackage)
        && entry.getKey().substring(0, entry.getKey().lastIndexOf(".")).split("\\.").length > commonPackage.split("\\.").length) {
        subDirectPackage.add(String.format("%s.%s", commonPackage, entry.getKey().split("\\.")[commonPackage.split("\\.").length]));
      }
      if (entry.getKey().contains(commonPackage) && entry.getKey().split("\\.").length - 1 == commonPackage.split("\\.").length) {
        retMap.put(entry.getKey(), displayForCLass(entry.getKey()));
      }
    }
    for (String s : subDirectPackage) {
      Float nbLineExecuted = 0f;
      Float nbLineTotal = 0f;
      for (Entry<String, Map<Integer, Boolean>> entry : classMap.entrySet()) {
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
      // retMap.put(commonPackage, Math.round(covergeRatio * 100));
      // System.out.printf("%s -> %.2f%%\n", commonPackage, covergeRatio);
      retMap.put(s, Math.round(covergeRatio * 100));
    }
    // if (!subDirectPackage.isEmpty()) {
    // for (String subPackage : subDirectPackage) {
    // getPackageResult(subPackage);
    // }
    // }
    return retMap;
  }

  private static String getLongestCommonPrefix(String[] strings) {
    if (strings.length == 0) {
      return ""; // Or maybe return null?
    }

    for (int prefixLen = 0; prefixLen < strings[0].length(); prefixLen++) {
      char c = strings[0].charAt(prefixLen);
      for (int i = 1; i < strings.length; i++) {
        if (prefixLen >= strings[i].length() ||
          strings[i].charAt(prefixLen) != c) {
          // Mismatch found
          return strings[i].substring(0, prefixLen);
        }
      }
    }
    return strings[0];
  }

  public static void main(String[] args) {
    launch(args);
  }

}
