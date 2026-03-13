import java.util.Objects;

// Circle.java
public class Circle {
    public double centreX, centreY, radius;
    public static int numCircles;

    public Circle() {
        centreX = 10.0;
        centreY = 10.0;
        radius = 5.0;
        numCircles++;

    }

    public Circle(double newCentreX, double newCentreY, double newRadius) {
        centreX = newCentreX;
        centreY = newCentreY;
        radius = newRadius;
        numCircles++;
    }

    public Circle(double newCentreX, double newCentreY) {
        centreX = newCentreX;
        centreY = newCentreY;
        radius = 5.0;
    }

    public Circle(double newRadius) {
        centreX = 10.0;
        centreY = 10.0;
        radius = newRadius;
    }
    public double getCentreX() {
        return centreX;
    }
    public void setCentreX(double centreX) {
        this.centreX = centreX;
    }
    public double getCentreY() {
        return centreY;
    }
    public void setCentreY(double centreY) {
        this.centreY = centreY;
    }
    public double getRadius() {
        return radius;
    }
    public void setRadius(double radius) {
        this.radius = radius;
    }
    public double computeCircumference () {
        double circum = 2 * Math.PI * radius;
        return circum;
    }
    public double computeArea () {
        double area = Math.PI * radius * radius;
        return area;
    }
    public void resize (double factor) {
        radius = radius*factor;
    }

    // Static method to count the number of circles
    public static void printNumCircles() {
        System.out.println("Number of circles = " + numCircles);
    }


    public boolean equals(Circle circle) {
        return Double.compare(circle.centreX, centreX) == 0 &&
                Double.compare(circle.centreY, centreY) == 0 &&
                Double.compare(circle.radius, radius) == 0;
    }


    @Override
    public String toString() {
            return "I am a Circle with {" +
                    "centreX=" + centreX +
                   ", centreY=" + centreY +
                    ", radius=" + radius +
                    '}';
    }

}
