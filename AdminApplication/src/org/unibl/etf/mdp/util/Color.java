package org.unibl.etf.mdp.util;

public enum Color {
	MY_MESSAGES_COLOR("FUCHSIA"),
	OTHER_MESSAGES_COLOR("DEEPSKYBLUE");
	
    private final String color;

    private Color(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
