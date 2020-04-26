package com.gmail.nowyarek.pvpcontrol.io;

import org.bukkit.command.ConsoleCommandSender;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class Console implements MessagesSender, HasMessagesBuffor {
    private final ConsoleCommandSender console;
    private final List<AbstractMap.SimpleEntry<OutputType, QueueableMessage>> messagesBuffer = new ArrayList<>();
    private boolean bufferLocked = true;

    public Console(ConsoleCommandSender console) {
        this.console = console;
    }

    @Override
    public void lockBuffer() {
        bufferLocked = true;
    }
    @Override
    public void releaseBuffer() {
        bufferLocked = false;
        messagesBuffer.forEach((keyValuePair) -> send(keyValuePair.getKey(), keyValuePair.getValue()));
        messagesBuffer.clear();
    }

    public static void debug(String ...messages) {
        String prefixes = Prefixes.getForOutputType(OutputType.DEBUG);
        for(String message : messages) {
            System.out.println(prefixes + message); // emergency debug
        }
    }

    @Override
    public void error(Text text) {
        send(OutputType.ERROR, new QueueableMessage(text));
    }
    @Override
    public void error(Text text, Variables var) {
        send(OutputType.ERROR, new QueueableMessage(text, var));
    }
    @Override
    public void error(QueueableMessage message) {
        send(OutputType.ERROR, message);
    }

    @Override
    public void warning(Text text) {
        send(OutputType.WARNING, new QueueableMessage(text));
    }
    @Override
    public void warning(Text text, Variables var) {
        send(OutputType.WARNING, new QueueableMessage(text, var));
    }
    @Override
    public void warning(QueueableMessage message) {
        send(OutputType.WARNING, message);
    }

    @Override
    public void info(Text text) {
        send(OutputType.INFO, new QueueableMessage(text));
    }
    @Override
    public void info(Text text, Variables var) {
        send(OutputType.INFO, new QueueableMessage(text, var));
    }
    @Override
    public void info(QueueableMessage message) {
        send(OutputType.INFO, message);
    }

    @Override
    public void annoucement(Text text) {
        send(OutputType.ANNOUCEMENT, new QueueableMessage(text));
    }
    @Override
    public void annoucement(Text text, Variables var) {
        send(OutputType.ANNOUCEMENT, new QueueableMessage(text, var));
    }
    @Override
    public void annoucement(QueueableMessage message) {
        send(OutputType.ANNOUCEMENT, message);
    }

    public void log(Text text) {
        send(OutputType.LOG, new QueueableMessage(text));
    }
    public void log(Text text, Variables var) {
        send(OutputType.LOG, new QueueableMessage(text, var));
    }
    public void log(QueueableMessage message) {
        send(OutputType.LOG, message);
    }

    private boolean send(OutputType outputType, QueueableMessage ...messages) {
        String prefixes = Prefixes.getForOutputType(outputType);

        if(bufferLocked) { // queue messages
            for(QueueableMessage message : messages) {
                messagesBuffer.add(
                    new AbstractMap.SimpleEntry<>(outputType, message)
                );
            }
            return false;
        } else { // send messages
            for(QueueableMessage message : messages) {
                this.console.sendMessage(prefixes + Localization.translate(message.getText(), message.getVariables()));
            }
            return true;
        }
    }

}
