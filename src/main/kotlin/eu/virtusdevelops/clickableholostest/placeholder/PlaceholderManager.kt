package eu.virtusdevelops.clickableholostest.placeholder

import eu.virtusdevelops.clickableholostest.ClickableHolosTest
import eu.virtusdevelops.virtuscore.VirtusCore
import org.bukkit.Bukkit

class PlaceholderManager {



    companion object {
        private var elapsedTenthsOfSecond: Long = 0


        fun load(plugin: ClickableHolosTest) {


            Bukkit.getScheduler().runTaskTimer(plugin, Runnable {
                var updated = false


                for (placeholder in PlaceholderRegistry.placeholders) {
                    if (elapsedTenthsOfSecond % placeholder.tenthsToRefresh == 0L) {
                        placeholder.update()
                        updated = true
                    }
                }

                if(updated) {
                    plugin.tick(elapsedTenthsOfSecond)
                }
                elapsedTenthsOfSecond++
            }, 2L, 2L)

        }
    }
}