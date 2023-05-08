package cn.rjtech.util.excel;

import java.io.Serializable;
import java.util.List;

/**
 * 分sheet导出Excel的实体对象
 *
 * @author Kephon
 */
public class SheetPage<T> implements Serializable {

    private String sheetName;

    private Integer currentPage;

    private Integer totalPage;

    private Object master;

    private List<?> details;

    public SheetPage() {
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public Object getMaster() {
        return master;
    }

    public void setMaster(Object master) {
        this.master = master;
    }

    public List<?> getDetails() {
        return details;
    }

    public void setDetails(List<?> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "SheetPage{" +
                "sheetName='" + sheetName + '\'' +
                ", currentPage=" + currentPage +
                ", totalPage=" + totalPage +
                ", details=" + details +
                '}';
    }

}
