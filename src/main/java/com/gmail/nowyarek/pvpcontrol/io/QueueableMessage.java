package com.gmail.nowyarek.pvpcontrol.io;

public class QueueableMessage {
    private Text text;
    private Variables var;

    public QueueableMessage(Text text) {
        this.text = text;
    }
    public QueueableMessage(Text text, Variables var) {
        this.text = text;
        this.var = var;
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    public Variables getVariables() {
        return var;
    }

    public void setVariables(Variables var) {
        this.var = var;
    }
}
