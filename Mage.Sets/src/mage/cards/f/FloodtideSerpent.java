
package mage.cards.f;

import java.util.UUID;
import mage.MageInt;
import mage.abilities.common.SimpleStaticAbility;
import mage.abilities.costs.common.ReturnToHandChosenControlledPermanentCost;
import mage.abilities.effects.PayCostToAttackBlockEffectImpl;
import mage.abilities.effects.common.combat.CantAttackBlockUnlessPaysSourceEffect;
import mage.cards.CardImpl;
import mage.cards.CardSetInfo;
import mage.constants.CardType;
import mage.constants.SubType;
import mage.constants.Zone;
import mage.filter.common.FilterControlledPermanent;
import mage.target.common.TargetControlledPermanent;

/**
 *
 * @author LevelX2
 */
public final class FloodtideSerpent extends CardImpl {

    private static final FilterControlledPermanent filter = new FilterControlledPermanent("an enchantment you control");

    static {
        filter.add(CardType.ENCHANTMENT.getPredicate());
    }

    public FloodtideSerpent(UUID ownerId, CardSetInfo setInfo) {
        super(ownerId,setInfo,new CardType[]{CardType.CREATURE},"{4}{U}");
        this.subtype.add(SubType.SERPENT);

        this.power = new MageInt(4);
        this.toughness = new MageInt(4);

        // Floodtide Serpent can't attack unless you return an enchantment you control to its owner's hand <i>(This cost is paid as attackers are declared.)</i>
        this.addAbility(new SimpleStaticAbility(Zone.BATTLEFIELD, new CantAttackBlockUnlessPaysSourceEffect(
                new ReturnToHandChosenControlledPermanentCost(new TargetControlledPermanent(filter)), PayCostToAttackBlockEffectImpl.RestrictType.ATTACK)));

    }

    public FloodtideSerpent(final FloodtideSerpent card) {
        super(card);
    }

    @Override
    public FloodtideSerpent copy() {
        return new FloodtideSerpent(this);
    }
}
