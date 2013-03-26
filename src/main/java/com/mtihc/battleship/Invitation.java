package com.mtihc.battleship;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public abstract class Invitation {

	private OfflinePlayer sender;
	private OfflinePlayer receiver;
	private long cancelDelay;
	
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

	public long getCancelDelay() {
		return cancelDelay;
	}

	public void setCancelDelay(long cancelDelay) {
		this.cancelDelay = cancelDelay;
	}
	
	void scheduleCancel() {
		
		Runnable canceller = new Runnable() {
			
			@Override
			public void run() {
				InvitationManager.getInstance().removeInvitation(Invitation.this);
				Invitation.this.onCancel();
			}
		};
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(
				InvitationManager.getInstance().getPlugin(), canceller, cancelDelay);
	}
	
	protected abstract void onSend();
	protected abstract void onAccept();
	protected abstract void onDeny();
	protected abstract void onCancel();
	

}
