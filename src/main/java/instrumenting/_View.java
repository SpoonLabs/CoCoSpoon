package instrumenting;

import java.util.Timer;

public class _View {

  public enum View {
    TEXT() {
      @Override
      public void start() {
        new Thread(new Runnable() {
          @Override
          public void run() {
            Timer timer = new Timer();
            timer.schedule(new _TextView(), 0, 1000);
          }
        }).start();
      }
    },
    OVERALL() {
      @Override
      public void start() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
          @Override
          public void run() {
            while (true) {
            }
          }
        }, "Shutdown-thread"));
        new Thread(new Runnable() {
          @Override
          public void run() {
            _OverallBarChart.run();
          }
        }).start();
      }
    },
    INTERACTIVE() {

      @Override
      public void start() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
          @Override
          public void run() {
            while (true) {
            }
          }
        }, "Shutdown-thread"));
        new Thread(new Runnable() {
          @Override
          public void run() {
            _BarChart.run();
          }
        }).start();
      }
    };

    public abstract void start();
  }

}
