package homework1.src;

import java.util.Arrays;
import java.util.Random;


public class MergeSort {

    public static void main(String[] args) {

        int[] input = new int[1000];
        Random rand = new Random();

        for (int i = 0; i < 1000; i++) {
            //populate the input[0...999] input with random numbers 0-5000
            input[i] = rand.nextInt(5000);
        }
      
        
        mergeSort(input);
        //System.out.println(Arrays.toString(input));

    }

    public static int[] mergeSort(int[] input) {
        if (input == null || input.length <= 1) {
            return input;
        }
        int[] buffer = new int[input.length];
        
        mergeSortRecursive(input, buffer, 0, input.length - 1);
        
        return input;
    }


    public static void mergeSortRecursive(int[] input, int[] buffer, int left, int right) {
        if (left>=right) return;

        int mid = left + (right-left)/2;
        mergeSortRecursive(input, buffer, left, mid);
        mergeSortRecursive(input, buffer, mid+1, right);
        merge(input, buffer, left, mid,right);

    }


    // avoid creation of new array in hot path -> memory complexity to eb O(n) instead of O(nlogn)
    private static void merge(int[] input, int[] buffer, int left, int mid, int right) {
        
        int i = left; 
        int j = mid+1;
        int k = left;

        while (i <=mid && j <=right) {
            if (input[i] <= input[j]) {
                buffer[k] = input[i];
                i++;
            } else {
                buffer[k] = input[j];
                j++;
            }
            k++;
        }


        while (i <=mid) {
            buffer[k] = input[i];
            i++;
            k++;
        }
        
        while (j <=right) {
            buffer[k] = input[j];
            j++;
            k++;
        }

        for (k=left;k<=right;k++){
            input[k]=buffer[k];
        }

        return;
    }
}

