/*
* Copyright 2010 BetaSteward_at_googlemail.com. All rights reserved.
*
* Redistribution and use in source and binary forms, with or without modification, are
* permitted provided that the following conditions are met:
*
*    1. Redistributions of source code must retain the above copyright notice, this list of
*       conditions and the following disclaimer.
*
*    2. Redistributions in binary form must reproduce the above copyright notice, this list
*       of conditions and the following disclaimer in the documentation and/or other materials
*       provided with the distribution.
*
* THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
* WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
* FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
* CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
* CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
* SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
* ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
* NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
* ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*
* The views and conclusions contained in the software and documentation are those of the
* authors and should not be interpreted as representing official policies, either expressed
* or implied, of BetaSteward_at_googlemail.com.
*/

package mage.game.events;

import java.io.Serializable;
import java.util.*;

import mage.abilities.Ability;
import mage.abilities.ActivatedAbility;
import mage.abilities.TriggeredAbilities;
import mage.abilities.TriggeredAbility;
import mage.cards.Card;
import mage.cards.Cards;
import mage.game.permanent.Permanent;

/**
 *
 * @author BetaSteward_at_googlemail.com
 */
public class PlayerQueryEvent extends EventObject implements ExternalEvent, Serializable {

	public enum QueryType {
		ASK, CHOOSE, CHOOSE_ABILITY, CHOOSE_MODE, PICK_TARGET, PICK_ABILITY, SELECT, PLAY_MANA, PLAY_X_MANA, AMOUNT, LOOK, PICK_CARD, CONSTRUCT
	}

	private String message;
	private Collection<? extends Ability> abilities;
	private List<Permanent> perms;
	private Set<String> choices;
	private Set<UUID> targets;
	private Cards cards;
	private List<Card> booster;
	private QueryType queryType;
	private UUID playerId;
	private boolean required;
	private int min;
	private int max;
	private Map<String, Serializable> options;
	private Map<UUID, String> modes;

	private PlayerQueryEvent(UUID playerId, String message, Collection<? extends Ability> abilities, Set<String> choices, Set<UUID> targets, Cards cards, QueryType queryType, int min, int max, boolean required, Map<String, Serializable> options) {
		this(playerId, message, abilities, choices, targets, cards, queryType, min, max, required);
		this.options = options;
	}

	private PlayerQueryEvent(UUID playerId, String message, Collection<? extends Ability> abilities, Set<String> choices, Set<UUID> targets, Cards cards, QueryType queryType, int min, int max, boolean required) {
		super(playerId);
		this.queryType = queryType;
		this.message = message;
		this.playerId = playerId;
		this.abilities = abilities;
		this.choices = choices;
		this.targets = targets;
		this.cards = cards;
		this.required = required;
		this.min = min;
		this.max = max;
	}

	private PlayerQueryEvent(UUID playerId, String message, List<Card> booster, QueryType queryType, int time) {
		super(playerId);
		this.queryType = queryType;
		this.message = message;
		this.playerId = playerId;
		this.booster = booster;
		this.max = time;
	}

	private PlayerQueryEvent(UUID playerId, String message, QueryType queryType, int time) {
		super(playerId);
		this.queryType = queryType;
		this.message = message;
		this.playerId = playerId;
		this.max = time;
	}

	private PlayerQueryEvent(UUID playerId, String message, QueryType queryType, List<Permanent> perms, boolean required) {
		super(playerId);
		this.queryType = queryType;
		this.message = message;
		this.playerId = playerId;
		this.perms = perms;
		this.required = required;
	}

	private PlayerQueryEvent(UUID playerId, String message, Map<UUID, String> modes) {
		super(playerId);
		this.queryType = QueryType.CHOOSE_MODE;
		this.message = message;
		this.playerId = playerId;
		this.modes = modes;
	}

	public static PlayerQueryEvent askEvent(UUID playerId, String message) {
		return new PlayerQueryEvent(playerId, message, null, null, null, null, QueryType.ASK, 0, 0, false);
	}
	
	public static PlayerQueryEvent chooseAbilityEvent(UUID playerId, String message, Collection<? extends ActivatedAbility> choices) {
		return new PlayerQueryEvent(playerId, message, choices, null, null, null, QueryType.CHOOSE_ABILITY, 0, 0, false);
	}

