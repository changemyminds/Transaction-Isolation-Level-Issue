package com.darren.transactionisolation.isolation.unrepeatableread;

import com.darren.transactionisolation.controller.UnrepeatableReadControllerTest;
import com.darren.transactionisolation.isolation.Inventory;
import com.darren.transactionisolation.isolation.IsolationIssueTemplate;
import com.darren.transactionisolation.model.UnrepeatableReadExpectOccur;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Author: changemyminds.
 * Date: 2021/4/28.
 * Description: {@link UnrepeatableReadControllerTest}
 * Reference:
 */
public class BaseUnrepeatableRead extends IsolationIssueTemplate<Inventory, UnrepeatableReadExpectOccur> {
    @Override
    protected final void assertOccur(Inventory actual, UnrepeatableReadExpectOccur expectOccur) {
        assertThat(actual).isNotNull();
        assertThat(expectOccur).isNotNull();

        assertThat(actual.getQuantity()).isEqualTo(expectOccur.getIncorrectQuantity());
    }

    @Override
    protected final void assertNotOccur(Inventory actual, UnrepeatableReadExpectOccur expectOccur) {
        assertThat(actual).isNotNull();
        assertThat(expectOccur).isNotNull();

        assertThat(actual.getQuantity()).isEqualTo(expectOccur.getCorrectQuantity());
    }
}
