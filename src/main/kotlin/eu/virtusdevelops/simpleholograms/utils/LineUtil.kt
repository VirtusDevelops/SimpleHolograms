package eu.virtusdevelops.simpleholograms.utils

import eu.virtusdevelops.simpleholograms.placeholder.PlaceholderRegistry

class LineUtil{
    companion object{
        fun containsPlaceholders(line: String): Int{
            for(placeholder in PlaceholderRegistry.placeholders){
                if(line.contains(placeholder.textPlaceholder, true)){
                    return placeholder.tenthsToRefresh
                }
            }
            return -1
        }
    }
}