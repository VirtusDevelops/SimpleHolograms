package eu.virtusdevelops.simpleholograms.placeholder

import eu.virtusdevelops.simpleholograms.SimpleHolograms
import org.bukkit.Bukkit

class PlaceholderManager {



    companion object {
        private var elapsedTenthsOfSecond: Long = 0


        fun load(plugin: SimpleHolograms) {


            Bukkit.getScheduler().runTaskTimer(plugin, Runnable {
                for (placeholder in PlaceholderRegistry.placeholders) {
                    if (elapsedTenthsOfSecond % placeholder.tenthsToRefresh == 0L) {
                        placeholder.update()

                    }
                }
                elapsedTenthsOfSecond++
            }, 2L, 2L)

        }
    }
}