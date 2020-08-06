This mod adds new tools for datapack developers to take advantage of.

=================================================
Commands:

/advancedeffect give <targets> <effect> [<amplifier>] [<seconds>] [<hideParticles>] [<hideIcon>] [<isAmbient>]

/advancedeffect clear [<targets>] [<effect>]

This command is an extension of the classic effect command. It adds two additional boolean parameters: hideIcon and isAmbient.
hideIcon determines whether the effect’s icon will be displayed in the upper-right hand corner of the screen. This defaults to false unless hideParticles is set to true.
isAmbient determines whether the effect’s particles will be transparent and its icon be outlined in blue, in the same style as passive beacon effects.


air <targets> (add|set) <value>

This can be used to set the amount of air the targets have or add more. When using add, the value can be negative, allowing you to subtract air as well.
If the air you set the target to have is higher than their maximum air, their air will be filled to the maximum.
Value must be an integer.


/damage <targets> <amount> <sourceString> <sourceEntity> [<isFire>] [<pierceArmor>] [<difficultyScaled>] [<isMagic>] [<damageCreative>] [<isExplosion>] [<isProjectile>] [<absolute>] [<thorns>]

This command is a versatile method of dealing damage to targets. The message displayed on death screen and in the chat is a translated key in the format death.attack.<sourceString> and passes to the key’s variables:
	-The display name of the victim
	-The display name of <sourceEntity>
	-The display name of the item in the mainhand slot of <sourceEntity>
in that order.
So, if you wanted to deal 5.2 points of explosion damage to John from Kevin using a Boom Boom Stick, you would run damage John 5.2 explosion.player.item Kevin false false true false false true
The death message would consequently be “John was blown up by Kevin using Boom Boom Stick,” as the vanilla translation key death.attack.explosion.player.item translates to %1$s was blown up by %2$s using %3$s.
Of course, you can add your own translation keys to a language file allowing for highly customized death messages.
The damage amount must be a float no less than zero.


/damageitem <targets> <amount> (chest|feet|head|legs|mainhand|offhand)

Adds the given amount to the current value at the Damage path of the item in the specified equipment slot.
If the item does not have a Damage tag, one will be created.


/exhaust <players> <amount>

Exhausts the target players by a given amount. Pretty simple.
Will soon be changed to exhaustion <players> (add|set) <amount>
Amount must be a float no less than zero.


/feed <players> <foodLevel> [<saturation>]

Feeds the players by the given food level and saturates them by the given saturation.
Accepts values less than zero.
Food level must be an integer, while saturation must be a float.
Saturation defaults to zero when not set.


/health (add|set) <targets> <amount>

Sets the health of the targets or adds to their current amount.
Amount must be a float no less than zero.


/ignite <targets> <seconds>

Sets the targets on fire for a given amount of time.
Seconds must be an integer.
=================================================
