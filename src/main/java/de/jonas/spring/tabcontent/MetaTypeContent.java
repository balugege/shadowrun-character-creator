package de.jonas.spring.tabcontent;

import com.vaadin.flow.component.AbstractField;
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


public class MetaTypeContent extends VerticalLayout {
    private Map<Priority, List<Checkbox>> checkboxByPriority = new HashMap<>();
    private Map<Prioritizable, List<Checkbox>> checkboxesByPrioritizable = new HashMap<>();
    private ComboBox<Metatype> metatypeComboBox = new ComboBox<>();
    private ComboBox<AwokenType> awokenTypeComboBox = new ComboBox<>();
    private Priority oldMagicPriority;
    private Priority oldMetatypePriority;
    private List<AttributeComboBox> attributeComboBoxes = new ArrayList<>();


    public MetaTypeContent(Binder<PlayerCharacter> binder) {

        addPriorityGrid(binder.getBean());

        addMetaTypeComboBox(binder);

        awokenTypeComboBox.setLabel("Magie oder Resonanz");
        binder.bind(awokenTypeComboBox, PlayerCharacter::getAwokenType, PlayerCharacter::setAwokenType);
        add(awokenTypeComboBox);

        addAttributeGrid(binder);

        updatePriorityDependantComponents(binder.getBean());
    }

    private void addMetaTypeComboBox(Binder<PlayerCharacter> playerBinder) {
        metatypeComboBox.setLabel("Metatyp");
        playerBinder.bind(metatypeComboBox, PlayerCharacter::getMetatype, PlayerCharacter::setMetatype);
        add(metatypeComboBox);
    }

    private void addAttributeGrid(Binder<PlayerCharacter> binder) {
        Grid<Attributes> attributesGrid = new Grid<>();
        attributesGrid.setItems(Collections.singletonList(binder.getBean().getBoughtAttributes()));

        AttributeComboBox constitutionComboBox = new AttributeComboBox(Attributes::getConstitution, Attributes::setConstitution, binder);
        attributesGrid.addComponentColumn(attributes -> constitutionComboBox)
                .setHeader("Konstitution");
        attributeComboBoxes.add(constitutionComboBox);

        AttributeComboBox agilityComboBox = new AttributeComboBox(Attributes::getAgility, Attributes::setAgility, binder);
        attributesGrid.addComponentColumn(attributes -> agilityComboBox)
                .setHeader("Agilität");
        attributeComboBoxes.add(agilityComboBox);

        AttributeComboBox reactivityComboBox = new AttributeComboBox(Attributes::getReactivity, Attributes::setReactivity, binder);
        attributesGrid.addComponentColumn(attributes -> reactivityComboBox)
                .setHeader("Reaktion");
        attributeComboBoxes.add(reactivityComboBox);

        AttributeComboBox strengthComboBox = new AttributeComboBox(Attributes::getStrength, Attributes::setStrength, binder);
        attributesGrid.addComponentColumn(attributes -> strengthComboBox)
                .setHeader("Stärke");
        attributeComboBoxes.add(strengthComboBox);

        AttributeComboBox willpowerComboBox = new AttributeComboBox(Attributes::getWillpower, Attributes::setWillpower, binder);
        attributesGrid.addComponentColumn(attributes -> willpowerComboBox)
                .setHeader("Willenskraft");
        attributeComboBoxes.add(willpowerComboBox);

        AttributeComboBox logicComboBox = new AttributeComboBox(Attributes::getLogic, Attributes::setLogic, binder);
        attributesGrid.addComponentColumn(attributes -> logicComboBox)
                .setHeader("Willenskraft");
        attributeComboBoxes.add(logicComboBox);

        AttributeComboBox intelligenceComboBox = new AttributeComboBox(Attributes::getIntelligence, Attributes::setIntelligence, binder);
        attributesGrid.addComponentColumn(attributes -> intelligenceComboBox)
                .setHeader("Intelligenz");
        attributeComboBoxes.add(intelligenceComboBox);

        AttributeComboBox charismaComboBox = new AttributeComboBox(Attributes::getCharisma, Attributes::setCharisma, binder);
        attributesGrid.addComponentColumn(attributes -> charismaComboBox)
                .setHeader("Charisma");
        attributeComboBoxes.add(charismaComboBox);

        AttributeComboBox edgeComboBox = new AttributeComboBox(Attributes::getEdge, Attributes::setEdge, binder);
        attributesGrid.addComponentColumn(attributes -> edgeComboBox)
                .setHeader("Edge");
        attributeComboBoxes.add(edgeComboBox);

        attributesGrid.setHeight("140px");
        attributesGrid.setSelectionMode(Grid.SelectionMode.NONE);

        add(attributesGrid);
    }

    private class AttributeComboBox extends ComboBox<Integer> {
        private final Function<Attributes, Integer> attributeGetter;
        private final Setter<Attributes, Integer> attributeSetter;
        private final Binder<PlayerCharacter> playerBinder;
        private Binder.Binding<PlayerCharacter, Integer> binding;

