package de.jonas.spring.tabcontent;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.function.ValueProvider;
import de.jonas.spring.model.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class MetaTypeContent extends VerticalLayout {
    private Map<Priority, List<Checkbox>> checkboxByPriority = new HashMap<>();
    private Map<Prioritizable, List<Checkbox>> checkboxesByPrioritizable = new HashMap<>();
    private ComboBox<Metatype> metatypeComboBox = new ComboBox<>();
    private ComboBox<AwokenType> awokenTypeComboBox = new ComboBox<>();
    private Priority oldMagicPriority;
    private Priority oldMetatypePriority;
    private List<AbstractAttributeComboBox> attributeComboBoxes = new ArrayList<>();

    public MetaTypeContent(Binder<PlayerCharacter> binder) {
        setPadding(false);

        addPriorityGrid(binder.getBean());

        addMetaTypeComboBox(binder);

        awokenTypeComboBox.setLabel("Erwachtheitstyp");
        binder.bind(awokenTypeComboBox, PlayerCharacter::getAwokenType, PlayerCharacter::setAwokenType);
        add(awokenTypeComboBox);

        addAttributeForm(binder);

        updatePriorityDependantComponents(binder.getBean());
    }

    private void addMetaTypeComboBox(Binder<PlayerCharacter> playerBinder) {
        metatypeComboBox.setLabel("Metatyp");
        playerBinder.bind(metatypeComboBox, PlayerCharacter::getMetatype, PlayerCharacter::setMetatype);
        add(metatypeComboBox);
    }

    private void addAttributeForm(Binder<PlayerCharacter> binder) {

        add(new Label("Attribute"));

        FormLayout attributesForm = new FormLayout();
        AttributeComboBox constitutionComboBox = new AttributeComboBox(Attributes::getConstitution, Attributes::setConstitution, binder);
        attributesForm.addFormItem(constitutionComboBox, "Konstitution");
        attributeComboBoxes.add(constitutionComboBox);

        AttributeComboBox agilityComboBox = new AttributeComboBox(Attributes::getAgility, Attributes::setAgility, binder);
        attributesForm.addFormItem(agilityComboBox, "Agilität");
        attributeComboBoxes.add(agilityComboBox);

        AttributeComboBox reactivityComboBox = new AttributeComboBox(Attributes::getReactivity, Attributes::setReactivity, binder);
        attributesForm.addFormItem(reactivityComboBox, "Reaktion");
        attributeComboBoxes.add(reactivityComboBox);

        AttributeComboBox strengthComboBox = new AttributeComboBox(Attributes::getStrength, Attributes::setStrength, binder);
        attributesForm.addFormItem(strengthComboBox, "Stärke");
        attributeComboBoxes.add(strengthComboBox);

        AttributeComboBox willpowerComboBox = new AttributeComboBox(Attributes::getWillpower, Attributes::setWillpower, binder);
        attributesForm.addFormItem(willpowerComboBox, "Willenskraft");
        attributeComboBoxes.add(willpowerComboBox);

        AttributeComboBox logicComboBox = new AttributeComboBox(Attributes::getLogic, Attributes::setLogic, binder);
        attributesForm.addFormItem(logicComboBox, "Logik");
        attributeComboBoxes.add(logicComboBox);

        AttributeComboBox intelligenceComboBox = new AttributeComboBox(Attributes::getIntelligence, Attributes::setIntelligence, binder);
        attributesForm.addFormItem(intelligenceComboBox, "Intelligenz");
        attributeComboBoxes.add(intelligenceComboBox);

        AttributeComboBox charismaComboBox = new AttributeComboBox(Attributes::getCharisma, Attributes::setCharisma, binder);
        attributesForm.addFormItem(charismaComboBox, "Charisma");
        attributeComboBoxes.add(charismaComboBox);

        add(attributesForm);

        add(new Label("Spezialattribute"));

        FormLayout specialAttributesForm = new FormLayout();

        SpecialAttributeComboBox edgeComboBox = new SpecialAttributeComboBox(Attributes::getEdge, Attributes::setEdge, binder, (player) -> player.getMetatype().getStartingAttributes().getEdge());
        attributeComboBoxes.add(edgeComboBox);
        specialAttributesForm.addFormItem(edgeComboBox, "Edge");

        SpecialAttributeComboBox magicOrResonanceComboBox = new SpecialAttributeComboBox(Attributes::getMagicOrResonance, Attributes::setMagicOrResonance, binder, player -> {
            if (player.getPriority(Prioritizable.MAGIC_OR_RESONANZ) == null) {
                return 0;
            }
            AwokenType awokenType = player.getAwokenType();
            if (awokenType == null) {
                return 0;
            }
            MagicOrResonance magicOrResonance = player.getPriority(Prioritizable.MAGIC_OR_RESONANZ).getMagicOrResonance(player.getAwokenType());
            if (awokenType == AwokenType.TECHNOMANCER) {
                return magicOrResonance.getResonanceAttributeLevel();
            } else {
                return magicOrResonance.getMagicAttributeLevel();
            }
        }) {
            @Override
            protected boolean getEnabledStatus(PlayerCharacter player) {
                return super.getEnabledStatus(player) && player.getAwokenType() != null;
            }
        };
        attributeComboBoxes.add(magicOrResonanceComboBox);
        Label magicOrResonanceComboBoxLabel = new Label("Magie");
        specialAttributesForm.addFormItem(magicOrResonanceComboBox, magicOrResonanceComboBoxLabel);

        binder.addValueChangeListener(event -> {
            magicOrResonanceComboBoxLabel.setText(binder.getBean().getAwokenType() == AwokenType.TECHNOMANCER ? "Resonanz" : "Magie");
        });

        add(specialAttributesForm);
    }

    private static abstract class AbstractAttributeComboBox extends ComboBox<Integer> {
        final Binder<PlayerCharacter> playerBinder;
        private final Function<Attributes, Integer> attributeGetter;
        private CallbackDataProvider<Integer, String> dataProvider;

        AbstractAttributeComboBox(Function<Attributes, Integer> attributeGetter, Setter<Attributes, Integer> attributeSetter, Binder<PlayerCharacter> playerBinder) {
            this.playerBinder = playerBinder;
            this.attributeGetter = attributeGetter;

            setAllowCustomValue(false);

            dataProvider = DataProvider.fromFilteringCallbacks(query -> {
                if (!getEnabledStatus(playerBinder.getBean())) {
                    query.getLimit();
                    query.getOffset();
                    return Stream.of();
                }
                return getSelectableAttributes(attributeGetter, query);
            }, query -> {
                if (!getEnabledStatus(playerBinder.getBean())) {
                    query.getLimit();
                    query.getOffset();
                    return 0;
                }
                return Math.max(0, (int) getSelectableAttributes(attributeGetter, query).count());
            });
            setDataProvider(dataProvider);
            playerBinder.addValueChangeListener(event -> {
                refreshEntries();
            });
            refreshEntries();
        }

        void refreshEntries() {
            boolean isEnabled = getEnabledStatus(playerBinder.getBean());
            setEnabled(isEnabled);
            dataProvider.refreshAll();
            if (isEnabled) {
                List<Integer> selectableAttributes = getSelectableAttributes(attributeGetter, new Query<>()).collect(Collectors.toList());
                if (!selectableAttributes.contains(getValue())) {
                    setValue(null);
                }
            }
        }

        protected abstract boolean getEnabledStatus(PlayerCharacter player);

        protected abstract Stream<Integer> getSelectableAttributes(Function<Attributes, Integer> attributeGetter, Query<Integer, String> query);
    }

    private static class AttributeComboBox extends AbstractAttributeComboBox {
        AttributeComboBox(Function<Attributes, Integer> attributeGetter, Setter<Attributes, Integer> attributeSetter, Binder<PlayerCharacter> playerBinder) {
            super(attributeGetter, attributeSetter, playerBinder);
            playerBinder.bind(this,
                    (ValueProvider<PlayerCharacter, Integer>) player -> {
                        if (player.getMetatype() == null) {
                            return null;
                        }
                        return attributeGetter.apply(player.getBoughtAttributes()) +
                                attributeGetter.apply(player.getMetatype().getStartingAttributes());
                    }
                    , (Setter<PlayerCharacter, Integer>) (player, totalAttributes) -> {
                        if (totalAttributes == null) {
                            attributeSetter.accept(player.getBoughtAttributes(), 0);
                            return;
                        }
                        if (player.getMetatype() == null) {
                            return;
                        }
                        attributeSetter.accept(
                                player.getBoughtAttributes(),
                                totalAttributes -
                                        attributeGetter.apply(player.getMetatype().getStartingAttributes())
                        );
                    });
        }

        @Override
        protected boolean getEnabledStatus(PlayerCharacter player) {
            return player.getMetatype() != null && player.getPriority(Prioritizable.ATTRIBUTES) != null;
        }

        @Override
        public Stream<Integer> getSelectableAttributes(Function<Attributes, Integer> attributeGetter, Query<Integer, String> query) {
            Stream.Builder<Integer> builder = Stream.builder();

            PlayerCharacter player = playerBinder.getBean();
            int attributeStart = attributeGetter.apply(player.getMetatype().getStartingAttributes());
            int attributeMaximum = attributeGetter.apply(player.getMetatype().getAttributeLimits());
            Attributes totalAttributes = player.getMetatype().getStartingAttributes().getSumWith(player.getBoughtAttributes());
            if (totalAttributes.anyNonSpecialAttributeAtOrAbove(player.getMetatype().getAttributeLimits()) && attributeGetter.apply(totalAttributes) < attributeGetter.apply(player.getMetatype().getAttributeLimits())) {
                attributeMaximum -= 1;
            }
            int availablePoints = player.getPriority(Prioritizable.ATTRIBUTES).getAttributePoints()
                    - player.getBoughtAttributes().getSumOfNonSpecialAttributes() + attributeGetter.apply(player.getBoughtAttributes());
            int effectiveLimit = Math.min(Math.min(
                    attributeMaximum, availablePoints), Math.max(query.getLimit() - 1, query.getLimit() + attributeStart - 1)
            );
            for (int i = attributeStart + query.getOffset(); i <= effectiveLimit; i++) {
                builder.add(i);
            }

            return builder.build();
        }
    }

    private static class SpecialAttributeComboBox extends AbstractAttributeComboBox {
        private final Function<PlayerCharacter, Integer> baseValueSupplier;

        SpecialAttributeComboBox(Function<Attributes, Integer> attributeGetter, Setter<Attributes, Integer> attributeSetter, Binder<PlayerCharacter> playerBinder, Function<PlayerCharacter, Integer> baseValueSupplier) {
            super(attributeGetter, attributeSetter, playerBinder);

            this.baseValueSupplier = baseValueSupplier;

            playerBinder.bind(this, player -> {
                if (player.getMetatype() == null) {
                    return null;
                }
                return attributeGetter.apply(player.getBoughtAttributes()) + baseValueSupplier.apply(player);
            }, (player, totalValue) -> {
                if (totalValue == null) {
                    attributeSetter.accept(player.getBoughtAttributes(), 0);
                    return;
                }
                if (player.getMetatype() == null) {
                    return;
                }
                attributeSetter.accept(player.getBoughtAttributes(), totalValue - baseValueSupplier.apply(player));
            });
        }

        @Override
        protected boolean getEnabledStatus(PlayerCharacter player) {
            return player.getPriority(Prioritizable.METATYPE) != null && player.getMetatype() != null;
        }

        @Override
        protected Stream<Integer> getSelectableAttributes(Function<Attributes, Integer> attributeGetter, Query<Integer, String> query) {
            Metatype metatype = playerBinder.getBean().getMetatype();
            Priority metaTypePriority = playerBinder.getBean().getPriority(Prioritizable.METATYPE);
            if (metatype == null || metaTypePriority == null) {
                return Stream.of();
            }

            int baseValue = baseValueSupplier.apply(playerBinder.getBean());
            int maxValue = attributeGetter.apply(metatype.getAttributeLimits());
            int availablePoints = metaTypePriority.getMetaType(metatype).getSpecialAttributePoints() - playerBinder.getBean().getBoughtAttributes().getSumOfSpecialAttributes() + attributeGetter.apply(playerBinder.getBean().getBoughtAttributes());
            int effectiveLimit = Math.min(Math.min(
                    maxValue, availablePoints + baseValue), Math.max(query.getLimit() - 1, query.getLimit() + baseValue - 1)
            );

            Stream.Builder<Integer> builder = Stream.builder();

            for (int i = baseValue + query.getOffset(); i <= effectiveLimit; i++) {
                builder.add(i);
            }

            return builder.build();
        }
    }

    private void updatePriorityDependantComponents(PlayerCharacter playerCharacter) {
        Priority metaTypePriority = playerCharacter.getPriority(Prioritizable.METATYPE);
        if (metaTypePriority != null) {
            if (oldMetatypePriority == null || metaTypePriority != oldMetatypePriority) {
                List<Metatype> newItems = Arrays.stream(metaTypePriority.getMetaTypes()).map(MetaTypeWithSpecialAttributes::getMetatype).collect(Collectors.toList());
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

        for (AbstractAttributeComboBox attributeComboBox : attributeComboBoxes) {
            attributeComboBox.refreshEntries();
        }
    }


    private void addPriorityGrid(PlayerCharacter playerCharacter) {
        add(new Label("Prioritäten"));

        Grid<Priority> priorityGrid = new Grid<>();
        priorityGrid.setItems(Priority.values());
        priorityGrid.setHeight("520px");
        priorityGrid.setMinWidth("1140px");
        priorityGrid.setSelectionMode(Grid.SelectionMode.NONE);

        priorityGrid.addComponentColumn(priority ->
                new PriorityCheckbox(priority.getMetatypesDescription(), playerCharacter, priority, Prioritizable.METATYPE)
        ).setHeader(Prioritizable.METATYPE.getLabel()).setFlexGrow(1);
        priorityGrid.addComponentColumn(priority ->
                new PriorityCheckbox(String.valueOf(priority.getAttributePoints()), playerCharacter, priority, Prioritizable.ATTRIBUTES)
        ).setHeader(Prioritizable.ATTRIBUTES.getLabel()).setFlexGrow(0);
        priorityGrid.addComponentColumn(priority ->
                new PriorityCheckbox(priority.getMagicOrResonanceDescription(), playerCharacter, priority, Prioritizable.MAGIC_OR_RESONANZ)
        ).setHeader(Prioritizable.MAGIC_OR_RESONANZ.getLabel()).setFlexGrow(10);
        priorityGrid.addComponentColumn(priority ->
                new PriorityCheckbox(priority.getStartingAbilityPoints().toString(), playerCharacter, priority, Prioritizable.ABILITY_POINTS)
        ).setHeader(Prioritizable.ABILITY_POINTS.getLabel()).setFlexGrow(3);
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
