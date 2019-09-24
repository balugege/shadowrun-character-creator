package de.jonas.spring;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import de.jonas.spring.model.PlayerCharacter;
import de.jonas.spring.tabcontent.ConceptContent;
import de.jonas.spring.tabcontent.MetatypeContent;

import java.util.HashMap;
import java.util.Map;

@Route
@PWA(name = "Shadowrun 5 Character Creator", shortName = "Character Creator")
public class MainView extends VerticalLayout {
    private Map<Tab, Component> tabContents = new HashMap<>();

    public MainView() {
        PlayerCharacter playerCharacter = new PlayerCharacter();

        Tabs tabs = new Tabs();

        Tab conceptTab = new Tab("Konzept");
        tabContents.put(conceptTab, new ConceptContent(playerCharacter));
        Tab metatypeTab = new Tab("Metatyp");
        tabContents.put(metatypeTab, new MetatypeContent(playerCharacter));
        Tab tab3 = new Tab("Magie und Resonanz");

        tabs.add(conceptTab, metatypeTab, tab3);
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
