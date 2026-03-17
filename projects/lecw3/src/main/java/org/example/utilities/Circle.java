package org.example.utilities;

public class Circle {
  public Point point;
  public double radius;
  public static int numCircles;

  public Circle() {
    this.point = new Point(10, 10);
    this.radius = 5.0;
    numCircles++;
  }

  public Circle(double newCentreX, double newCentreY, double newRadius) {
    this.point = new Point(newCentreX, newCentreY);
    radius = newRadius;
    numCircles++;
  }

  public Circle(double newCentreX, double newCentreY) {
    this.point = new Point(newCentreX, newCentreY);
    radius = 5.0;
  }

  public Circle(double newRadius) {
    this.point = new Point(10, 10);
    radius = newRadius;
  }

  public double getCentreX() {
    return this.point.getXCoord();
  }

  public void setCentreX(double centreX) {
    this.point.setXCoord(centreX);
  }

  public double getCentreY() {
    return this.point.getYCoord();
  }

  public void setCentreY(double centreY) {
    this.point.setYCoord(centreY);
  }

  public double getRadius() {
    return radius;
  }

  public void setRadius(double radius) {
    this.radius = radius;
  }

  public double computeCircumference() {
    double circum = 2 * Math.PI * radius;
    return circum;
  }

  public double computeArea() {
    double area = Math.PI * radius * radius;
    return area;
  }

  public void resize(double factor) {
    radius = radius * factor;
  }

  // Static method to count the number of circles
  public static void printNumCircles() {
    System.out.println("Number of circles = " + numCircles);
  }

  public boolean equals(Circle circle) {
    return this.point.equals(circle.point) && Double.compare(circle.radius, radius) == 0;
  }

  @Override
  public String toString() {
    return "I am a Circle with {" + "point=" + this.point + ", radius=" + radius + '}';
  }
}
