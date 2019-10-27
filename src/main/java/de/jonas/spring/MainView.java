package de.jonas.spring;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import de.jonas.spring.model.PlayerCharacter;
import de.jonas.spring.tabcontent.ConceptContent;
import de.jonas.spring.tabcontent.MagicOrResonanceContent;
import de.jonas.spring.tabcontent.MetaTypeContent;
import de.jonas.spring.tabcontent.SkillContent;

import java.util.HashMap;
import java.util.Map;

@Route
@PWA(name = "Shadowrun 5 Character Creator", shortName = "Character Creator")
public class MainView extends VerticalLayout {
    private Map<Tab, Component> tabContents = new HashMap<>();

    public MainView() {
        setHeightFull();
        setSpacing(false);
        setMargin(false);

        PlayerCharacter playerCharacter = new PlayerCharacter();
        Binder<PlayerCharacter> playerBinder = new Binder<>();
        playerBinder.setBean(playerCharacter);

        Tabs tabs = new Tabs();

        Tab conceptTab = new Tab("Konzept");
        tabContents.put(conceptTab, new ConceptContent(playerBinder));
        Tab metatypeTab = new Tab("Metatyp");
        tabContents.put(metatypeTab, new MetaTypeContent(playerBinder));
        Tab magicOrResonanceTab = new Tab("Magie oder Resonanz");
        tabContents.put(magicOrResonanceTab, new MagicOrResonanceContent(playerBinder));
        Tab skillTab = new Tab("Fertigkeiten");
        tabContents.put(skillTab, new SkillContent(playerBinder));
        Tab advantagesTab = new Tab("Vor- und Nachteile");
        tabContents.put(advantagesTab, new Label("Muss noch programmiert werden"));
        Tab resourceTab = new Tab("Resourcen");
        tabContents.put(resourceTab, new Label("Muss noch programmiert werden"));

        tabs.add(conceptTab, metatypeTab, magicOrResonanceTab, skillTab, advantagesTab, resourceTab);
        tabs.setOrientation(Tabs.Orientation.HORIZONTAL);

        tabs.addSelectedChangeListener(event -> {
            tabContents.values().forEach(content -> content.setVisible(false));
            tabContents.get(tabs.getSelectedTab()).setVisible(true);
        });

        add(tabs);
        tabContents.values().forEach(this::add);
        tabContents.values().forEach(content -> content.setVisible(false));
        tabContents.get(conceptTab).setVisible(true);
    }

}
