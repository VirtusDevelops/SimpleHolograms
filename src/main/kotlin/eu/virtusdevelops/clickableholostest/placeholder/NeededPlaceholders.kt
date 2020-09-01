package eu.virtusdevelops.clickableholostest.placeholder

import eu.virtusdevelops.clickableholostest.ClickableHolosTest

class NeededPlaceholders(clickableHolosTest: ClickableHolosTest) {


    init {
        PlaceholderRegistry.registerPlaceholder(Placeholder(
                clickableHolosTest,
                "{SLOW}",
                10.0,
                TimedPlaceholdersUpdater(mutableListOf("&8 &r", "&7 &r"))
        ))

        PlaceholderRegistry.registerPlaceholder(Placeholder(
                clickableHolosTest,
                "{SLOWISH}",
                5.0,
                TimedPlaceholdersUpdater(mutableListOf("&8 &r", "&7 &r"))
        ))

        PlaceholderRegistry.registerPlaceholder(Placeholder(
                clickableHolosTest,
                "{MEDIUM}",
                1.0,
                TimedPlaceholdersUpdater(mutableListOf("&8 &r", "&7 &r"))
        ))

        PlaceholderRegistry.registerPlaceholder(Placeholder(
                clickableHolosTest,
                "{FAST}",
                0.1,
                TimedPlaceholdersUpdater(mutableListOf("&8 &r", "&7 &r"))
        ))
    }


}