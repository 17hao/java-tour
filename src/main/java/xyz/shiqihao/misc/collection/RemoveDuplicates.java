package xyz.shiqihao.misc.collection;

import java.util.*;

public class RemoveDuplicates {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(1);
        Set<Integer> set = new HashSet<>(list);
        for (Integer i : set) {
            System.out.println(i);
        }
    }
}
