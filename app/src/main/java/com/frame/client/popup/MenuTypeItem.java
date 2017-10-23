package com.frame.client.popup;

/**
 * Created by Hongmingwei on 2017/9/13.
 * Email: 648600445@qq.com
 */

public class MenuTypeItem {

    private String text;
    private int resourceId;
    private int typeId;

    public MenuTypeItem(String text, int resourceId, int typeId){
        super();
        this.text = text;
        this.resourceId = resourceId;
        this.typeId = typeId;
    }

    public MenuTypeItem(String text){
        super();
        this.text = text;
    }

    public int getResourceId() {
        return resourceId;
    }

    public int getTypeId() {
        return typeId;
    }

    public String getText() {
        return text;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }
}

