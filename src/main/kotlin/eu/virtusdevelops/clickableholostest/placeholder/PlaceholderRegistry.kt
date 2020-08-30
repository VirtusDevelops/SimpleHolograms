package eu.virtusdevelops.clickableholostest.placeholder

import org.bukkit.plugin.java.JavaPlugin


class PlaceholderRegistry{
    companion object{
        val placeholders = mutableSetOf<Placeholder>()



        fun registerPlaceholder(placeholder: Placeholder){
            placeholders.add(placeholder)
        }

        fun unregister(plugin: JavaPlugin, placeholderText: String): Boolean{

            val iter: Iterator<Placeholder> = placeholders.iterator()

            while (iter.hasNext()) {
                val placeholder = iter.next()
                if (placeholder.owner == plugin && placeholder.textPlaceholder == placeholderText) {
                    placeholders.remove(placeholder)
                    return true
                }
            }
            return false;
        }
    }
}