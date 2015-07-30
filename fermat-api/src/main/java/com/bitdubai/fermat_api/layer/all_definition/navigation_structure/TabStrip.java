package com.bitdubai.fermat_api.layer.all_definition.navigation_structure;


import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.interfaces.FermatTabStrip;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by rodrigo on 2015.07.17..
 */

public class TabStrip implements FermatTabStrip {

    /**
     * TabStrip class member variables
     */
    int dividerColor;

    int indicatorColor;

    int indicatorHeight;

    int backgroundColor;

    int textColor;

    int backgroundResource;

    String tabsColor;

    String tabsTextColor;

    String tabsIndicateColor;

    List<Tab> tabs = new ArrayList<>();

    public void addTab(Tab tab) {
        tabs.add(tab);
    }

    /**
     * TabStrip class Constructors
     */
    public TabStrip() {
    }

    public TabStrip(int dividerColor, int indicatorColor, int indicatorHeight, int backgroundColor, int textColor, int backgroundResource, String tabsColor, String tabsTextColor, String tabsIndicateColor, List<Tab> tabs) {
        this.dividerColor = dividerColor;
        this.indicatorColor = indicatorColor;
        this.indicatorHeight = indicatorHeight;
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
        this.backgroundResource = backgroundResource;
        this.tabsColor = tabsColor;
        this.tabsTextColor = tabsTextColor;
        this.tabsIndicateColor = tabsIndicateColor;
        this.tabs = tabs;
    }

    /**
     * TabStrip class getters
     */

    @Override
    public int getDividerColor() {
        return dividerColor;
    }


    @Override
    public int getIndicatorColor() {
        return indicatorColor;
    }

    @Override
    public int getIndicatorHeight() {
        return indicatorHeight;
    }


    @Override
    public int getBackgroundColor() {
        return backgroundColor;
    }


    @Override
    public int getTextColor() {
        return textColor;
    }


    @Override
    public int getBackgroundResource() {
        return backgroundResource;
    }


    @Override
    public String getTabsColor() {
        return tabsColor;
    }


    @Override
    public String getTabsTextColor() {
        return tabsTextColor;
    }


    @Override
    public String getTabsIndicateColor() {
        return tabsIndicateColor;
    }


    @Override
    public List<Tab> getTabs() {
        return tabs;
    }

    /**
     * TabStrip class setters
     */
    @Override
    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
    }

    @Override
    public void setIndicatorColor(int indicatorColor) {
        this.indicatorColor = indicatorColor;
    }

    @Override
    public void setIndicatorHeight(int indicatorHeight) {
        this.indicatorHeight = indicatorHeight;
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    @Override
    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    @Override
    public void setBackgroundResource(int backgroundResource) {
        this.backgroundResource = backgroundResource;
    }

    @Override
    public void setTabsColor(String tabsColor) {
        this.tabsColor = tabsColor;
    }

    @Override
    public void setTabsTextColor(String tabsTextColor) {
        this.tabsTextColor = tabsTextColor;
    }

    @Override
    public void setTabsIndicateColor(String tabsIndicateColor) {
        this.tabsIndicateColor = tabsIndicateColor;
    }

    public void setTabs(List<Tab> tabs) {
        this.tabs = tabs;
    }
}
