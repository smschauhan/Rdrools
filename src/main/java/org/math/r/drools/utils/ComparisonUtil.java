package org.math.r.drools.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@SuppressWarnings("unchecked")
public class ComparisonUtil {
    protected static final int EQ = 1;
    protected static final int NEQ = 2;
    protected static final int LT = 3;
    protected static final int LTE = 4;
    protected static final int GT = 5;
    protected static final int GTE = 6;
    
    protected static final String DATE_FORMAT = "yyyy-MM-dd";
    protected static final String TIME_FORMAT = "HH:mm:ss.SSS";
    protected static final String TIMESTAMP_FORMAT = DATE_FORMAT + " " + TIME_FORMAT;
    
    protected static SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
    protected static SimpleDateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);
    protected static SimpleDateFormat timestampFormat = new SimpleDateFormat(TIMESTAMP_FORMAT);
    protected static SimpleDateFormat isoDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    
    protected static SimpleDateFormat[] backupFormats = new SimpleDateFormat[] {
        isoDateFormat, timestampFormat, dateFormat, timeFormat
    };
    
    protected static Object convert(String string, String type) {
        if(type == null || string == null) {
            return null;
        }
        try {
            if(type.equals(String.class.getName())) {
                return string;
            } else if(type.equals(Character.class.getName())) {
                if( string.length() == 0) {
                    return null;
                } else {
                    return Character.valueOf(string.charAt(0));
                }
            } else if(type.equals(BigDecimal.class.getName())) {
                return new BigDecimal(string);
            } else if(type.equals(BigInteger.class.getName())) {
                return new BigInteger(string);
            } else if(type.equals(Double.class.getName())) {
                return Double.valueOf(string);
            } else if(type.equals(Float.class.getName())) {
                return Float.valueOf(string);
            } else if(type.equals(Long.class.getName())) {
                return Long.valueOf(string);
            } else if(type.equals(Integer.class.getName())) {
                return Integer.valueOf(string);
            } else if(type.equals(Timestamp.class.getName()) ||
                    type.equals(java.util.Date.class.getName())) {
                java.util.Date date = parseDate(timestampFormat, string, backupFormats);
                return new java.sql.Timestamp(date.getTime());
            } else if(type.equals(java.sql.Date.class.getName())) {
                java.util.Date date = parseDate(dateFormat, string, backupFormats);
                return java.sql.Date.valueOf(new java.sql.Date(date.getTime()).toString());
            } else if(type.equals(java.sql.Time.class.getName())) {
                java.util.Date date = parseDate(timeFormat, string, backupFormats);
                return java.sql.Time.valueOf(new java.sql.Time(date.getTime()).toString());
            } else if(type.equals(java.util.Date.class.getName())) {
                return parseDate(isoDateFormat, string, backupFormats);
            } else if(type.equals(Boolean.class.getName())) {
                return Boolean.valueOf(string);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return string;
    }
    
    protected static java.util.Date parseDate(
            SimpleDateFormat format, String string, SimpleDateFormat[] backupFormats) {
        java.util.Date result = null;
        Exception ex = null;
        try {
            result = format.parse(string);
        } catch(Exception e) {
            ex = e;
        }
        if(result == null) {
            int index = 0;
            while(result == null && index < backupFormats.length) {
                try {
                    if(backupFormats[index] != format) {
                        result = backupFormats[index].parse(string);
                    }
                } catch(Exception e) {
                    if(ex == null) {
                        ex = e;
                    }
                }
                index += 1;
            }
        }
        if(result == null) {
            throw new RuntimeException(ex);
        }
        return result;
    }
    
    @SuppressWarnings("rawtypes")
    public static synchronized int compare(Object o1, Object o2, String typeName) {
        try {
            if(o1 instanceof String) {
                o1 = convert((String) o1, typeName);
            }
            if(o2 instanceof String) {
                o2 = convert((String) o2, typeName);
            }
            if(o1 == o2) {
                return 0;
            } else if(o1 == null) {
                return -1;
            } else if(o2 == null) {
                return 1;
            } else {
                return ((Comparable) o1).compareTo(o2);
            }
        } catch(Exception e) {
            if(o1 != null && o1.equals(o2)) {
                return 0;
            } else {
                throw new RuntimeException("Incomparable objects");
            }
        }
    }
    
    public static synchronized boolean eq(Object o1, Object o2, String typeName) {
        try {
            return compare(compare(o1, o2, typeName), EQ);
        } catch(Exception e) {
            return false;
        }
    }
    
    public static synchronized boolean neq(Object o1, Object o2, String typeName) {
        try {
            return compare(compare(o1, o2, typeName), NEQ);
        } catch(Exception e) {
            // could possibly return true here, but we don't know what the exception really was
            // we don't want bogus comparisons to return true... any type of valid comparison
            // should be able to return true for NEQ without throwing an exception anyway
            return false;
        }
    }
    
    public static synchronized boolean lt(Object o1, Object o2, String typeName) {
        try {
            return compare(compare(o1, o2, typeName), LT);
        } catch(Exception e) {
            return false;
        }
    }
    
    public static synchronized boolean lte(Object o1, Object o2, String typeName) {
        try {
            return compare(compare(o1, o2, typeName), LTE);
        } catch(Exception e) {
            return false;
        }
    }
    
    public static synchronized boolean gt(Object o1, Object o2, String typeName) {
        try {
            return compare(compare(o1, o2, typeName), GT);
        } catch(Exception e) {
            return false;
        }
    }
    
    public static synchronized boolean gte(Object o1, Object o2, String typeName) {
        try {
            return compare(compare(o1, o2, typeName), GTE);
        } catch(Exception e) {
            return false;
        }
    }
    
    public static synchronized boolean compare(int result, int type) {
        return (result == 0 && (type == EQ || type == LTE || type == GTE)) ||
            (result < 0 && (type == LT || type == LT || type == NEQ)) ||
            (result > 0 && (type == GT || type == GTE || type == NEQ));
    }
}
