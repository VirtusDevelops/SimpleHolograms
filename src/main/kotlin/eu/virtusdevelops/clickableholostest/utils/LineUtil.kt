package eu.virtusdevelops.clickableholostest.utils

import eu.virtusdevelops.clickableholostest.placeholder.PlaceholderRegistry

class LineUtil{
    companion object{
        fun containsPlaceholders(line: String): Boolean{
            for(placeholder in PlaceholderRegistry.placeholders){
                if(line.contains(placeholder.textPlaceholder, true)){
                    return true
                }
            }
            return false
        }
    }
}