package com.example.a76947.cmnet.model;

/**
 * Created by 76947 on 2017/12/13.
 */
import android.media.ImageWriter;
import android.widget.ImageView;

import java.io.Serializable;
import java.util.ArrayList;

public class CellInfo implements Serializable{

    private String Cellid;

    private String Celltitle;

    private String Cellurl;

    private String Cellpic;

    public String getCellid() {
        return Cellid;
    }

    public String getCellurl() {
        return Cellurl;
    }

    public String getCellpic() {
        return Cellpic;
    }

    public void setCellpic(String cellpic) {
        Cellpic = cellpic;
    }

    public String getCelltitle() {
        return Celltitle;
    }

    public void setCellid(String cellid) {
        Cellid = cellid;
    }

    public void setCellurl(String cellurl) {
        Cellurl = cellurl;
    }

    public void setCelltitle(String celltitle) {
        Celltitle = celltitle;
    }
}
