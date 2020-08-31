package eu.virtusdevelops.clickableholostest.placeholder

import eu.virtusdevelops.clickableholostest.ClickableHolosTest
import eu.virtusdevelops.clickableholostest.handlers.HologramRegistry
import eu.virtusdevelops.virtuscore.VirtusCore
import org.bukkit.Bukkit

class PlaceholderManager() {




    companion object {
        private var elapsedTenthsOfSecond: Long = 0


        fun load(plugin: ClickableHolosTest) {
            Bukkit.getScheduler().runTaskTimer(plugin, Runnable {
                var updated = false
                for (placeholder in PlaceholderRegistry.placeholders) {
                    if (elapsedTenthsOfSecond % placeholder.tenthsToRefresh == 0L) {
                        try {
                            placeholder.update()
                            updated = true
                        } catch (t: Throwable) {
                            VirtusCore.console().sendMessage("Error: ${t.message}")
                        }
                    }
                }
                if(updated) {
                    plugin.tick()
                }
                elapsedTenthsOfSecond++
            }, 5L, 2L)

        }
    }
}