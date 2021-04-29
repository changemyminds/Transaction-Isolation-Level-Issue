package com.darren.transactionisolation.isolation;

/**
 * Author: changemyminds.
 * Date: 2021/4/26.
 * Description:
 * Reference:
 */
public interface IsolationOperate {
    void test_DEFAULT() throws Exception;

    void test_READ_UNCOMMITTED() throws Exception;

    void test_READ_COMMITTED() throws Exception;

    void test_REPEATABLE_READ() throws Exception;

    void test_SERIALIZABLE() throws Exception;
}
