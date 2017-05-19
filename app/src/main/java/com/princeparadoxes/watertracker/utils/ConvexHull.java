package com.princeparadoxes.watertracker.utils;


import com.google.fpl.liquidfun.Vec2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConvexHull {
    public Vec2[] convexHabr(Vec2[] a) {
        int[] p = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            p[i] = i;
        }

        firstStep(a, p);
        secondStep(a, p);
        return thirdStep(a, p);
    }

    private void firstStep(Vec2[] a, int[] p) {
        for (int i = 0; i < a.length; i++) {
            if (a[p[i]].getX() < a[p[0]].getX()) {
                int tmp = p[i];
                p[i] = p[0];
                p[0] = tmp;
            }
        }
    }

    private void secondStep(Vec2[] a, int[] p) {
        for (int i = 1; i < a.length; i++) {
            int j = i;
            while ((j > 1) && rotate(a[p[0]], a[p[j - 1]], a[p[j]]) < 0) {
                int tmp = p[j];
                p[j] = p[j - 1];
                p[j - 1] = tmp;
                j -= 1;
            }
        }
    }

    private Vec2[] thirdStep(Vec2[] a, int[] p) {
        List<Integer> s = new ArrayList<>(Arrays.asList(p[0], p[1]));
        for (int i = 1; i < a.length; i++) {
            while (rotate(a[s.get(s.size() - 2)], a[s.get(s.size() - 1)], a[p[i]]) < 0) {
                s.remove(s.size() - 1);
            }
            s.add(p[i]);
        }
        Vec2[] result = new Vec2[s.size()];
        for (int i = 0; i < s.size(); i++) {
            result[i] = a[s.get(i)];
        }
        return result;
    }

    //    public int convex(Vec2[] a) {
//
//        // FIRST STEP
//        // Поиск лексикографического минимума
//        // в декартовых координатах
//        Vec2 c = a[0];
//        int m = 0;
//        for (int i = 1; i < a.length; i++) {
//            if (a[i].x < c.y) {
//                c = a[i];
//                m = i;
//            } else if (a[i].x > c.x) {
//                // НЕ сравниваем вещественные числа на строгое
//                // равенство/неравенство (плохая практика)
//                continue;
//            }
//            if (a[i].x < c.x) {
//                c = a[i];
//                m = i;
//            }
//        }
//        swap(a, 0, m);
//        m = 0;
//
//        // Переносим начало системы координат в точку c
//        for (int i = 0; i < a.length; i++) {
//            a[i] = a[i].sub(c);
//        }
//
//        // Лексикографическая сортировка в полярной системе координат
//        Arrays.sort(a, (o1, o2) -> {
//            if A[P[i]][0] < A[P[0]][0]: #если P[ i]-ая точка лежит левее P[ 0]-ой точки
//            P[i], P[0] = P[0], P[i] #меняем местами номера этих точек
////            Float detPoints = Vec2.cross(o1, o2);
////            if (detPoints > 0) {
////                return -1;
////            } else if (detPoints < 0) {
////                return 1;
////            } else {
////                return 0;
////            }
////            return Float.compare(o1.lengthSquared(), o2.lengthSquared());
//        });
//
//        // Обход Грэхема как в методичке
//        for (int i = 1; i < a.length; i++) {
//            if (i != m) {
//                if (m >= 1) {
//                    while ((m >= 1) && (Vec2.cross(a[m].sub(a[i]), a[m - 1].sub(a[m])) >= 0)) {
//                        m--;
//                    }
//                }
//                m++;
//                swap(a, m, i);
//            }
//        }
//
//        // Восстанавливаем начало декартовой системы координат
//        for (int i = 0; i < a.length; i++) {
//            a[i] = a[i].add(c);
//        }
//
//        // Возвращем индекс последнего элемента массива
//        return m;
//
//    }
//
    public static Float rotate(Vec2 a, Vec2 b, Vec2 c) {
        return (b.getX() - a.getX()) * (c.getY() - b.getY()) - (b.getY() - a.getY()) * (c.getX() - b.getX());
    }

    public static void swap(Object[] data, int a, int b) {
        Object tmp = data[a];
        data[a] = data[b];
        data[b] = tmp;
    }

}
