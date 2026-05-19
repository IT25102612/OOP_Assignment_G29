package com.tranquility.structures;

import com.tranquility.models.Reservation;
import java.util.ArrayList;

// ABSTRACTION — sorting logic is hidden from the rest of the app
public class ReservationSorter {

    // Public method called from DAO
    public void quickSort(ArrayList<Reservation> list, int low, int high) {
        if (low < high) {
            int pi = partition(list, low, high);
            quickSort(list, low, pi - 1);
            quickSort(list, pi + 1, high);
        }
    }

    // Private helper — hidden from outside
    private int partition(ArrayList<Reservation> list, int low, int high) {
        String pivot = list.get(high).getCheckinDate();
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (list.get(j).getCheckinDate().compareTo(pivot) < 0) {
                i++;
                Reservation temp = list.get(i);
                list.set(i, list.get(j));
                list.set(j, temp);
            }
        }

        Reservation temp = list.get(i + 1);
        list.set(i + 1, list.get(high));
        list.set(high, temp);
        return i + 1;
    }
}