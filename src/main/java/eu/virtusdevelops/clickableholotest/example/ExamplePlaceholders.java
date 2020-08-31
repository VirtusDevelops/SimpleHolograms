package eu.virtusdevelops.clickableholotest.example;

import eu.virtusdevelops.clickableholostest.ClickableHolosTest;
import eu.virtusdevelops.clickableholostest.placeholder.Placeholder;
import eu.virtusdevelops.clickableholostest.placeholder.PlaceholderRegistry;
import eu.virtusdevelops.clickableholostest.placeholder.PlaceholderReplacer;
import org.jetbrains.annotations.NotNull;

public class ExamplePlaceholders {

    private ClickableHolosTest plugin;
    private int current = 0;
    private int thisda = 0;

    public ExamplePlaceholders(ClickableHolosTest plugin){
        this.plugin = plugin;

        registerPlaceholders();
    }


    public void registerPlaceholders(){
        PlaceholderRegistry.Companion.registerPlaceholder(new Placeholder(plugin, "{TEST8}", 10.0, this::getValue));

        PlaceholderRegistry.Companion.registerPlaceholder(new Placeholder(plugin, "{COUNTER}", 0.1, this::getValue));
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
