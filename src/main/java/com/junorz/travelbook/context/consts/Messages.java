package com.junorz.travelbook.context.consts;

public interface Messages {

    // TravelBook
    public static String FETCH_TRAVELBOOKS_SUCCESS = "FETCH_TRAVELBOOKS_SUCCESS";
    public static String FETCH_TRAVELBOOK_BY_URL_SUCCESS = "FETCH_TRAVELBOOK_BY_URL_SUCCESS";
    public static String TRAVELBOOK_CREATE_SUCCESS = "TRAVELBOOK_CREATE_SUCCESS";
    public static String TRAVELBOOK_CREATE_FAILED = "TRAVELBOOK_CREATE_FAILED";
    public static String TRAVELBOOK_EDIT_SUCCESS = "TRAVELBOOK_EDIT_SUCCESS";
    public static String TRAVELBOOK_DELETE_SUCCESS = "TRAVELBOOK_DELETE_SUCCESS";
    public static String TRAVELBOOK_ID_NOT_SPECIFIED = "TRAVELBOOK_ID_NOT_SPECIFIED";
    public static String TRAVELBOOK_ID_INVALID = "TRAVELBOOK_ID_INVALID";
    public static String NO_TRAVELBOOK_FOUND = "NO_TRAVELBOOK_FOUND";
    public static String AUTHENTICATION_SUCCESS = "AUTHENTICATION_SUCCESS";
    public static String AUTHENTICATION_FAILED = "AUTHENTICATION_FAILED";

    // Member
    public static String MEMBER_CREATE_SUCCESS = "MEMBER_CREATE_SUCCESS";
    public static String MEMBER_EDIT_SUCCESS = "MEMBER_EDIT_SUCCESS";
    public static String MEMBER_EDIT_FAILED = "MEMBER_EDIT_FAILED";
    public static String MEMBER_DELETE_SUCCESS = "MEMBER_DELETE_SUCCESS";
    public static String MEMBER_DELETE_FAILED = "MEMBER_DELETE_FAILED";
    public static String MEMBER_NOT_FOUND = "MEMBER_NOT_FOUND";

    // AccessUrl
    public static String NO_ACCESSURL_FOUND = "NO_ACCESSURL_FOUND";

    // Currency
    public static String EXCHANGE_RATE_FETCH_SUCCESS = "EXCHANGE_RATE_FETCH_SUCCESS";

    // Detail
    public static String DETAIL_CREATE_SUCCESS = "DETAIL_CREATE_SUCCESS";
    public static String DETAIL_EDIT_SUCCESS = "DETAIL_EDIT_SUCCESS";
    public static String DETAIL_NOT_FOUND = "DETAIL_NOT_FOUND";
    public static String DETAIL_DELETE_SUCCESS = "DETAIL_DELETE_SUCCESS";
    
    // Primary Category
    public static String PRIMARY_CATEGORY_NOT_FOUND = "PRIMARY_CATEGORY_NOT_FOUND";
    
    // Secondary Category
    public static String SECONDARY_CATEGORY_NOT_FOUND = "SECONDARY_CATEGORY_NOT_FOUND";

    // System
    public static String BAD_REQUEST = "BAD_REQUEST";
    public static String FORBIDDEN = "FORBIDDEN";
    public static String INVALID_OPERATION = "INVALID_OPERATION";
    
    // Logger
    public static String LOG_AUTHENTICATION_SUCCESS = "LOG_AUTHENTICATION_SUCCESS";
    public static String LOG_TRAVELBOOK_CREATE_SUCCESS = "LOG_TRAVELBOOK_CREATE_SUCCESS";
    public static String LOG_MEMBER_CREATE_SUCCESS = "LOG_MEMBER_CREATE_SUCCESS";
}
