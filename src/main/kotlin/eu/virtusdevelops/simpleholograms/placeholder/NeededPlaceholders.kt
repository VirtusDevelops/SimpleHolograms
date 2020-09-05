package eu.virtusdevelops.simpleholograms.placeholder

import eu.virtusdevelops.simpleholograms.SimpleHolograms

class NeededPlaceholders(simpleHolograms: SimpleHolograms) {


    init {
        PlaceholderRegistry.registerPlaceholder(Placeholder(
                simpleHolograms,
                "{SLOW}",
                10.0,
                TimedPlaceholdersUpdater(mutableListOf("", ""))
        ))

        PlaceholderRegistry.registerPlaceholder(Placeholder(
                simpleHolograms,
                "{SLOWISH}",
                5.0,
                TimedPlaceholdersUpdater(mutableListOf("", ""))
        ))

        PlaceholderRegistry.registerPlaceholder(Placeholder(
                simpleHolograms,
                "{MEDIUM}",
                1.0,
                TimedPlaceholdersUpdater(mutableListOf("", ""))
        ))

        PlaceholderRegistry.registerPlaceholder(Placeholder(
                simpleHolograms,
                "{FAST}",
                0.1,
                TimedPlaceholdersUpdater(mutableListOf("", ""))
        ))
    }


}