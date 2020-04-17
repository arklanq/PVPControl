package com.gmail.nowyarek.pvpcontrol.core;

import com.gmail.nowyarek.pvpcontrol.io.*;
import org.bukkit.entity.Player;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class PvpPlayer implements MessagesSender, HasMessagesBuffor {
    private final Player p;
    private final List<AbstractMap.SimpleEntry<OutputType, QueueableMessage>> messagesBuffer = new ArrayList<>();
    private boolean locked = true;

    public PvpPlayer(Player p) {
        this.p = p;
    }

    public Player getPlayer() {
        return p;
    }

    @Override
    public void lockBuffer() {
        locked = true;
    }
    @Override
    public void releaseBuffer() {
        locked = false;
        messagesBuffer.forEach((keyValuePair) -> send(keyValuePair.getKey(), keyValuePair.getValue()));
        messagesBuffer.clear();
    }

    @Override
    public void debug(String ...messages) {
        String prefixes = Prefixes.getForOutputType(OutputType.DEBUG);
        for(String message : messages) {
            p.sendMessage(prefixes + message);
        }
    }

    @Override
    public void error(Text text) {
        this.send(OutputType.ERROR, new QueueableMessage(text));
    }
    @Override
    public void error(Text text, Variables var) {
        this.send(OutputType.ERROR, new QueueableMessage(text, var));
    }
    @Override
    public void error(QueueableMessage message) {
        this.send(OutputType.ERROR, message);
    }
    @Override
    public void warning(Text text) {
        this.send(OutputType.WARNING, new QueueableMessage(text));
    }
    @Override
    public void warning(Text text, Variables var) {
        this.send(OutputType.WARNING, new QueueableMessage(text, var));
    }

    @Override
    public void warning(QueueableMessage message) {
        this.send(OutputType.WARNING, message);
    }
    @Override
    public void info(Text text) {
        this.send(OutputType.INFO, new QueueableMessage(text));
    }
    @Override
    public void info(Text text, Variables var) {
        this.send(OutputType.INFO, new QueueableMessage(text, var));
    }
    @Override
    public void info(QueueableMessage message) {
        this.send(OutputType.INFO, message);
    }

    @Override
    public void annoucement(Text text) {
        this.send(OutputType.ANNOUCEMENT, new QueueableMessage(text));
    }
    @Override
    public void annoucement(Text text, Variables var) {
        this.send(OutputType.ANNOUCEMENT, new QueueableMessage(text, var));
    }
    @Override
    public void annoucement(QueueableMessage message) {
        this.send(OutputType.ANNOUCEMENT, message);
    }

    private void send(OutputType outputType, QueueableMessage ...messages) {
        String prefixes = Prefixes.getForOutputType(outputType);
        for(QueueableMessage message : messages) {
            p.sendMessage(prefixes + Localization.translate(message.getText(), message.getVariables()));
        }
    }

}
