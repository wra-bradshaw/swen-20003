package org.example.tute2;

public class Rectangle {
  private double left;
  private double top;
  private double width;
  private double height;

  public Rectangle() {
    this.left = 0;
    this.top = 0;
    this.width = 1;
    this.height = 1;
  }

  public Rectangle(double width, double height) {
    this.width = width;
    this.height = height;
  }

  public Rectangle(double width, double height, double left, double top) {
    this.width = width;
    this.height = height;
    this.left = left;
    this.top = top;
  }

  @Override
  public String toString() {
    return "Rectangle{ width: "
        + this.width
        + ", height: "
        + this.height
        + ", left: "
        + this.left
        + ", top: "
        + this.top
        + " }";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }

    Rectangle r = (Rectangle) o;
    return Double.compare(r.width, this.width) == 0
        && Double.compare(r.height, this.height) == 0
        && Double.compare(r.top, this.top) == 0
        && Double.compare(r.left, this.left) == 0;
  }
}
