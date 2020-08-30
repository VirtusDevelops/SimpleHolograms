package eu.virtusdevelops.clickableholostest.placeholder

import org.bukkit.plugin.java.JavaPlugin

data class Placeholder(
    val owner: JavaPlugin,
    val textPlaceholder: String,
    val refreshRate: Double,
    var replacer: PlaceholderReplacer){

    var currentReplacement: String = ""
    var tenthsToRefresh: Int = 0

    init {
        tenthsToRefresh = if (refreshRate <= 0.1) 1 else (refreshRate * 10.0).toInt()
    }

    fun update(){
        setCurrentText(replacer.update())
    }

    fun setCurrentText(replacement: String?){
        currentReplacement = replacement ?: "null"
    }

    override fun equals(obj: Any?): Boolean {
        if(obj == null){
            return false
        }

        if(obj is Placeholder)
            return obj.textPlaceholder.equals(textPlaceholder)
        return false
    }

}