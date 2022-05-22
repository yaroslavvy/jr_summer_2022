package com.game.service;

import java.util.List;

public class SearchCriteria {

    private String key;
    private SearchOperation searchOperation;
    private String value;
    private List<String> values;

    public SearchCriteria(String key, SearchOperation searchOperation, String value) {
        this.key = key;
        this.searchOperation = searchOperation;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public SearchOperation getSearchOperation() {
        return searchOperation;
    }

    public void setSearchOperation(SearchOperation searchOperation) {
        this.searchOperation = searchOperation;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }
}