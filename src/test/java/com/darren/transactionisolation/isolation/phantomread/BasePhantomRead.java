package com.darren.transactionisolation.isolation.phantomread;

import com.darren.transactionisolation.controller.PhantomReadControllerTest;
import com.darren.transactionisolation.isolation.GameTask;
import com.darren.transactionisolation.isolation.IsolationIssueTemplate;
import com.darren.transactionisolation.model.PhantomReadExpectOccur;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Author: changemyminds.
 * Date: 2021/4/27.
 * Description: {@link PhantomReadControllerTest}
 * Reference:
 */
public abstract class BasePhantomRead extends IsolationIssueTemplate<GameTask, PhantomReadExpectOccur> {
    protected final void assertOccur(GameTask actual, PhantomReadExpectOccur expectOccur) {
        assertThat(actual).isNotNull();
        assertThat(expectOccur).isNotNull();

        assertThat(actual.getScore()).isGreaterThan(expectOccur.getAuditScore());
        assertThat(actual.getName()).isEqualTo(expectOccur.getName());
        assertThat(actual.getScore()).isEqualTo(expectOccur.getScore());
        assertThat(actual.getCredit()).isEqualTo(expectOccur.getCredit());
    }

    protected final void assertNotOccur(GameTask actual, PhantomReadExpectOccur expectOccur) {
        assertThat(actual).isNull();
        assertThat(expectOccur).isNotNull();
    }
}
