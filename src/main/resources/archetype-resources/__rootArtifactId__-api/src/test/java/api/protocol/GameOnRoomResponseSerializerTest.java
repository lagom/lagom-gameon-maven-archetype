#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.api.protocol;

import akka.util.ByteString;
import ${package}.api.protocol.GameOnRoomResponse.*;
import com.lightbend.lagom.javadsl.api.deser.DeserializationException;
import org.junit.Test;
import org.pcollections.HashTreePMap;
import org.pcollections.TreePVector;

import java.util.Optional;

import static com.google.common.primitives.Ints.asList;
import static ${package}.api.protocol.GameOnRoomResponse.PlayerResponse.ALL_PLAYERS;
import static org.junit.Assert.assertEquals;

public class GameOnRoomResponseSerializerTest {
    private final GameOnRoomResponseSerializer serializer = new GameOnRoomResponseSerializer();

    @Test(expected = DeserializationException.class)
    public void deserializeInvalidMessage() {
        serializer.deserialize(ByteString.fromString("invalid"));
    }

    @Test(expected = DeserializationException.class)
    public void deserializeUnknownTargetTypeMessage() {
        serializer.deserialize(ByteString.fromString("invalid,<playerId>,{}"));
    }

    @Test
    public void deserializeAckResponse() {
        ByteString message = ByteString.fromString("ack,{${symbol_escape}n" +
                "    ${symbol_escape}"version${symbol_escape}": [ 1, 2 ]${symbol_escape}n" +
                "}"
        );

        Ack expected = new Ack(TreePVector.from(asList(1, 2)));

        assertEquals(expected, serializer.deserialize(message));
    }

    @Test
    public void deserializeLocationResponse() {
        ByteString message = ByteString.fromString("player,<playerId>,{${symbol_escape}n" +
                "    ${symbol_escape}"type${symbol_escape}": ${symbol_escape}"location${symbol_escape}",${symbol_escape}n" +
                "    ${symbol_escape}"name${symbol_escape}": ${symbol_escape}"Room name${symbol_escape}",${symbol_escape}n" +
                "    ${symbol_escape}"fullName${symbol_escape}": ${symbol_escape}"Room's descriptive full name${symbol_escape}",${symbol_escape}n" +
                "    ${symbol_escape}"description${symbol_escape}": ${symbol_escape}"Lots of text about what the room looks like${symbol_escape}",${symbol_escape}n" +
                "    ${symbol_escape}"commands${symbol_escape}": {${symbol_escape}n" +
                "        ${symbol_escape}"/custom${symbol_escape}" : ${symbol_escape}"Description of what command does${symbol_escape}"${symbol_escape}n" +
                "    },${symbol_escape}n" +
                "    ${symbol_escape}"roomInventory${symbol_escape}": [${symbol_escape}"itemA${symbol_escape}",${symbol_escape}"itemB${symbol_escape}"]${symbol_escape}n" +
                "}"
        );

        Location expected = new GameOnRoomResponse.Location(
                "<playerId>",
                "Room name",
                "Room's descriptive full name",
                "Lots of text about what the room looks like",
                HashTreePMap.singleton("/custom", "Description of what command does"),
                TreePVector.<String>empty()
                        .plus("itemA")
                        .plus("itemB")
        );

        assertEquals(expected, serializer.deserialize(message));
    }

    @Test
    public void deserializeChatResponse() {
        ByteString message = ByteString.fromString("player,*,{${symbol_escape}n" +
                "  ${symbol_escape}"type${symbol_escape}": ${symbol_escape}"chat${symbol_escape}",${symbol_escape}n" +
                "  ${symbol_escape}"username${symbol_escape}": ${symbol_escape}"<username>${symbol_escape}",${symbol_escape}n" +
                "  ${symbol_escape}"content${symbol_escape}": ${symbol_escape}"<message>${symbol_escape}",${symbol_escape}n" +
                "  ${symbol_escape}"bookmark${symbol_escape}": ${symbol_escape}"String representing last message seen${symbol_escape}"${symbol_escape}n" +
                "}${symbol_escape}n"
        );

        Chat expected = new Chat(
                ALL_PLAYERS,
                "<username>",
                "<message>",
                Optional.of("String representing last message seen")
        );

        assertEquals(expected, serializer.deserialize(message));
    }

