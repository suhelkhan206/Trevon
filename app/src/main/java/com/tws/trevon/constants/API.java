/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tws.trevon.constants;

import com.tws.trevon.co.NetworkActivity;

/**
 *
 * @author chandra.kalwar
 */
public abstract class API
{
    public static final NetworkActivity login_with_otp;
    public static final NetworkActivity Registration;
    public static final NetworkActivity SingleProduct;
    public static final NetworkActivity GetCityStateByPincode;
    public static final NetworkActivity allCategoryBrands;

    public static final NetworkActivity MyWishList;
    public static final NetworkActivity MyOrder;
    public static final NetworkActivity view_cart;
    public static final NetworkActivity FilterBy;
    public static final NetworkActivity GetProductsByFilters;

    public static final NetworkActivity AddUserAddress;
    public static final NetworkActivity ADVERTISEMENT_SLIDER;
    public static final NetworkActivity allProducts;

    public static final NetworkActivity topBrands;
    public static final NetworkActivity updatePassword;
    public static final NetworkActivity AddToCart;
    public static final NetworkActivity recentViewedProducts;
    public static final NetworkActivity FeaturedProducts;
    public static final NetworkActivity DealOfTheDayProducts;
    public static final NetworkActivity TopRatedProducts;
    public static final NetworkActivity SaleProducts;
    public static final NetworkActivity upDateFCMToken;
    public static final NetworkActivity addUserBankDetail;
    public static final NetworkActivity addUserEmployementDetail;
    public static final NetworkActivity addUserLoanBasicDetail;
    public static final NetworkActivity UpdateUserDocumentDetail;
    public static final NetworkActivity GetUserDetail;
    public static final NetworkActivity CheckPincodeCod;
    public static final NetworkActivity ChangePassword;
    public static final NetworkActivity add_order;
    public static final NetworkActivity GetLoanSchemes;
    public static final NetworkActivity EmiCalculator;
    public static final NetworkActivity GetLoanApplications;
    public static final NetworkActivity get_notifications;
    public static final NetworkActivity SaveForgotPasssword;
    public static final NetworkActivity RemoveToCart;

    public static final NetworkActivity SliderBanner2;
    public static final NetworkActivity SliderBanner3;
    public static final NetworkActivity CATEGORY_SLIDER;
    public static final NetworkActivity GeTCategorySubcategory;
    public static final NetworkActivity SliderBanner1;
    public static final NetworkActivity SearchCityName;
    public static final NetworkActivity editUserProfile;
    public static final NetworkActivity CancelOrder;
    public static final NetworkActivity search_product;
    public static final NetworkActivity Refunded;
    public static final NetworkActivity DeleteUserAddress;
    public static final NetworkActivity returnProductRequest;

    // New Start From Here
    public static final NetworkActivity home_page_sliders;
    public static final NetworkActivity socialLogin;
    public static final NetworkActivity main_category_list;
    public static final NetworkActivity featuredCategory;
    public static final NetworkActivity featured_product_list;
    public static final NetworkActivity banner;
    public static final NetworkActivity populerProducts;
    public static final NetworkActivity user_registration;
    public static final NetworkActivity getProductbySubminiCategory;
    public static final NetworkActivity homePagecategorySubcategoryMinicategory;
    public static final NetworkActivity homePageSinglecategorySubcategoryMinicategory;
    public static final NetworkActivity singleProductDetails;
    public static final NetworkActivity my_address_list;
    public static final NetworkActivity add_new_address;
    public static final NetworkActivity userSharedCatalog;
    public static final NetworkActivity wishlist;
    public static final NetworkActivity forgotPassword;
    public static final NetworkActivity resatePassword;
    public static final NetworkActivity my_earning;
    public static final NetworkActivity AddToWishList;
    public static final NetworkActivity edit_bank_details;
    public static final NetworkActivity check_pincode;
    public static final NetworkActivity my_order_detail;
    public static final NetworkActivity add_to_wishList;
    public static final NetworkActivity allFilters;
    public static final NetworkActivity dealsOfTheDay;
    public static final NetworkActivity  applyCoupon;
    public static final NetworkActivity   orderStatusChange;
    public static final NetworkActivity   Faqs;

