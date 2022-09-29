package com.ximao.infinitelyflu_mobile.utils.apm;


import androidx.annotation.NonNull;
import java.text.DecimalFormat;


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
        DecimalFormat decimalFormat= new DecimalFormat( "0.00" );
        return "max: " + decimalFormat.format(maxMemory) + "MB\n" + "total: " +
                decimalFormat.format(totalMemory) + "MB\n" + "free: " +
                decimalFormat.format(freeMemory) + "MB" ;
    }

}
