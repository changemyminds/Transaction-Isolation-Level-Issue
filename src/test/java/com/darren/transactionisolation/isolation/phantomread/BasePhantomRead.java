package com.darren.transactionisolation.isolation.phantomread;

import com.darren.transactionisolation.controller.PhantomReadControllerTest;
import com.darren.transactionisolation.isolation.GameTask;
import com.darren.transactionisolation.isolation.IsolationIssueTemplate;
import com.darren.transactionisolation.model.PhantomReadExpectOccur;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Author: changemyminds.
 * Date: 2021/4/27.
 * Description: {@link PhantomReadControllerTest}
 * Reference:
 */
public abstract class BasePhantomRead extends IsolationIssueTemplate<List<GameTask>, PhantomReadExpectOccur> {
    protected final void assertOccur(List<GameTask> actual, PhantomReadExpectOccur expectOccur) {
        assertThat(actual).isNotNull();
        assertThat(actual).isNotEmpty();
        assertThat(expectOccur).isNotNull();

        assertThat(actual.size()).isEqualTo(expectOccur.getIncorrectCount());
    }

    protected final void assertNotOccur(List<GameTask> actual, PhantomReadExpectOccur expectOccur) {
        assertThat(actual).isNotNull();
        assertThat(actual).isNotEmpty();
        assertThat(expectOccur).isNotNull();

        assertThat(actual.size()).isEqualTo(expectOccur.getCorrectCount());
    }
}
