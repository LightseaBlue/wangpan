package com.LightseaBlue.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * Description: UpListBean
 * Date: 2020/2/8
 * Time 15:10
 * Author: LightseaBlue
 * Version: 1.0 <br>
 */
public class UpListBean implements Serializable {
    private static final long serialVersionUID = -6289438840139823752L;
    boolean issucess;
    private String parentPath;
    private List<FileBean> list;

    public boolean isSucess() {
        return issucess;
    }

    public void setSucess(boolean sucess) {
        this.issucess = sucess;
    }

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    public List<FileBean> getList() {
        return list;
    }

    public void setList(List<FileBean> list) {
        this.list = list;
    }
}
