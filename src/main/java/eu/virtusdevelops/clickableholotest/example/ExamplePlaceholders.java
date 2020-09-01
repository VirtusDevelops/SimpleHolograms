package eu.virtusdevelops.clickableholotest.example;

import eu.virtusdevelops.clickableholostest.ClickableHolosTest;
import eu.virtusdevelops.clickableholostest.placeholder.Placeholder;
import eu.virtusdevelops.clickableholostest.placeholder.PlaceholderRegistry;

public class ExamplePlaceholders {

    private ClickableHolosTest plugin;
    private int current = 0;
    private int thisda = 0;

    public ExamplePlaceholders(ClickableHolosTest plugin){
        this.plugin = plugin;

        registerPlaceholders();
    }


    public void registerPlaceholders(){
        PlaceholderRegistry.Companion.registerPlaceholder(new Placeholder(plugin, "{TEST8}", 0.5, this::getRandom));

        PlaceholderRegistry.Companion.registerPlaceholder(new Placeholder(plugin, "{COUNTER}", 1, this::getValue));
    }

    public String getValue(){
        current++;
        return "Current: " + current;
    }
    private String getRandom(){
        thisda++;
        return "This: " + thisda;
    }

}
