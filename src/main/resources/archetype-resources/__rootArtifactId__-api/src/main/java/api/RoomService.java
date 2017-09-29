#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/*
 * Copyright (C) 2016-2017 Lightbend Inc. <https://www.lightbend.com>
 */
package ${package}.api;

import akka.NotUsed;
import akka.stream.javadsl.Source;
import ${package}.api.protocol.GameOnRoomRequest;
import ${package}.api.protocol.GameOnRoomRequestSerializer;
import ${package}.api.protocol.GameOnRoomResponse;
import ${package}.api.protocol.GameOnRoomResponseSerializer;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;

import static com.lightbend.lagom.javadsl.api.Service.named;
import static com.lightbend.lagom.javadsl.api.Service.pathCall;

public interface RoomService extends Service {
    ServiceCall<Source<GameOnRoomRequest, ?>, Source<GameOnRoomResponse, NotUsed>> room();

    @Override
    default Descriptor descriptor() {
        return named("${rootArtifactId}")
                .withCalls(
                        pathCall("/${rootArtifactId}", this::room)
                )
                .withMessageSerializer(GameOnRoomRequest.class, new GameOnRoomRequestSerializer())
                .withMessageSerializer(GameOnRoomResponse.class, new GameOnRoomResponseSerializer())
                .withAutoAcl(true);
    }
}
