package com.flyco.tablayout.listener;

public interface OnTabSelectListener {
    /**
     * @param position
     * @return 消费掉，不让tab设置
     */
    boolean onTabSelect(int position);
    boolean onTabReselect(int position);
}