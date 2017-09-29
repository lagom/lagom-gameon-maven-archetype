#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.api.protocol;

import akka.util.ByteString;
import ${package}.api.protocol.GameOnRoomRequest.*;
import com.lightbend.lagom.javadsl.api.deser.DeserializationException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GameOnRoomRequestSerializerTest {
    private final GameOnRoomRequestSerializer serializer = new GameOnRoomRequestSerializer();

    @Test(expected = DeserializationException.class)
    public void deserializeInvalidMessage() {
        serializer.deserialize(ByteString.fromString("invalid"));
    }

    @Test(expected = DeserializationException.class)
    public void deserializeUnknownTargetTypeMessage() {
        serializer.deserialize(ByteString.fromString("invalid,<roomId>,{}"));
    }

    @Test
    public void deserializeRoomCommandRequest() {
        ByteString message = ByteString.fromString("room,<roomId>,{${symbol_escape}n" +
                "    ${symbol_escape}"username${symbol_escape}": ${symbol_escape}"<username>${symbol_escape}",${symbol_escape}n" +
                "    ${symbol_escape}"userId${symbol_escape}": ${symbol_escape}"<userId>${symbol_escape}",${symbol_escape}n" +
                "    ${symbol_escape}"content${symbol_escape}": ${symbol_escape}"<message>${symbol_escape}"${symbol_escape}n" +
                "}"
        );

        RoomCommand expected = new RoomCommand("<roomId>", "<username>", "<userId>", "<message>");

        assertEquals(expected, serializer.deserialize(message));
    }

    @Test
    public void deserializeRoomHelloRequest() {
        ByteString message = ByteString.fromString("roomHello,<roomId>,{${symbol_escape}n" +
                "    ${symbol_escape}"username${symbol_escape}": ${symbol_escape}"<username>${symbol_escape}",${symbol_escape}n" +
                "    ${symbol_escape}"userId${symbol_escape}": ${symbol_escape}"<userId>${symbol_escape}",${symbol_escape}n" +
                "    ${symbol_escape}"version${symbol_escape}": 2${symbol_escape}n" +
                "}"
        );

        RoomHello expected = new RoomHello("<roomId>", "<username>", "<userId>", 2, false);

        assertEquals(expected, serializer.deserialize(message));
    }

    @Test
    public void deserializeRoomHelloRequestWithRecoveryFalse() {
        ByteString message = ByteString.fromString("roomHello,<roomId>,{${symbol_escape}n" +
                "    ${symbol_escape}"username${symbol_escape}": ${symbol_escape}"<username>${symbol_escape}",${symbol_escape}n" +
                "    ${symbol_escape}"userId${symbol_escape}": ${symbol_escape}"<userId>${symbol_escape}",${symbol_escape}n" +
                "    ${symbol_escape}"version${symbol_escape}": 2,${symbol_escape}n" +
                "    ${symbol_escape}"recovery${symbol_escape}": false${symbol_escape}n" +
                "}"
        );

        RoomHello expected = new RoomHello("<roomId>", "<username>", "<userId>", 2, false);

        assertEquals(expected, serializer.deserialize(message));
    }

    @Test
    public void deserializeRoomHelloRequestWithRecoveryTrue() {
        ByteString message = ByteString.fromString("roomHello,<roomId>,{${symbol_escape}n" +
                "    ${symbol_escape}"username${symbol_escape}": ${symbol_escape}"<username>${symbol_escape}",${symbol_escape}n" +
                "    ${symbol_escape}"userId${symbol_escape}": ${symbol_escape}"<userId>${symbol_escape}",${symbol_escape}n" +
                "    ${symbol_escape}"version${symbol_escape}": 2,${symbol_escape}n" +
                "    ${symbol_escape}"recovery${symbol_escape}": true${symbol_escape}n" +
                "}"
        );

        RoomHello expected = new RoomHello("<roomId>", "<username>", "<userId>", 2, true);

        assertEquals(expected, serializer.deserialize(message));
    }

    @Test
    public void deserializeRoomGoodbyeRequest() {
        ByteString message = ByteString.fromString("roomGoodbye,<roomId>,{${symbol_escape}n" +
                "    ${symbol_escape}"username${symbol_escape}": ${symbol_escape}"<username>${symbol_escape}",${symbol_escape}n" +
                "    ${symbol_escape}"userId${symbol_escape}": ${symbol_escape}"<userId>${symbol_escape}"${symbol_escape}n" +
                "}"
        );

        RoomGoodbye expected = new RoomGoodbye("<roomId>", "<username>", "<userId>");

        assertEquals(expected, serializer.deserialize(message));
    }

    @Test
    public void deserializeRoomJoinRequest() {
        ByteString message = ByteString.fromString("roomJoin,<roomId>,{${symbol_escape}n" +
                "    ${symbol_escape}"username${symbol_escape}": ${symbol_escape}"<username>${symbol_escape}",${symbol_escape}n" +
                "    ${symbol_escape}"userId${symbol_escape}": ${symbol_escape}"<userId>${symbol_escape}",${symbol_escape}n" +
                "    ${symbol_escape}"version${symbol_escape}": 2${symbol_escape}n" +
                "}"
        );

        RoomJoin expected = new RoomJoin("<roomId>", "<username>", "<userId>", 2);

        assertEquals(expected, serializer.deserialize(message));
    }

    @Test
    public void deserializeRoomPartRequest() {
        ByteString message = ByteString.fromString("roomPart,<roomId>,{${symbol_escape}n" +
                "    ${symbol_escape}"username${symbol_escape}": ${symbol_escape}"<username>${symbol_escape}",${symbol_escape}n" +
                "    ${symbol_escape}"userId${symbol_escape}": ${symbol_escape}"<userId>${symbol_escape}"${symbol_escape}n" +
                "}"
        );

        RoomPart expected = new RoomPart("<roomId>", "<username>", "<userId>");

        assertEquals(expected, serializer.deserialize(message));
    }

    @Test
    public void roundTripRoomCommandRequest() {
        assertSerializationRoundTripIsEqual(new RoomCommand("<roomId>", "<username>", "<userId>", "<message>"));
    }

    @Test
    public void roundTripRoomHelloRequestWithRecoveryFalse() {
        assertSerializationRoundTripIsEqual(new RoomHello("<roomId>", "<username>", "<userId>", 2, false));
    }

    @Test
    public void roundTripRoomHelloRequestWithRecoveryTrue() {
        assertSerializationRoundTripIsEqual(new RoomHello("<roomId>", "<username>", "<userId>", 2, true));
    }

    @Test
    public void roundTripRoomGoodbyeRequest() {
        assertSerializationRoundTripIsEqual(new RoomGoodbye("<roomId>", "<username>", "<userId>"));
    }

    @Test
    public void roundTripRoomJoinRequest() {
        assertSerializationRoundTripIsEqual(new RoomJoin("<roomId>", "<username>", "<userId>", 2));
    }

    @Test
    public void roundTripRoomPartRequest() {
        assertSerializationRoundTripIsEqual(new RoomPart("<roomId>", "<username>", "<userId>"));
    }

    private void assertSerializationRoundTripIsEqual(GameOnRoomRequest expected) {
        assertEquals(expected, serializer.deserialize(serializer.serialize(expected)));
    }
}
