package eu.virtusdevelops.clickableholostest.API

interface PlaceholderReplacer {

    /**
     * Called to update a placeholder's replacement.
     * @return the replacement
     */
    fun update() : String?
}