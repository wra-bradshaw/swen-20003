package org.example.lecw4;

public class App {
  public static void main(String[] args) {
    System.out.println("Hello World!");
  }

  public static double[] computeDoublePowers(int size) {
    double[] nums = new double[size];

    for (int i = 0; i < nums.length; i++) {
      nums[i] = Math.pow(2, i);
    }

    return nums;
  }

  public static int[][] triangleArray(int size) {
    int[][] arr = new int[size][];

    for (int i = 0; i < arr.length; i++) {
      arr[i] = new int[size - i];

      for (int j = 0; j < arr[i].length; j++) {
        arr[i][j] = i + j + 1;
      }
    }

    return arr;
  }
}
