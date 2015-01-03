package ru.viqa.test_data_generator.generator;

import ru.viqa.test_data_generator.funcInterfaces.*;

/**
 * Created by 12345 on 28.12.2014.
 */
public class FilterData {
    public String[] whiteList;
    public String[] blackList;
    public FuncTT<String, Boolean> fieldsRule;
    public String[] availableGroups;
    public String breadcrumb = "*";

    public FilterData() { }

    public FilterData(String[] whiteList, String[] blackList, FuncTT<String, Boolean> fieldsRule,
                      String[] availableGroups, String breadcrumb)
    {
        this.whiteList = whiteList;
        this.blackList = blackList;
        this.fieldsRule = fieldsRule;
        this.availableGroups = availableGroups;
        this.breadcrumb = breadcrumb;
    }

    public FilterData(FilterData filterData, String fieldName) {
        this(filterData.whiteList, filterData.blackList, filterData.fieldsRule, filterData.availableGroups, filterData.breadcrumb + "." + fieldName);
    }

    public boolean allowGeneration() throws Exception {
        if (fieldsRule != null)
            return fieldsRule.invoke(breadcrumb);
        if (whiteList != null && whiteList.length > 0) {
            for(String value : whiteList)
                if (breadcrumb.contains(value))
                    return true;
            return false;
        }
        if (blackList != null && blackList.length > 0) {
            for(String value : blackList)
                if (breadcrumb.contains(value))
                    return false;
            return true;
        }
        return true;
    }

}
