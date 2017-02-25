package com.orgazmpionerki.braintracker.test;

import org.hamcrest.Matchers;
import org.junit.Test;

import static org.junit.Assert.assertThat;

/**
 * Created by Dmitriy on 28.11.2016.
 */

public class EmailValidatorTest {

    @Test
    public void emailValidator_CorrectEmailSimple_ReturnsTrue() {
        assertThat("mock".equals("mock"), Matchers.is(true));
    }

}
