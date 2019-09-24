package de.jonas.spring.tabcontent;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.function.ValueProvider;
import de.jonas.spring.model.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


public class MetatypeContent extends VerticalLayout {
    private Map<Priority, List<Checkbox>> checkboxByPriority = new HashMap<>();
    private Map<Prioritizable, List<Checkbox>> checkboxesByPrioritizable = new HashMap<>();
    private ComboBox<Metatype> metatypeComboBox = new ComboBox<>();
    private ComboBox<AwokenType> awokenTypeComboBox = new ComboBox<>();
    private Priority oldMagicPriority;
    private Priority oldMetatypePriority;


    public MetatypeContent(PlayerCharacter playerCharacter) {
        addPriorityGrid(playerCharacter);

        metatypeComboBox.setLabel("Metatyp");
        add(metatypeComboBox);

        awokenTypeComboBox.setLabel("Magie oder Resonanz");
        add(awokenTypeComboBox);
        updatePriorityDependantComponents(playerCharacter);

        Binder<PlayerCharacter> playerBinder = new Binder<>();
        playerBinder.bind(metatypeComboBox, PlayerCharacter::getMetatype, PlayerCharacter::setMetatype);
        playerBinder.bind(awokenTypeComboBox, PlayerCharacter::getAwokenType, PlayerCharacter::setAwokenType);
        playerBinder.setBean(playerCharacter);

        addAttributeGrid(playerCharacter, playerBinder);
    }

    private void addAttributeGrid(PlayerCharacter playerCharacter, Binder<PlayerCharacter> playerBinder) {
        Grid<Attributes> attributesGrid = new Grid<>();
        attributesGrid.setItems(Collections.singletonList(playerCharacter.getBoughtAttributes()));
        attributesGrid.addComponentColumn(attributes -> new AttributeComboBox(Attributes::getConstitution, Attributes::setConstitution, playerCharacter, playerBinder))
                .setHeader("Konstitution");
    }

    private class AttributeComboBox extends ComboBox<Integer> {
        public AttributeComboBox(Function<Attributes, Integer> attributeGetter, Setter<Attributes, Integer> attributeSetter, PlayerCharacter player, Binder<PlayerCharacter> playerBinder) {
            List<Integer> availableAttributes = new ArrayList<>();
            for (int i = attributeGetter.apply(player.getMetatype().getStartingAttributes()); i <= attributeGetter.apply(player.getMetatype().getAttributeLimits()); i++) {
                availableAttributes.add(i);
            }
            setItems(availableAttributes);
            playerBinder.bind(this, (ValueProvider<PlayerCharacter, Integer>) playerCharacter -> attributeGetter.apply(player.getBoughtAttributes()) - attributeGetter.apply(player.getMetatype().getStartingAttributes())
                    , (Setter<PlayerCharacter, Integer>) (playerCharacter, totalAttributes) -> attributeSetter.accept(player.getBoughtAttributes(), totalAttributes - attributeGetter.apply(player.getMetatype().getStartingAttributes())));
        }

    }

    private void updatePriorityDependantComponents(PlayerCharacter playerCharacter) {
        Priority metatypePriority = playerCharacter.getPriority(Prioritizable.METATYPE);
        if (metatypePriority != null) {
            if (oldMetatypePriority == null || metatypePriority != oldMetatypePriority) {
                List<Metatype> newItems = Arrays.stream(metatypePriority.getMetatypes()).map(MetatypeWithSpecialAttributes::getMetatype).collect(Collectors.toList());
                Metatype currentValue = metatypeComboBox.getValue();
                metatypeComboBox.setItems(newItems);
                metatypeComboBox.setEnabled(true);
                if (newItems.contains(currentValue)) {
                    metatypeComboBox.setValue(currentValue);
                }
            }
        } else {
            metatypeComboBox.setEnabled(false);
        }
        metatypeComboBox.setEnabled(metatypePriority != null);
        oldMetatypePriority = metatypePriority;

        Priority magicPriority = playerCharacter.getPriority(Prioritizable.MAGIC_OR_RESONANZ);
        if (magicPriority != null) {
            if (oldMagicPriority == null || magicPriority != oldMagicPriority) {
                List<AwokenType> newItems = Arrays.stream(magicPriority.getMagicOrResonances()).map(MagicOrResonance::getAwokenType).collect(Collectors.toList());
                AwokenType currentValue = awokenTypeComboBox.getValue();
                awokenTypeComboBox.setItems(newItems);
                awokenTypeComboBox.setEnabled(true);
                if (newItems.contains(currentValue)) {
                    awokenTypeComboBox.setValue(currentValue);
                }
            }
        } else {
            awokenTypeComboBox.setEnabled(false);
        }
        awokenTypeComboBox.setEnabled(magicPriority != null);
        oldMagicPriority = magicPriority;
    }