	public static PlayerQueryEvent chooseModeEvent(UUID playerId, String message, Map<UUID, String> modes) {
		return new PlayerQueryEvent(playerId, message, modes);
	}

	public static PlayerQueryEvent chooseEvent(UUID playerId, String message, Set<String> choices) {
		return new PlayerQueryEvent(playerId, message, null, choices, null, null, QueryType.CHOOSE, 0, 0, false);
	}

	public static PlayerQueryEvent targetEvent(UUID playerId, String message, Set<UUID> targets, boolean required) {
		return new PlayerQueryEvent(playerId, message, null, null, targets, null, QueryType.PICK_TARGET, 0, 0, required);
	}

	public static PlayerQueryEvent targetEvent(UUID playerId, String message, Set<UUID> targets, boolean required, Map<String, Serializable> options) {
		return new PlayerQueryEvent(playerId, message, null, null, targets, null, QueryType.PICK_TARGET, 0, 0, required, options);
	}

	public static PlayerQueryEvent targetEvent(UUID playerId, String message, Cards cards, boolean required, Map<String, Serializable> options) {
		return new PlayerQueryEvent(playerId, message, null, null, null, cards, QueryType.PICK_TARGET, 0, 0, required, options);
	}

	public static PlayerQueryEvent targetEvent(UUID playerId, String message, List<TriggeredAbility> abilities) {
		return new PlayerQueryEvent(playerId, message, abilities, null, null, null, QueryType.PICK_ABILITY, 0, 0, true);
	}

	public static PlayerQueryEvent targetEvent(UUID playerId, String message, List<Permanent> perms, boolean required) {
		return new PlayerQueryEvent(playerId, message, QueryType.PICK_TARGET, perms, required);
	}

	public static PlayerQueryEvent selectEvent(UUID playerId, String message) {
		return new PlayerQueryEvent(playerId, message, null, null, null, null, QueryType.SELECT, 0, 0, false);
	}

	public static PlayerQueryEvent playManaEvent(UUID playerId, String message) {
		return new PlayerQueryEvent(playerId, message, null, null, null, null, QueryType.PLAY_MANA, 0, 0, false);
	}

	public static PlayerQueryEvent playXManaEvent(UUID playerId, String message) {
		return new PlayerQueryEvent(playerId, message, null, null, null, null, QueryType.PLAY_X_MANA, 0, 0, false);
	}

	public static PlayerQueryEvent amountEvent(UUID playerId, String message, int min , int max) {
		return new PlayerQueryEvent(playerId, message, null, null, null, null, QueryType.AMOUNT, min, max, false);
	}

	public static PlayerQueryEvent lookEvent(UUID playerId, String message, Cards cards) {
		return new PlayerQueryEvent(playerId, message, null, null, null, cards, QueryType.LOOK, 0, 0, false);
	}
	
	public static PlayerQueryEvent pickCard(UUID playerId, String message, List<Card> booster, int time) {
		return new PlayerQueryEvent(playerId, message, booster, QueryType.PICK_CARD, time);
	}

	public static PlayerQueryEvent construct(UUID playerId, String message, int time) {
		return new PlayerQueryEvent(playerId, message, QueryType.CONSTRUCT, time);
	}


	public String getMessage() {
		return message;
	}

	public QueryType getQueryType() {
		return queryType;
	}

	public Collection<? extends Ability> getAbilities() {
		return abilities;
	}

	public Set<String> getChoices() {
		return choices;
	}

	public Set<UUID> getTargets() {
		return targets;
	}

	public UUID getPlayerId() {
		return playerId;
	}

	public boolean isRequired() {
		return required;
	}

	public Cards getCards() {
		return cards;
	}

	public List<Permanent> getPerms() {
		return perms;
	}
	
	public List<Card> getBooster() {
		return booster;
	}

	public int getMin() {
		return min;
	}

	public int getMax() {
		return max;
	}

	public Map<String, Serializable> getOptions() {
		return options;
	}
	
	public Map<UUID, String> getModes() {
		return modes;
	}
}
