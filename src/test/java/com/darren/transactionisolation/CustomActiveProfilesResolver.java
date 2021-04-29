package com.darren.transactionisolation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfilesResolver;

/**
 * Author: changemyminds.
 * Date: 2021/4/27.
 * Description:
 * Reference:
 */
public class CustomActiveProfilesResolver implements ActiveProfilesResolver {
    @Override
    public String[] resolve(Class<?> testClass) {
        String profile = System.getProperty("spring.profiles.active");
        System.out.println("Profile: " + profile);
        return new String[]{profile};
    }
}
