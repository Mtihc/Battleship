package com.mtihc.battleship;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class Invitation {

	private OfflinePlayer sender;
	private OfflinePlayer receiver;
	private String[] messages;
	private long cancelDelay;
	private int taskId;
	
	public Invitation(Player sender, String receiverName) {
		this(sender, receiverName, 1200L);
	}
	
	public Invitation(Player sender, String receiverName, long cancelDelay) {
		this.sender = sender;
		this.receiver = Bukkit.getOfflinePlayer(receiverName);
		this.cancelDelay = cancelDelay;
	}

	public OfflinePlayer getSender() {
		return sender;
	}
	
	public OfflinePlayer getReceiver() {
		return receiver;
	}
	
	public String[] getMessages() {
		return messages;
	}
	
	public void setMessages(String[] messages) {
		this.messages = messages;
	}

	public long getCancelDelay() {
		return cancelDelay;
	}

	public void setCancelDelay(long cancelDelay) {
		this.cancelDelay = cancelDelay;
	}
	
	protected void onSend() throws InvitationException {
		scheduleCancel();
	}
	protected void onAccept() throws InvitationException {
		unscheduleCancel();
	}
	protected void onDeny() throws InvitationException {
		unscheduleCancel();
	}
	protected void onCancel() {
		unscheduleCancel();
	}
	protected void onCancelByTimeout() {
		
	}
	
	private void scheduleCancel() {
		
		Runnable canceller = new Runnable() {
			
			@Override
			public void run() {
				InvitationManager.getInstance().removeInvitation(Invitation.this);
				Invitation.this.onCancelByTimeout();
			}
		};
		
		taskId = Bukkit.getScheduler().scheduleSyncDelayedTask(
				InvitationManager.getInstance().getPlugin(), canceller, cancelDelay);
	}
	
	private void unscheduleCancel() {
		Bukkit.getScheduler().cancelTask(taskId);
	}
	
	

}