    private void addPriorityGrid(PlayerCharacter playerCharacter) {
        Grid<Priority> priorityGrid = new Grid<>();
        priorityGrid.setItems(Priority.values());

        priorityGrid.addColumn(Priority::name).setHeader("Priorität");
        priorityGrid.addComponentColumn(priority ->
                new PriorityCheckbox(priority.getMetatypesDescription(), playerCharacter, priority, Prioritizable.METATYPE)
        ).setHeader(Prioritizable.METATYPE.getLabel());
        priorityGrid.addComponentColumn(priority ->
                new PriorityCheckbox(String.valueOf(priority.getAttributePoints()), playerCharacter, priority, Prioritizable.ATTRIBUTES)
        ).setHeader(Prioritizable.ATTRIBUTES.getLabel());
        priorityGrid.addComponentColumn(priority ->
                new PriorityCheckbox(priority.getMagicOrMetatypeDescription(), playerCharacter, priority, Prioritizable.MAGIC_OR_RESONANZ)
        ).setHeader(Prioritizable.MAGIC_OR_RESONANZ.getLabel());
        priorityGrid.addComponentColumn(priority ->
                new PriorityCheckbox(priority.getStartingAbilityPoints().toString(), playerCharacter, priority, Prioritizable.ABILITY_POINTS)
        ).setHeader(Prioritizable.ABILITY_POINTS.getLabel());
        priorityGrid.addComponentColumn(priority ->
                new PriorityCheckbox(priority.getResources() + " $", playerCharacter, priority, Prioritizable.RESOURCES)
        ).setHeader(Prioritizable.RESOURCES.getLabel());

        add(priorityGrid);
    }

    private class PriorityCheckbox extends HorizontalLayout {
        PriorityCheckbox(String labelText, PlayerCharacter playerCharacter, Priority priority, Prioritizable prioritizable) {
            super();
            setDefaultVerticalComponentAlignment(Alignment.CENTER);

            Checkbox checkbox = new Checkbox();

            checkboxByPriority.putIfAbsent(priority, new ArrayList<>());
            checkboxByPriority.get(priority).add(checkbox);

            checkboxesByPrioritizable.putIfAbsent(prioritizable, new ArrayList<>());
            checkboxesByPrioritizable.get(prioritizable).add(checkbox);

            checkbox.addValueChangeListener(event -> {
                if (checkboxesByPrioritizable.get(prioritizable) != null && checkboxesByPrioritizable.get(prioritizable).stream().allMatch(checkbox1 -> checkbox1.getValue() == false)) {
                    playerCharacter.setPriority(prioritizable, null);
                }
                ;

                if (event.getValue()) {
                    playerCharacter.setPriority(prioritizable, priority);
                    checkboxByPriority.get(priority).forEach(priorityCheckbox -> {
                        if (priorityCheckbox != checkbox) {
                            priorityCheckbox.setValue(false);
                        }
                    });

                    checkboxesByPrioritizable.get(prioritizable).forEach(prioritizableCheckbox -> {
                        if (prioritizableCheckbox != checkbox) {
                            prioritizableCheckbox.setValue(false);
                        }
                    });
                }
                updatePriorityDependantComponents(playerCharacter);
            });

            add(checkbox);
            Html label = new Html("<span>" + labelText + "</span>");
            add(label);
        }
    }
}
