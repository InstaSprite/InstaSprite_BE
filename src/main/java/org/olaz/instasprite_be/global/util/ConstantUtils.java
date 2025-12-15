package org.olaz.instasprite_be.global.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Common constants used throughout the application
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConstantUtils {

    // Pagination constants
    public static final int BASE_PAGE_NUMBER = 0; // A page starts at 0
    public static final int POST_INFINITY_SCROLL_SIZE = 1;
    public static final int PAGE_ADJUSTMENT_VALUE = 1; // The client requests from page 1
    
    // String processing constants
    public static final int HASHTAG_PREFIX_LENGTH = 1;
    public static final int MENTION_PREFIX_LENGTH = 1;
    public static final int ANY_INDEX = 0;
    
    // Numeric constants
    public static final long NONE = 0L;
    public static final int MAX_TAGS_PER_POST = 20;
    
    // String constants
    public static final String EMPTY = "";
    public static final String COMMA = ",";
    public static final String BLANK = " ";
    public static final String COMMA_WITH_BLANK = ", ";
    public static final String HASHTAG_PREFIX = "#";
    public static final String MENTION_PREFIX = "@";
}

