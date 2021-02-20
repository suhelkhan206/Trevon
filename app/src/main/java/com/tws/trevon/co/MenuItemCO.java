package com.tws.trevon.co;

import java.util.ArrayList;

/**
 * Created by chandra.kalwar on 8/19/2015.
 */

public class MenuItemCO
{
    public static ArrayList<MenuItemCO> menuItemList;
    public static final String PROFILE_MENU_ITEM = "1";
    public static final String PEOPLE_MENU_ITEM = "2";
    public static final String GROUP_MENU_ITEM = "3";
    public static final String PRODUCT_TOUR_MENU_ITEM = "4";
    public static final String SETTINGS_MENU_ITEM = "5";
    public static final String REPORT_PROBLEM_MENU_ITEM = "6";
    public static final String CONTACT_US_MENU_ITEM = "7";
    public static final String LOGOUT_MENU_ITEM = "8";
    public static final String HOME_MENU_ITEM = "9";
    public static final String INVITE_FRIENDS_MENU_ITEM = "10";

    public String code;
    public int iconRes;
    public int nameRes;
    public Boolean isSelected = Boolean.FALSE;

    public MenuItemCO(String code, int icon, int name)
    {
        this.code = code;
        this.iconRes = icon;
        this.nameRes = name;
        this.isSelected = Boolean.FALSE;
    }

    static
    {
        menuItemList = new ArrayList<>();

/*        menuItemList.add(new MenuItemCO(
                HOME_MENU_ITEM,
                R.drawable.ic_custom_feeds,
                R.string.nd_item_feeds));*/

     /*   menuItemList.add(new MenuItemCO(
                PROFILE_MENU_ITEM,
                R.drawable.ic_custom_profile_image,
                R.string.nd_item_profile));

        menuItemList.add(new MenuItemCO(
                PEOPLE_MENU_ITEM,
                R.drawable.ic_custom_people,
                R.string.nd_item_people));

        menuItemList.add(new MenuItemCO(
                GROUP_MENU_ITEM,
                R.drawable.ic_custom_groups,
                R.string.nd_item_group_list));

        menuItemList.add(new MenuItemCO(
                INVITE_FRIENDS_MENU_ITEM,
                R.drawable.ic_custom_profile_miss_you,
                R.string.nd_item_invite_friends));

        menuItemList.add(new MenuItemCO(
                SETTINGS_MENU_ITEM,
                R.drawable.ic_custom_settings,
                R.string.settings_string));

        menuItemList.add(new MenuItemCO(
                PRODUCT_TOUR_MENU_ITEM,
                R.drawable.ic_custom_product_tour,
                R.string.nd_item_product_tour));

        menuItemList.add(new MenuItemCO(
                REPORT_PROBLEM_MENU_ITEM,
                R.drawable.ic_custom_report_problem,
                R.string.nd_item_report_problem));

        menuItemList.add(new MenuItemCO(
                CONTACT_US_MENU_ITEM,
                R.drawable.ic_custom_mail,
                R.string.nd_item_contact_us));

        menuItemList.add(new MenuItemCO(
                LOGOUT_MENU_ITEM,
                R.drawable.ic_custom_logout,
                R.string.nd_item_logout));*/
    }

    public static void clearSelection()
    {
        for(MenuItemCO menuItemCO : menuItemList)
        {
            menuItemCO.isSelected = Boolean.FALSE;
        }
    }
}
