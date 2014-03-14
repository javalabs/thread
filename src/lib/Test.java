package lib;

import java.util.Random;

/**
 *  1. В данном массиве из 18 вещественных чисел. найти сумму элементов крупнее среднее арифметическое.
 */
public class Test {
    private static double[] array = new double[5];
    private static Random r = new Random();
    public static void main(String args[]) {

        for(int i = 0; i < array.length; ++i) {
            array[i] = r.nextDouble() * 100;
            System.out.print(array[i]+"   ");
        }

        double average, s = 0, sum = 0;

        for(int i = 0; i < array.length; ++i) {
            s += array[i];
        }

        average = s/array.length;

        for(int i = 0; i < array.length; ++i) {
            if(array[i] > average)
                sum += array[i];
        }
        System.out.println();
        System.out.println("Среднее арифметическое = "+average+" Сумма ="+sum);
    }
}
