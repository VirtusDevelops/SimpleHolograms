package eu.virtusdevelops.clickableholostest.placeholder

interface PlaceholderReplacer {

    /**
     * Called to update a placeholder's replacement.
     * @return the replacement
     */
    fun update() : String
}