package com.mtihc.battleship;

import java.util.HashMap;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class InvitationManager {

	private static InvitationManager instance;

	/**
	 * Returns the singleton InvitationManager. 
	 * @return the invitation manager
	 */
	public static InvitationManager getInstance() {
		return instance;
	}
	
	
	
	
	
	private JavaPlugin plugin;
	private HashMap<String, Invitation> invitations = new HashMap<String, Invitation>();

	/**
	 * Constructor.
	 * 
	 * @param plugin
	 *            The plugin
	 * @throws Exception
	 *             when the InvitationManager is already created.
	 */
	protected InvitationManager(JavaPlugin plugin) throws Exception {
		if (instance != null) {
			throw new Exception(
					"InvitationManager is already created. Use static method getInstance() instead.");
		}
		instance = this;

		this.plugin = plugin;
	}
	
	/**
	 * Returns the plugin
	 * 
	 * @return the plugin
	 */
	public JavaPlugin getPlugin() {
		return plugin;
	}
	
	
	
	
	
	/**
	 * Send an invitation.
	 * 
	 * @param invitation
	 *            The invitation
	 * @throws InvitationException
	 *             when sender/receiver already sent/received an invitation. Or
	 *             when receiver is not online.
	 */
	public void invite(Invitation invitation) throws InvitationException {
		OfflinePlayer sender = invitation.getSender();
		Invitation existing = invitations.get(sender.getName());
		if (existing != null) {
			if (sender.getName().equals(existing.getSender().getName())) {
				throw new InvitationException("Already invited "
						+ existing.getReceiver().getName());
			} else {
				throw new InvitationException("Already invited by "
						+ existing.getSender().getName());
			}
		}
		
		OfflinePlayer receiver = invitation.getReceiver();
		if (!receiver.hasPlayedBefore()) {
			throw new InvitationException("Player " + receiver.getName()
					+ " does not exist.");
		}
		if (!receiver.isOnline()) {
			throw new InvitationException("Player " + receiver.getName()
					+ " is offline.");
		}
		
		existing = invitations.get(receiver.getName());
		if (existing != null) {
			if (receiver.getName().equals(existing.getSender().getName())) {
				throw new InvitationException(receiver.getName()
						+ " already invited someone else.");
			} else {
				throw new InvitationException(receiver.getName()
						+ " is already invited by someone else.");
			}
		}

		invitation.onSend();
		receiver.getPlayer().sendMessage(invitation.getMessages());
		
		addInvitation(invitation);
	}

	/**
	 * Accept an invitation.
	 * 
	 * @param receiver The receiver of the invitation to accept (the accepter)
	 * @throws InvitationException when not invited, or when sender is offline
	 */
	public void accept(Player receiver) throws InvitationException {
		Invitation invitation = invitations.get(receiver.getName());
		if (invitation == null) {
			throw new InvitationException("Not invited by anyone. Or invitation expired.");
		}

		OfflinePlayer sender = invitation.getSender();
		if (!sender.isOnline()) {
			throw new InvitationException("Player " + sender.getName()
					+ " is offline.");
		}

		invitation.onAccept();
		
		removeInvitation(invitation);
	}

	/**
	 * Deny an invitation.
	 * 
	 * @param receiver The receiver of the invitation to accept (the denier)
	 * @throws InvitationException when not invited
	 */
	public void deny(Player receiver) throws InvitationException {
		Invitation invitation = invitations.get(receiver.getName());
		if (invitation == null) {
			throw new InvitationException("Not invited by anyone. Or invitation expired.");
		}

		invitation.onDeny();
		
		removeInvitation(invitation);
	}
	
	/**
	 * Cancel an invitation that was sent.
	 * @param sender The sender
	 * @throws InvitationException when there are no invitations sent by the given sender
	 */
	public void cancel(Player sender) throws InvitationException {
		Invitation invitation = invitations.get(sender.getName());
		if(invitation == null || invitation.getReceiver().getName().equals(sender.getName())) {
			throw new InvitationException("There are no sent invitations to cancel.");
		}
		
		invitation.onCancel();
		
		removeInvitation(invitation);
	}

	/**
	 * Adds an invitation to the list of invitations.
	 * 
	 * @param inv The invitation
	 */
	protected void addInvitation(Invitation inv) {
		invitations.put(inv.getReceiver().getName(), inv);
		invitations.put(inv.getSender().getName(), inv);
	}

	/**
	 * Removes an invitation from the list of invitations.
	 * 
	 * @param inv The invitation
	 */
	protected void removeInvitation(Invitation inv) {
		invitations.remove(inv.getReceiver().getName());
		invitations.remove(inv.getSender().getName());
	}

}
