package org.example.utilities;

public class Point {
  private double xCoord;
  private double yCoord;

  public Point(double xCoord, double yCoord) {
    this.xCoord = xCoord;
    this.yCoord = yCoord;
  }

  public double getXCoord() {
    return this.xCoord;
  }

  public double getYCoord() {
    return this.yCoord;
  }

  public void setXCoord(double xCoord) {
    this.xCoord = xCoord;
  }

  public void setYCoord(double yCoord) {
    this.yCoord = yCoord;
  }

  public boolean equals(Point point) {
    return Double.compare(point.xCoord, this.xCoord) == 0
        && Double.compare(point.yCoord, this.yCoord) == 0;
  }

  @Override
  public String toString() {
    return "I am a Point with {" + "xCoord=" + xCoord + ", yCoord=" + yCoord + '}';
  }
}
