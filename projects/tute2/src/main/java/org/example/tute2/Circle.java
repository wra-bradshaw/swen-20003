package org.example.tute2;

// I used gemini to answer some questions I had while writing this
// https://gemini.google.com/share/9dad6d982ba6

public class Circle {
  private double radius;
  private double x;
  private double y;

  public Circle() {
    this.radius = 1;
    this.x = 0;
    this.y = 0;
  }

  public Circle(double radius) {
    this.radius = radius;
  }

  public Circle(double radius, double x, double y) {
    this.radius = radius;
    this.x = x;
    this.y = y;
  }

  @Override
  public String toString() {
    return "Circle{ radius: " + this.radius + ", x: " + this.x + ", y: " + this.y + " }";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }

    Circle c = (Circle) o;
    return Double.compare(c.radius, this.radius) == 0
        && Double.compare(c.x, this.x) == 0
        && Double.compare(c.y, this.y) == 0;
  }
}
