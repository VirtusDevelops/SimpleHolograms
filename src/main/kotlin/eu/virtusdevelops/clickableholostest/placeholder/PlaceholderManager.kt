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
                for (placeholder in PlaceholderRegistry.placeholders) {
                    if (elapsedTenthsOfSecond % placeholder.tenthsToRefresh == 0L) {
                        try {
                            placeholder.update()
                        } catch (t: Throwable) {
                            VirtusCore.console().sendMessage("Error: ${t.message}")
                        }
                    }
                }
                plugin.tick()
                elapsedTenthsOfSecond++
            }, 5L, 2L)

        }
    }
}