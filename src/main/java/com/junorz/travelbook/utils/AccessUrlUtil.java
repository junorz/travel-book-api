package com.junorz.travelbook.utils;

import java.util.Random;

import com.junorz.travelbook.context.orm.Repository;
import com.junorz.travelbook.domain.AccessUrl;

/**
 * Generate a unique URL for travel book access.<br>
 */
public class AccessUrlUtil {

    /**
     * Generate a string with 12 characters.
     * @return URL for travel book
     */
    public static String getUrl() {
        String bounded = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        int urlLength = 12;
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < urlLength; i++) {
            int index = random.nextInt(bounded.length());
            sb.append(bounded.charAt(index));
        }

        return sb.toString();
    }

    /**
     * Check if the url is unique in the database.
     * @param url Url
     * @param rep Repository
     * @return return true if the url is unique.
     */
    public static boolean checkUrlUnique(String url, Repository rep) {
        AccessUrl accessUrl = AccessUrl.findByUrl(url, rep);
        return accessUrl == null ? true : false;
    }

}
