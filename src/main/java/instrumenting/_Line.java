package instrumenting;

import java.io.Serializable;

public class _Line implements Comparable<_Line>, Serializable {
  private static final long serialVersionUID = 5592667711895960185L;
  int position;
  boolean executed;

  public _Line(int position) {
    super();
    this.position = position;
    this.executed = false;
  }

  @Override
  public String toString() {
    return "Line [position=" + position + ", executed=" + executed + "]";
  }

  public int compareTo(_Line o) {
    return position - o.position;
  }

  public int getPosition() {
    return position;
  }

  public void setPosition(int position) {
    this.position = position;
  }

  public boolean isExecuted() {
    return executed;
  }

  public void setExecuted(boolean executed) {
    this.executed = executed;
  }

}
