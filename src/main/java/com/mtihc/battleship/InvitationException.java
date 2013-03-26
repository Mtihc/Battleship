package com.mtihc.battleship;

public class InvitationException extends Exception {
	
	private static final long serialVersionUID = 3945231980356539617L;

	public InvitationException() {
		
	}

	public InvitationException(String msg) {
		super(msg);
	}

	public InvitationException(Throwable cause) {
		super(cause);
	}

	public InvitationException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
