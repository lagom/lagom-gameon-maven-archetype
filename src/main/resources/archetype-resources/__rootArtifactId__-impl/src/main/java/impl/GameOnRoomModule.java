#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/*
 * Copyright (C) 2016-2017 Lightbend Inc. <https://www.lightbend.com>
 */
package ${package}.impl;

import com.google.inject.AbstractModule;
import ${package}.api.RoomService;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;

/**
 * The module that binds the RoomService so that it can be served.
 */
public class GameOnRoomModule extends AbstractModule implements ServiceGuiceSupport {
    @Override
    protected void configure() {
        bindService(RoomService.class, RoomServiceImpl.class);
    }
}
