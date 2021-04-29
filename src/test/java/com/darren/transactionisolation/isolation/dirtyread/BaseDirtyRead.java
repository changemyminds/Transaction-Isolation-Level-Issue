package com.darren.transactionisolation.isolation.dirtyread;

import com.darren.transactionisolation.controller.DirtyReadControllerTest;
import com.darren.transactionisolation.isolation.Account;
import com.darren.transactionisolation.isolation.IsolationIssueTemplate;
import com.darren.transactionisolation.model.DirtyReadExpectOccur;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Author: changemyminds.
 * Date: 2021/4/28.
 * Description: {@link DirtyReadControllerTest}
 * Reference:
 */
public class BaseDirtyRead extends IsolationIssueTemplate<Account, DirtyReadExpectOccur> {
    @Override
    protected final void assertOccur(Account actual, DirtyReadExpectOccur expectOccur) {
        assertThat(actual).isNotNull();

        // actual: -300, expect: -300
        assertThat(actual.getAmount()).isEqualTo(expectOccur.getIncorrectTotalAmount());
    }

    @Override
    protected final void assertNotOccur(Account actual, DirtyReadExpectOccur expectOccur) {
        assertThat(actual).isNotNull();

        // actual: 200, expect: 200
        assertThat(actual.getAmount()).isEqualTo(expectOccur.getCorrectTotalAmount());
    }
}