    public static final NetworkActivity   home_page_baner_1;
    public static final NetworkActivity   home_page_baner_2;
    public static final NetworkActivity   user_signup_with_doc;
    public static final NetworkActivity   most_selling_product_list;
    public static final NetworkActivity   all_level_category_list;
    public static final NetworkActivity   remove_wishList;
    public static final NetworkActivity   add_cart;
    public static final NetworkActivity   my_connection;
    public static final NetworkActivity   edit_profile;
    public static final NetworkActivity   edit_company_profile;
    public static final NetworkActivity   view_order_list;
    public static final NetworkActivity   category_baner;
    public static final NetworkActivity   deal_of_the_day_products;
    public static final NetworkActivity   order_status_change;
    public static final NetworkActivity   get_bank_details;
    public static final NetworkActivity   delete_address;
    public static final NetworkActivity   product_list;
    public static final NetworkActivity   setting;
    public static final NetworkActivity   sub_category_product_list;
    public static final NetworkActivity   resend_otp;
    public static final NetworkActivity   add_custom;
    public static final NetworkActivity   new_product_list;
    public static final NetworkActivity   redeem_request;
    public static final NetworkActivity   make_premium;





    static
    {
       /* login = new NetworkActivity("cmn_login");
        login.loginRequired = Boolean.FALSE;*/
        home_page_sliders = new NetworkActivity("home_page_sliders");
        home_page_sliders.loginRequired = Boolean.FALSE;

        banner = new NetworkActivity("banner");
        banner.loginRequired = Boolean.FALSE;

        socialLogin = new NetworkActivity("socialLogin");
        socialLogin.loginRequired = Boolean.FALSE;

        main_category_list = new NetworkActivity("main_category_list");
        main_category_list.loginRequired = Boolean.FALSE;

        featuredCategory = new NetworkActivity("featuredCategory");
        featuredCategory.loginRequired = Boolean.FALSE;

        featured_product_list = new NetworkActivity("featured_product_list");
        featured_product_list.loginRequired = Boolean.FALSE;

        populerProducts = new NetworkActivity("populerProducts");
        populerProducts.loginRequired = Boolean.FALSE;

        user_registration = new NetworkActivity("user_registration");
        user_registration.loginRequired = Boolean.FALSE;

        getProductbySubminiCategory = new NetworkActivity("getProductbySubminiCategory");
        getProductbySubminiCategory.loginRequired = Boolean.FALSE;

        homePagecategorySubcategoryMinicategory = new NetworkActivity("homePagecategorySubcategoryMinicategory");
        homePagecategorySubcategoryMinicategory.loginRequired = Boolean.FALSE;

        homePageSinglecategorySubcategoryMinicategory = new NetworkActivity("homePageSinglecategorySubcategoryMinicategory");
        homePageSinglecategorySubcategoryMinicategory.loginRequired = Boolean.FALSE;

        singleProductDetails = new NetworkActivity("single_product_detail");
        singleProductDetails.loginRequired = Boolean.FALSE;

        my_address_list = new NetworkActivity("my_address_list");
        my_address_list.loginRequired = Boolean.TRUE;

        add_new_address = new NetworkActivity("add_new_address");
        add_new_address.loginRequired = Boolean.TRUE;

        userSharedCatalog = new NetworkActivity("userSharedCatalog");
        userSharedCatalog.loginRequired = Boolean.TRUE;

        wishlist = new NetworkActivity("wishlist");
        wishlist.loginRequired = Boolean.TRUE;

        forgotPassword = new NetworkActivity("forgotPassword");
        forgotPassword.loginRequired = Boolean.FALSE;

        resatePassword = new NetworkActivity("resatePassword");
        resatePassword.loginRequired = Boolean.FALSE;

        my_earning = new NetworkActivity("my_earning");
        my_earning.loginRequired = Boolean.FALSE;

        AddToWishList = new NetworkActivity("AddToWishList");
        AddToWishList.loginRequired = Boolean.TRUE;

        edit_bank_details = new NetworkActivity("edit_bank_details");
        edit_bank_details.loginRequired = Boolean.TRUE;

        check_pincode = new NetworkActivity("check_pincode");
        check_pincode.loginRequired = Boolean.TRUE;

        my_order_detail = new NetworkActivity("my_order_detail");
        my_order_detail.loginRequired = Boolean.TRUE;

        add_to_wishList = new NetworkActivity("add_to_wishList");
        add_to_wishList.loginRequired = Boolean.TRUE;

        allFilters = new NetworkActivity("allFilters");
        allFilters.loginRequired = Boolean.TRUE;

        dealsOfTheDay = new NetworkActivity("dealsOfTheDay");
        dealsOfTheDay.loginRequired = Boolean.TRUE;

        applyCoupon = new NetworkActivity("applyCoupon");
        applyCoupon.loginRequired = Boolean.TRUE;

        orderStatusChange = new NetworkActivity("orderStatusChange");
        orderStatusChange.loginRequired = Boolean.TRUE;

        Faqs = new NetworkActivity("Faqs");
        Faqs.loginRequired = Boolean.TRUE;


        view_cart = new NetworkActivity("view_cart");
        view_cart.loginRequired = Boolean.TRUE;

        home_page_baner_1 = new NetworkActivity("home_page_baner_1");
        home_page_baner_1.loginRequired = Boolean.TRUE;

        home_page_baner_2 = new NetworkActivity("home_page_baner_2");
        home_page_baner_2.loginRequired = Boolean.TRUE;

        user_signup_with_doc = new NetworkActivity("user_signup_with_doc");
        user_signup_with_doc.loginRequired = Boolean.TRUE;

        most_selling_product_list = new NetworkActivity("most_selling_product_list");
        most_selling_product_list.loginRequired = Boolean.TRUE;

        all_level_category_list = new NetworkActivity("all_level_category_list");
        all_level_category_list.loginRequired = Boolean.TRUE;

        remove_wishList = new NetworkActivity("remove_wishList");
        remove_wishList.loginRequired = Boolean.TRUE;

        add_cart = new NetworkActivity("add_cart");
        add_cart.loginRequired = Boolean.TRUE;

        my_connection = new NetworkActivity("my_connection");
        my_connection.loginRequired = Boolean.TRUE;

        edit_profile = new NetworkActivity("edit_profile");
        edit_profile.loginRequired = Boolean.TRUE;

        edit_company_profile = new NetworkActivity("edit_company_profile");
        edit_company_profile.loginRequired = Boolean.TRUE;

        view_order_list = new NetworkActivity("view_order_list");
        view_order_list.loginRequired = Boolean.TRUE;

        category_baner = new NetworkActivity("category_baner");
        category_baner.loginRequired = Boolean.TRUE;

        deal_of_the_day_products = new NetworkActivity("deal_of_the_day_products");
        deal_of_the_day_products.loginRequired = Boolean.TRUE;

        order_status_change = new NetworkActivity("order_status_change");
        order_status_change.loginRequired = Boolean.TRUE;

        get_bank_details = new NetworkActivity("get_bank_details");
        get_bank_details.loginRequired = Boolean.TRUE;

        delete_address = new NetworkActivity("delete_address");
        delete_address.loginRequired = Boolean.TRUE;

        product_list = new NetworkActivity("product_list");
        product_list.loginRequired = Boolean.TRUE;

        setting = new NetworkActivity("setting");
        setting.loginRequired = Boolean.TRUE;

        sub_category_product_list = new NetworkActivity("sub_category_product_list");
        sub_category_product_list.loginRequired = Boolean.TRUE;

        resend_otp = new NetworkActivity("resend_otp");
        resend_otp.loginRequired = Boolean.TRUE;

        add_custom = new NetworkActivity("add_custom");
        add_custom.loginRequired = Boolean.TRUE;

        new_product_list = new NetworkActivity("new_product_list");
        new_product_list.loginRequired = Boolean.TRUE;

        redeem_request = new NetworkActivity("redeem_request");
        redeem_request.loginRequired = Boolean.TRUE;

        make_premium = new NetworkActivity("make_premium");
        make_premium.loginRequired = Boolean.TRUE;






















        login_with_otp = new NetworkActivity("login_with_otp");
        login_with_otp.loginRequired = Boolean.FALSE;

        Registration = new NetworkActivity("Registration");
        Registration.loginRequired = Boolean.FALSE;


        SingleProduct = new NetworkActivity("SingleProduct");
        SingleProduct.loginRequired = Boolean.FALSE;

        GetCityStateByPincode = new NetworkActivity("GetCityStateByPincode");
        GetCityStateByPincode.loginRequired = Boolean.FALSE;


        allCategoryBrands = new NetworkActivity("allCategoryBrands");
        allCategoryBrands.loginRequired = Boolean.FALSE;




        MyWishList = new NetworkActivity("MyWishList");
        MyWishList.loginRequired = Boolean.TRUE;

        MyOrder = new NetworkActivity("MyOrder");
        MyOrder.loginRequired = Boolean.TRUE;



        FilterBy = new NetworkActivity("FilterBy");
        FilterBy.loginRequired = Boolean.FALSE;

        GetProductsByFilters = new NetworkActivity("GetProductsByFilters");
        GetProductsByFilters.loginRequired = Boolean.FALSE;


        AddUserAddress = new NetworkActivity("AddUserAddress");
        AddUserAddress.loginRequired = Boolean.FALSE;

        ADVERTISEMENT_SLIDER = new NetworkActivity("SliderAdvertisement");
        ADVERTISEMENT_SLIDER.loginRequired = Boolean.FALSE;

        SliderBanner2 = new NetworkActivity("SliderBanner2");
        SliderBanner2.loginRequired = Boolean.FALSE;

        SliderBanner1 = new NetworkActivity("SliderBanner1");
        SliderBanner1.loginRequired = Boolean.FALSE;


        SliderBanner3 = new NetworkActivity("SliderBanner3");
        SliderBanner3.loginRequired = Boolean.FALSE;

        CATEGORY_SLIDER = new NetworkActivity("SliderCategory");
        CATEGORY_SLIDER.loginRequired = Boolean.FALSE;



        recentViewedProducts = new NetworkActivity("recentViewedProducts");
        recentViewedProducts.loginRequired = Boolean.TRUE;

        allProducts = new NetworkActivity("allProducts");
        allProducts.loginRequired = Boolean.FALSE;



        topBrands = new NetworkActivity("topBrands");
        topBrands.loginRequired = Boolean.FALSE;


        updatePassword = new NetworkActivity("updatePassword");
        updatePassword.loginRequired = Boolean.TRUE;

        AddToCart = new NetworkActivity("AddToCart");
        AddToCart.loginRequired = Boolean.TRUE;

        FeaturedProducts = new NetworkActivity("FeaturedProducts");
        FeaturedProducts.loginRequired = Boolean.FALSE;

        DealOfTheDayProducts = new NetworkActivity("DealOfTheDayProducts");
        DealOfTheDayProducts.loginRequired = Boolean.FALSE;

        TopRatedProducts = new NetworkActivity("TopRatedProducts");
        TopRatedProducts.loginRequired = Boolean.FALSE;

        SaleProducts = new NetworkActivity("SaleProducts");
        SaleProducts.loginRequired = Boolean.FALSE;


        upDateFCMToken = new NetworkActivity("upDateFCMToken");
        upDateFCMToken.loginRequired = Boolean.FALSE;

        addUserBankDetail = new NetworkActivity("addUserBankDetail");
        addUserBankDetail.loginRequired = Boolean.FALSE;

        addUserEmployementDetail = new NetworkActivity("addUserEmployementDetail");
        addUserEmployementDetail.loginRequired = Boolean.FALSE;

        addUserLoanBasicDetail = new NetworkActivity("addUserLoanBasicDetail");
        addUserLoanBasicDetail.loginRequired = Boolean.FALSE;

        UpdateUserDocumentDetail = new NetworkActivity("UpdateUserDocumentDetail");
        UpdateUserDocumentDetail.loginRequired = Boolean.FALSE;

        GetUserDetail = new NetworkActivity("GetUserDetail");
        GetUserDetail.loginRequired = Boolean.FALSE;

        CheckPincodeCod = new NetworkActivity("CheckPincodeCod");
        CheckPincodeCod.loginRequired = Boolean.FALSE;

        ChangePassword = new NetworkActivity("ChangePassword");
        ChangePassword.loginRequired = Boolean.FALSE;

        add_order = new NetworkActivity("add_order");
        add_order.loginRequired = Boolean.FALSE;

        GetLoanSchemes = new NetworkActivity("GetLoanSchemes");
        GetLoanSchemes.loginRequired = Boolean.FALSE;

        EmiCalculator = new NetworkActivity("EmiCalculator");
        EmiCalculator.loginRequired = Boolean.FALSE;

        GetLoanApplications = new NetworkActivity("GetLoanApplications");
        GetLoanApplications.loginRequired = Boolean.FALSE;


        get_notifications = new NetworkActivity("get_notifications");
        get_notifications.loginRequired = Boolean.FALSE;

        SaveForgotPasssword = new NetworkActivity("SaveForgotPasssword");
        SaveForgotPasssword.loginRequired = Boolean.FALSE;

        RemoveToCart = new NetworkActivity("RemoveToCart");
        RemoveToCart.loginRequired = Boolean.FALSE;

        GeTCategorySubcategory = new NetworkActivity("GeTCategorySubcategory");
        GeTCategorySubcategory.loginRequired = Boolean.FALSE;

        SearchCityName = new NetworkActivity("SearchCityName");
        SearchCityName.loginRequired = Boolean.FALSE;

        editUserProfile = new NetworkActivity("editUserProfile");
        editUserProfile.loginRequired = Boolean.FALSE;

        CancelOrder = new NetworkActivity("CancelOrder");
        CancelOrder.loginRequired = Boolean.FALSE;

        search_product = new NetworkActivity("search_product");
        search_product.loginRequired = Boolean.FALSE;

        Refunded = new NetworkActivity("Refunded");
        Refunded.loginRequired = Boolean.FALSE;

        DeleteUserAddress = new NetworkActivity("DeleteUserAddress");
        DeleteUserAddress.loginRequired = Boolean.FALSE;

        returnProductRequest = new NetworkActivity("returnProductRequest");
        returnProductRequest.loginRequired = Boolean.TRUE;





    }
}