        AttributeComboBox(Function<Attributes, Integer> attributeGetter, Setter<Attributes, Integer> attributeSetter, Binder<PlayerCharacter> playerBinder) {
            this.attributeGetter = attributeGetter;
            this.attributeSetter = attributeSetter;
            this.playerBinder = playerBinder;

            setErrorMessage("Nicht genügend verfügbare Attributpunkte. Ändere die Attributpriorität");
            setAllowCustomValue(false);
            playerBinder.addValueChangeListener(event -> {
                if (event.getValue() != null) {
                    update();
                }
            });
            update();
        }

        void update() {
            PlayerCharacter player = playerBinder.getBean();
            boolean isEnabled = player.getMetatype() != null && player.getPriority(Prioritizable.ATTRIBUTES) != null;
            setEnabled(isEnabled);
            if (isEnabled) {
                List<Integer> availableAttributes = new ArrayList<>();
                int attributeStart = attributeGetter.apply(player.getMetatype().getStartingAttributes());
                int attributeMaximum = attributeGetter.apply(player.getMetatype().getAttributeLimits());
                for (int i = attributeStart; i <= attributeMaximum; i++) {
                    availableAttributes.add(i);
                }
                if (!availableAttributes.isEmpty()) {
                    setItems(availableAttributes);
                    if (binding != null) {
                        binding.unbind();
                    }
                    binding = playerBinder.bind(this,
                            (ValueProvider<PlayerCharacter, Integer>) playerCharacter -> {
                                return attributeGetter.apply(player.getBoughtAttributes()) +
                                        attributeGetter.apply(player.getMetatype().getStartingAttributes());
                            }
                            , (Setter<PlayerCharacter, Integer>) (playerCharacter, totalAttributes) -> {
                                if (totalAttributes != null) {
                                    attributeSetter.accept(
                                            player.getBoughtAttributes(),
                                            totalAttributes -
                                                    attributeGetter.apply(player.getMetatype().getStartingAttributes())
                                    );
                                }
                            });
                }

                int spendablePoints = player.getPriority(Prioritizable.ATTRIBUTES).getAttributePoints() - player.getBoughtAttributes().getSumOfBuyable();
                setInvalid(getValue() > attributeStart + spendablePoints);
            }
        }
    }

    private void updatePriorityDependantComponents(PlayerCharacter playerCharacter) {
        Priority metaTypePriority = playerCharacter.getPriority(Prioritizable.METATYPE);
        if (metaTypePriority != null) {
            if (oldMetatypePriority == null || metaTypePriority != oldMetatypePriority) {
                List<Metatype> newItems = Arrays.stream(metaTypePriority.getMetatypes()).map(MetatypeWithSpecialAttributes::getMetatype).collect(Collectors.toList());
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
        metatypeComboBox.setEnabled(metaTypePriority != null);
        oldMetatypePriority = metaTypePriority;

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

        for (AttributeComboBox attributeComboBox : attributeComboBoxes) {
            attributeComboBox.update();
        }
    }

    private void addPriorityGrid(PlayerCharacter playerCharacter) {
        Grid<Priority> priorityGrid = new Grid<>();
        priorityGrid.setItems(Priority.values());
        priorityGrid.setHeight("520px");
        priorityGrid.setSelectionMode(Grid.SelectionMode.NONE);

        priorityGrid.addColumn(Priority::name).setHeader("Priorität").setFlexGrow(0);
        priorityGrid.addComponentColumn(priority ->
                new PriorityCheckbox(priority.getMetatypesDescription(), playerCharacter, priority, Prioritizable.METATYPE)
        ).setHeader(Prioritizable.METATYPE.getLabel()).setFlexGrow(3);
        priorityGrid.addComponentColumn(priority ->
                new PriorityCheckbox(String.valueOf(priority.getAttributePoints()), playerCharacter, priority, Prioritizable.ATTRIBUTES)
        ).setHeader(Prioritizable.ATTRIBUTES.getLabel()).setFlexGrow(0);
        priorityGrid.addComponentColumn(priority ->
                new PriorityCheckbox(priority.getMagicOrResonanceDescription(), playerCharacter, priority, Prioritizable.MAGIC_OR_RESONANZ)
        ).setHeader(Prioritizable.MAGIC_OR_RESONANZ.getLabel()).setFlexGrow(15);
        priorityGrid.addComponentColumn(priority ->
                new PriorityCheckbox(priority.getStartingAbilityPoints().toString(), playerCharacter, priority, Prioritizable.ABILITY_POINTS)
        ).setHeader(Prioritizable.ABILITY_POINTS.getLabel()).setFlexGrow(4);
        priorityGrid.addComponentColumn(priority ->
                new PriorityCheckbox(priority.getResources() + " $", playerCharacter, priority, Prioritizable.RESOURCES)
        ).setHeader(Prioritizable.RESOURCES.getLabel()).setFlexGrow(1);

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
                if (checkboxesByPrioritizable.get(prioritizable) != null && checkboxesByPrioritizable.get(prioritizable).stream().noneMatch(AbstractField::getValue)) {
                    playerCharacter.setPriority(prioritizable, null);
                }

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
