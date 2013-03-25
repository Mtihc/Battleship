package com.mtihc.battleship.controllers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;

import com.mtihc.battleship.models.Board;
import com.mtihc.battleship.models.Ship;
import com.mtihc.battleship.views.BoardEnemyView;
import com.mtihc.battleship.views.BoardView;

public class GameController {

	private Board leftBoard;
	private Board rightBoard;
	
	private Location leftOrigin;
	private Location rightOrigin;
	
	private BoardView leftBoardView;
	private BoardView rightBoardView;
	
	private BoardEnemyView leftEnemyView;
	private BoardEnemyView rightEnemyView;

	public GameController(int width, int height, Location origin) {
		leftBoard = new Board(width, height, createShips());
		rightBoard = new Board(width, height, createShips());
		
		BlockFace facing = BoardView.yawToFace(origin.getYaw());
		BlockFace facingLeft = BoardView.yawToFace(origin.getYaw() - 90);
		BlockFace facingRight = BoardView.yawToFace(origin.getYaw() + 90);
		
		leftOrigin = origin.getBlock().getRelative(facingLeft, 3).getRelative(facing, width).getLocation();
		rightOrigin = origin.getBlock().getRelative(facingRight, 3).getRelative(facing, 1).getLocation();
		

		// TODO see if boards line up
		rightOrigin.getBlock().setType(Material.IRON_BLOCK);
		leftOrigin.getBlock().setType(Material.DIAMOND_BLOCK);
		
		leftBoardView = new BoardView(leftBoard, leftOrigin, facingRight);
		rightBoardView = new BoardView(rightBoard, rightOrigin, facingLeft);
		
		leftEnemyView = new BoardEnemyView(rightBoard, leftOrigin, facingRight);
		rightEnemyView = new BoardEnemyView(leftBoard, rightOrigin, facingLeft);
	}

	private Ship[] createShips() {
		return new Ship[] {
				new Ship(2),
				new Ship(2),
				new Ship(2),
				new Ship(2),
				new Ship(3),
				new Ship(3),
				new Ship(3),
				new Ship(4),
				new Ship(4),
				new Ship(5)
		};
	}

	public void initialize() {
		leftBoardView.draw();
		leftEnemyView.draw();
		
		rightBoardView.draw();
		rightEnemyView.draw();
	}
}
