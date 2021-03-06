#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.impl;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import ${package}.api.protocol.GameOnRoomRequest.RoomCommand;
import ${package}.api.protocol.GameOnRoomResponse;
import ${package}.api.protocol.GameOnRoomResponse.Chat;
import ${package}.api.protocol.GameOnRoomResponse.Event;
import ${package}.api.protocol.GameOnRoomResponse.Location;
import org.pcollections.HashTreePMap;
import org.pcollections.PMap;
import org.pcollections.PSequence;
import org.pcollections.TreePVector;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ${package}.api.protocol.GameOnRoomRequest.RoomHello;
import static ${package}.api.protocol.GameOnRoomResponse.Exit;
import static ${package}.api.protocol.GameOnRoomResponse.PlayerResponse;

class Room extends AbstractActor {
    static final String NAME = "${rootArtifactId}";
    static final String FULL_NAME = "Room's descriptive full name";
    static final String DESCRIPTION = "Lots of text about what the room looks like";

    static final PMap<String, String> EXITS = HashTreePMap.<String, String>empty()
            .plus("N", "north")
            .plus("S", "south")
            .plus("E", "east")
            .plus("W", "west");

    static final PMap<String, String> COMMANDS = HashTreePMap.<String, String>empty();
    // Add custom commands below:
    //        .plus("/ping", "Does this work?");
    // Each custom command will also need to be added to the handleCommand method.

    static final PSequence<String> INVENTORY = TreePVector.<String>empty();
    // Add items below to include them in your room:
    //        .plus("itemA")
    //        .plus("itemB")

    private static final String EXIT_PREFIX = "You head ";
    private static final String EXIT_INSTRUCTIONS = " (type '/exits' to see a list)";
    private static final String MISSING_DIRECTION = "Provide a direction to go" + EXIT_INSTRUCTIONS;
    private static final String UNKNOWN_DIRECTION_PREFIX = "Unknown direction: ";
    private static final String UNKNOWN_COMMAND = "Unknown command: ";

    private static final Pattern COMMAND = Pattern.compile("${symbol_escape}${symbol_escape}A/(${symbol_escape}${symbol_escape}S+)${symbol_escape}${symbol_escape}s*(.*)${symbol_escape}${symbol_escape}Z");

    static Props props() {
        return Props.create(Room.class);
    }

    Room() {
        receive(ReceiveBuilder
                .create()
                .match(RoomHello.class, this::replyWithLocation)
                .match(RoomCommand.class, this::handleCommand)
                .build()
        );

    }

    private void replyWithLocation(RoomHello hello) {
        Location location = Location.builder()
                .playerId(hello.getUserId())
                .name(NAME)
                .fullName(FULL_NAME)
                .description(DESCRIPTION)
                .commands(COMMANDS)
                .roomInventory(INVENTORY)
                .build();
        reply(location, sender());
    }

    private void handleCommand(RoomCommand message) {
        Optional<Command> command = parseCommand(message);
        if (command.isPresent()) {
            switch (command.get().name) {
                case "look":
                    handleLookCommand(message);
                    break;
                case "go":
                    handleGoCommand(message, command.get().argument);
                    break;
                default:
                    handleUnknownCommand(message);
            }
        } else handleChat(message);
    }

    private Optional<Command> parseCommand(RoomCommand message) {
        Matcher matcher = COMMAND.matcher(message.getContent());
        if (matcher.matches()) {
            return Optional.of(new Command(matcher.group(1), matcher.group(2).trim()));
        }
        return Optional.empty();
    }

    private void handleLookCommand(RoomCommand lookCommand) {
        Location location = Location.builder()
                .playerId(lookCommand.getUserId())
                .name(NAME)
                .fullName(FULL_NAME)
                .description(DESCRIPTION)
                .commands(COMMANDS)
                .roomInventory(INVENTORY)
                .build();
        reply(location, sender());
    }

    private void handleGoCommand(RoomCommand goCommand, String direction) {
        if (!direction.isEmpty()) {
            String exitDescription = EXITS.get(direction.toUpperCase());
            if (exitDescription != null) {
                Exit exit = Exit.builder()
                        .playerId(goCommand.getUserId())
                        .exitId(direction)
                        .content(EXIT_PREFIX + exitDescription)
                        .build();
                reply(exit, sender());
            } else {
                handleUnknownDirection(goCommand, direction);
            }
        } else {
            handleMissingDirection(goCommand);
        }
    }

    private void handleUnknownDirection(RoomCommand goCommand, String unknownDirection) {
        Event unknownDirectionResponse = Event.builder()
                .playerId(goCommand.getUserId())
                .content(HashTreePMap.singleton(
                        goCommand.getUserId(), UNKNOWN_DIRECTION_PREFIX + unknownDirection + EXIT_INSTRUCTIONS
                ))
                .bookmark(Optional.empty())
                .build();
        reply(unknownDirectionResponse, sender());
    }

    private void handleMissingDirection(RoomCommand goCommand) {
        Event missingDirectionResponse = Event.builder()
                .playerId(goCommand.getUserId())
                .content(HashTreePMap.singleton(
                        goCommand.getUserId(), MISSING_DIRECTION
                ))
                .bookmark(Optional.empty())
                .build();
        reply(missingDirectionResponse, sender());
    }

    private void handleUnknownCommand(RoomCommand unknownCommand) {
        Event unknownCommandResponse = Event.builder()
                .playerId(unknownCommand.getUserId())
                .content(HashTreePMap.singleton(
                        unknownCommand.getUserId(), UNKNOWN_COMMAND + unknownCommand.getContent()
                ))
                .bookmark(Optional.empty())
                .build();
        reply(unknownCommandResponse, sender());
    }

    private void handleChat(RoomCommand chatCommand) {
        Chat chatResponse = Chat.builder()
                .playerId(PlayerResponse.ALL_PLAYERS)
                .username(chatCommand.getUsername())
                .content(chatCommand.getContent())
                .bookmark(Optional.empty())
                .build();
        reply(chatResponse, sender());
    }

    private void reply(GameOnRoomResponse response, ActorRef sender) {
        sender().tell(response, self());
    }

    private static class Command {
        final String name;
        final String argument;

        private Command(String name, String argument) {
            this.name = name;
            this.argument = argument;
        }
    }
}
