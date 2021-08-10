package com.ximao.infinitelyflu_mobile.utils.apm;


import androidx.annotation.NonNull;


/**
 * @author ximao
 * @date 2021/8/9
 * memory数据类
 */
public class MemoryInfo {

    /**
     * 最大分配内存
     */
    private float maxMemory;
    /**
     * 当前分配内存
     */
    private float totalMemory;
    /**
     * 还可使用内存
     */
    private float freeMemory;

    public void setMaxMemory(float maxMemory) {
        this.maxMemory = maxMemory;
    }

    public void setTotalMemory(float totalMemory) {
        this.totalMemory = totalMemory;
    }

    public void setFreeMemory(float freeMemory) {
        this.freeMemory = freeMemory;
    }

    @NonNull
    @Override
    public String toString() {
        return "max: " + maxMemory + "MB\n" + "total: " + totalMemory + "MB\n" + "free: " + freeMemory+ "MB" ;
    }
}
