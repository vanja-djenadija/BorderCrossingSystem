package org.unibl.etf.mdp.protocol;

public enum ProtocolMessages {
	HELLO("HELLO"),
	UNICAST("UNICAST"),
	MULTICAST("MULTICAST"), 
	BROADCAST("BROADCAST");
	
    private final String value;

    private ProtocolMessages(String value) {
        this.value = value;
    }

    public String getMessage() {
        return value;
    }
}
