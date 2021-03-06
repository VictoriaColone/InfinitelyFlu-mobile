package com.ximao.infinitelyflu_mobile.data;


/**
 * @author ximao
 * @date 2021/7/21
 * 模板数据类
 */
public class Template {

    private String name;
    private String version;

    public Template(String userId, String displayName) {
        this.name = userId;
        this.version = displayName;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }
}