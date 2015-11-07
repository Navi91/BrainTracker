package com.orgazmpionerki.braintracker.auth;

/**
 * Created by Dmitriy on 06.11.2015.
 */
public interface AuthTokenTask extends Runnable {
    boolean refresh();
}
