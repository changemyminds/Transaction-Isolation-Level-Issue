package com.darren.transactionisolation.log;

/**
 * Author: changemyminds.
 * Date: 2021/4/26.
 * Description:
 * Reference:
 */
public class LogTopic {
    public static final String DIRTY_READ = "DirtyRead";
    public static final String UNREPEATABLE_READ = "UnrepeatableRead";
    public static final String LOST_UPDATE = "LostUpdate";
    public static final String PHANTOM_READ = "PhantomRead";
}
