package com.mtihc.battleship.controllers;

public class GameException extends Exception {

	private static final long serialVersionUID = 7169035860444851859L;

	public GameException() {
		
	}

	public GameException(String msg) {
		super(msg);
	}

	public GameException(Throwable cause) {
		super(cause);
	}

	public GameException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
