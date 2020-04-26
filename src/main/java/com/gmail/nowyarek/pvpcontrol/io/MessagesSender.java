package com.gmail.nowyarek.pvpcontrol.io;

public interface MessagesSender {

    void error(Text text);
    void error(Text text, Variables var);
    void error(QueueableMessage message);

    void warning(Text text);
    void warning(Text text, Variables var);
    void warning(QueueableMessage message);

    void info(Text text);
    void info(Text text, Variables var);
    void info(QueueableMessage message);

    void annoucement(Text text);
    void annoucement(Text text, Variables var);
    void annoucement(QueueableMessage message);

}
