package de.jonas.spring.tabcontent;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;
import de.jonas.spring.components.ListSelection;
import de.jonas.spring.model.PlayerCharacter;
import de.jonas.spring.model.Prioritizable;
import de.jonas.spring.model.Priority;
import de.jonas.spring.model.StartingAbilityPoints;
import de.jonas.spring.model.skills.KnowledgeSkill;
import de.jonas.spring.model.skills.PhysicalSkill;
import de.jonas.spring.model.skills.PhysicalSkillGroup;
import de.jonas.spring.model.skills.Skill;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SkillContent extends VerticalLayout {
    private ListSelection<PhysicalSkill> physicalSkillSelection;
    private Grid<KnowledgeSkill> knowledgeSkillGrid;
    private TextField knowledgeSkillTextField;
    private Label noAbilitiesHint = new Label("Dein Charakter hat seine Fertigkeitspunkte noch nicht prioritisiert.");

    public SkillContent(Binder<PlayerCharacter> binder) {
        setPadding(false);

        build(binder);
        updateVisibility(binder);
        binder.addValueChangeListener(event -> updateVisibility(binder));
    }

    private void updateVisibility(Binder<PlayerCharacter> binder) {
        boolean canLearnMorePhysicalSkills = canLearnMorePhysicalSkills(binder);
        physicalSkillSelection.setVisible(canLearnMorePhysicalSkills);
        physicalSkillSelection.setEnabled(canLearnMorePhysicalSkills);
        physicalSkillSelection.setItems(EnumSet.allOf(PhysicalSkill.class));
        physicalSkillSelection.setSelectedItems(binder.getBean().getPhysicalSkills());

        boolean canLearnMoreKnowledgeSkills = canLearnMoreKnowledgeSkills(binder);
        knowledgeSkillGrid.setItems(binder.getBean().getKnowledgeSkills());
        knowledgeSkillGrid.setVisible(canLearnMoreKnowledgeSkills);
        knowledgeSkillTextField.setVisible(canLearnMoreKnowledgeSkills);
        knowledgeSkillTextField.setEnabled(canLearnMoreKnowledgeSkills);

        noAbilitiesHint.setVisible(!canLearnMoreKnowledgeSkills || !canLearnMorePhysicalSkills);
    }

    private void build(Binder<PlayerCharacter> binder) {
        add(noAbilitiesHint);
        addPhysicalSkillSelector(binder);
        addKnowledgeSkillSelector(binder);
    }

    private void addPhysicalSkillSelector(Binder<PlayerCharacter> binder) {
        physicalSkillSelection = new ListSelection<>("Fähigkeit");
        physicalSkillSelection.setLabel("Aktionsfertigkeiten (S. 130)");

        physicalSkillSelection.addComponentColumn(skill -> new SkillLevelComboBox(skill, binder)).setHeader("Stufe");

        physicalSkillSelection.addComponentColumn(skill -> {
            Button removeButton = new Button("Entfernen");
            removeButton.addClickListener(event -> {
                binder.getBean().forgetSkill(skill);
                List<PhysicalSkill> physicalSkills = binder.getBean().getPhysicalSkills();
                physicalSkillSelection.setSelectedItems(physicalSkills);
                physicalSkillSelection.setItems(EnumSet.allOf(PhysicalSkill.class).stream().filter(element -> !physicalSkills.contains(element)));
                physicalSkillSelection.setEnabled(canLearnMorePhysicalSkills(binder));
            });
            return removeButton;
        }).setHeader("");

        physicalSkillSelection.onSelect(event -> {
            if (event.getValue() != null) {
                binder.getBean().learnSkill(event.getValue(), 1);
                List<PhysicalSkill> learnedSkills = binder.getBean().getPhysicalSkills();
                physicalSkillSelection.setSelectedItems(learnedSkills);
                physicalSkillSelection.setItems(EnumSet.allOf(PhysicalSkill.class).stream().filter(element -> !learnedSkills.contains(element)));
                physicalSkillSelection.setEnabled(canLearnMorePhysicalSkills(binder));
            }
        });

        add(physicalSkillSelection);
    }

    private void addKnowledgeSkillSelector(Binder<PlayerCharacter> binder) {
        knowledgeSkillGrid = new Grid<>();
        knowledgeSkillTextField = new TextField();
        knowledgeSkillTextField.setWidth("40%");
        knowledgeSkillTextField.setLabel("Wissensfertigkeiten (S. 148)");

        knowledgeSkillGrid.setSelectionMode(Grid.SelectionMode.NONE);
        knowledgeSkillGrid.addColumn(Skill::toString).setHeader("Wissensfertigkeit");
        knowledgeSkillGrid.addComponentColumn(skill -> new SkillLevelComboBox(skill, binder)).setHeader("Stufe");
        knowledgeSkillGrid.addComponentColumn(skill -> {
            Button removeButton = new Button("Entfernen");
            removeButton.addClickListener(event -> {
                binder.getBean().forgetSkill(skill);
                List<KnowledgeSkill> knowledgeSkills = binder.getBean().getKnowledgeSkills();
                knowledgeSkillGrid.setItems(knowledgeSkills);
                knowledgeSkillTextField.setEnabled(canLearnMoreKnowledgeSkills(binder));
            });
            return removeButton;
        });

        knowledgeSkillTextField.addKeyPressListener(Key.ENTER, event -> {
            if (knowledgeSkillTextField.getValue() != null) {
                binder.getBean().learnSkill(new KnowledgeSkill(knowledgeSkillTextField.getValue()), 1);
                List<KnowledgeSkill> learnedKnowledgeSkills = binder.getBean().getKnowledgeSkills();
                knowledgeSkillGrid.setItems(learnedKnowledgeSkills);
                knowledgeSkillTextField.setEnabled(canLearnMoreKnowledgeSkills(binder));
                knowledgeSkillTextField.setValue("");
            }
        });

        add(knowledgeSkillTextField);
        add(knowledgeSkillGrid);
    }

    private boolean canLearnMoreKnowledgeSkills(Binder<PlayerCharacter> binder) {
        PlayerCharacter player = binder.getBean();
        if (player.getTotalAttributes() == null) {
            return false;
        }
        // page 75
        int availablePoints = 2 * (player.getTotalAttributes().getIntuition() + player.getTotalAttributes().getLogic());
        int spentPoints = player.getKnowledgeSkills().stream().mapToInt(player::getSkillLevel).sum();
        return spentPoints < availablePoints;
    }

    private boolean canLearnMorePhysicalSkills(Binder<PlayerCharacter> binder) {
        Priority priority = binder.getBean().getPriority(Prioritizable.ABILITY_POINTS);
        PlayerCharacter player = binder.getBean();
        if (priority == null || player.getTotalAttributes() == null) {
            return false;
        }
        int availablePoints = priority.getStartingAbilityPoints().getSingleAbilities();
        int spentPoints = player.getPhysicalSkills().stream().mapToInt(player::getSkillLevel).sum();
        return spentPoints < availablePoints;
    }

    private static class SkillLevelComboBox extends ComboBox<Integer> {
        private final Binder<PlayerCharacter> binder;

        SkillLevelComboBox(Skill skill, Binder<PlayerCharacter> binder) {
            this.binder = binder;

            CallbackDataProvider<Integer, String> dataProvider = DataProvider.fromFilteringCallbacks(
                    query -> getSelectableSkillLevels(skill, query),
                    query -> (int) getSelectableSkillLevels(skill, query).count()
            );
            setDataProvider(dataProvider);
            setValue(binder.getBean().getSkillLevel(skill));

            binder.bind(this, player -> player.getSkillLevel(skill), (player, value) -> {
                player.learnSkill(skill, value != null ? value : 1);
            });
            binder.addValueChangeListener(event -> {
                dataProvider.refreshAll();
            });
        }

        private Stream<Integer> getSelectableSkillLevels(Skill skill, Query<Integer, String> query) {
            PlayerCharacter player = binder.getBean();
            if (player.getPriority(Prioritizable.ABILITY_POINTS) == null) {
                return Stream.of(0);
            }

            Stream.Builder<Integer> builder = Stream.builder();

            int levelMaximum = 6; // TODO increase by one if advantage is present
            StartingAbilityPoints startingAbilityPoints = player.getPriority(Prioritizable.ABILITY_POINTS).getStartingAbilityPoints();
            int spentPoints;
            int availablePoints;
            if (skill instanceof PhysicalSkill) {
                availablePoints = startingAbilityPoints.getSingleAbilities();
                spentPoints = player.getPhysicalSkills().stream().mapToInt(player::getSkillLevel).sum();
            } else if (skill instanceof PhysicalSkillGroup) {
                availablePoints = startingAbilityPoints.getAbilityGroups();
                spentPoints = player.getPhysicalSkillGroups().stream().mapToInt(player::getSkillLevel).sum();
            } else if (skill instanceof KnowledgeSkill) {
                // page 75
                availablePoints = 2 * (player.getTotalAttributes().getIntuition() + player.getTotalAttributes().getLogic());
                spentPoints = player.getKnowledgeSkills().stream().mapToInt(player::getSkillLevel).sum();
            } else {
                throw new IllegalArgumentException("Skill is not a physical or knowledge skill");
            }

            int effectiveLimit = Math.min(Math.min(
                    levelMaximum, availablePoints - spentPoints), Math.max(query.getLimit() - 1, query.getLimit())
            );
            for (int i = 1 + query.getOffset(); i <= effectiveLimit; i++) {
                builder.add(i);
            }

            return builder.build();
        }
    }
}