    @Test
    public void deserializeEventResponse() {
        ByteString message = ByteString.fromString("player,<playerId>,{${symbol_escape}n" +
                "    ${symbol_escape}"type${symbol_escape}": ${symbol_escape}"event${symbol_escape}",${symbol_escape}n" +
                "    ${symbol_escape}"content${symbol_escape}": {${symbol_escape}n" +
                "        ${symbol_escape}"*${symbol_escape}": ${symbol_escape}"general text for everyone${symbol_escape}",${symbol_escape}n" +
                "        ${symbol_escape}"<playerId>${symbol_escape}": ${symbol_escape}"specific to player${symbol_escape}"${symbol_escape}n" +
                "        },${symbol_escape}n" +
                "    ${symbol_escape}"bookmark${symbol_escape}": ${symbol_escape}"String representing last message seen${symbol_escape}"${symbol_escape}n" +
                "}"
        );

        Event expected = new Event(
                "<playerId>",
                HashTreePMap.<String, String>empty()
                        .plus(ALL_PLAYERS, "general text for everyone")
                        .plus("<playerId>", "specific to player"),
                Optional.of("String representing last message seen")
        );

        assertEquals(expected, serializer.deserialize(message));
    }

    @Test
    public void deserializeExitResponse() {
        ByteString message = ByteString.fromString("playerLocation,<playerId>,{${symbol_escape}n" +
                "    ${symbol_escape}"type${symbol_escape}": ${symbol_escape}"exit${symbol_escape}",${symbol_escape}n" +
                "    ${symbol_escape}"content${symbol_escape}": ${symbol_escape}"You exit through door xyz... ${symbol_escape}",${symbol_escape}n" +
                "    ${symbol_escape}"exitId${symbol_escape}": ${symbol_escape}"N${symbol_escape}"${symbol_escape}n" +
                // exit not supported
                "}"
        );

        Exit expected = new Exit(
                "<playerId>",
                "You exit through door xyz... ",
                "N"
        );

        assertEquals(expected, serializer.deserialize(message));
    }

    @Test
    public void roundTripAckResponse() {
        assertSerializationRoundTripIsEqual(new Ack(TreePVector.from(asList(1, 2))));
    }

    @Test
    public void roundTripLocationResponse() {
        assertSerializationRoundTripIsEqual(
                new GameOnRoomResponse.Location(
                        "<playerId>",
                        "Room name",
                        "Room's descriptive full name",
                        "Lots of text about what the room looks like",
                        HashTreePMap.singleton("/custom", "Description of what command does"),
                        TreePVector.<String>empty()
                                .plus("itemA")
                                .plus("itemB")
                )
        );
    }

    @Test
    public void roundTripChatResponse() {
        assertSerializationRoundTripIsEqual(
                new Chat(
                        ALL_PLAYERS,
                        "<username>",
                        "<message>",
                        Optional.of("String representing last message seen")
                )
        );
    }

    @Test
    public void roundTripEventResponse() {
        assertSerializationRoundTripIsEqual(
                new Event(
                        "<playerId>",
                        HashTreePMap.<String, String>empty()
                                .plus(ALL_PLAYERS, "general text for everyone")
                                .plus("<playerId>", "specific to player"),
                        Optional.of("String representing last message seen")
                )
        );
    }

    @Test
    public void roundTripExitResponse() {
        assertSerializationRoundTripIsEqual(
                new Exit(
                        "<playerId>",
                        "You exit through door xyz... ",
                        "N"
                )
        );
    }

    private void assertSerializationRoundTripIsEqual(GameOnRoomResponse expected) {
        assertEquals(expected, serializer.deserialize(serializer.serialize(expected)));
    }
}
