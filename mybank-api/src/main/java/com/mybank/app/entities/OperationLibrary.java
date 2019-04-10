package com.mybank.app.entities;

import java.util.List;
/**
 *
 * <h1> This entity containing an array of all operation and information related to  search requests</h1>
 *
 * */
public class OperationLibrary {

    private long totalCount;
    private boolean completeResult;
    private List<Operation> items;

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public boolean isCompleteResult() {
        return completeResult;
    }

    public void setCompleteResult(boolean completeResult) {
        this.completeResult = completeResult;
    }

    public List<Operation> getItems() {
        return items;
    }

    public void setItems(List<Operation> items) {
        this.items = items;
    }

    public OperationLibrary(long totalCount, boolean completeResult, List<Operation> items) {
        this.totalCount = totalCount;
        this.completeResult = completeResult;
        this.items = items;
    }

    @Override
    public String toString() {
        return "OperationLibrary{" +
                "totalCount=" + totalCount +
                ", completeResult=" + completeResult +
                ", items=" + items +
                '}';
    }
}
