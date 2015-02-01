package me.codingmatt.twitch.objects.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Copyright (c) 2015 Matthew Smeets - http://www.codingmatt.me
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
    String name();
}
