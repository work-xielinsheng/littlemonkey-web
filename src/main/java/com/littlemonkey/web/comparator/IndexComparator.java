package com.littlemonkey.web.comparator;

import com.littlemonkey.utils.lang.Objects2;
import com.littlemonkey.web.annotation.Index;

import java.util.Comparator;

public class IndexComparator implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        Index target1 = Objects2.getAnnotation(o1, Index.class);
        Index target2 = Objects2.getAnnotation(o2, Index.class);
        return target1.value() > target2.value() ? -1 : (target1.value() == target2.value() ? 0 : 1);
    }
}
