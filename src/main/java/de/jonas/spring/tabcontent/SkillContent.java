package de.jonas.spring.tabcontent;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import de.jonas.spring.model.*;
import de.jonas.spring.model.skills.*;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public class SkillContent extends VerticalLayout {
    private Grid<PhysicalSkill> physicalSkillGrid;
    private ComboBox<PhysicalSkill> physicalSkillComboBox;
    private Grid<KnowledgeSkill> knowledgeSkillGrid;
    private TextField knowledgeSkillTextField;

    public SkillContent(Binder<PlayerCharacter> binder) {
        setPadding(false);

        build(binder);
        updateVisibility(binder);
        binder.addValueChangeListener(event -> updateVisibility(binder));
    }

    private void updateVisibility(Binder<PlayerCharacter> binder) {
        Priority priority = binder.getBean().getPriority(Prioritizable.ABILITY_POINTS);
        physicalSkillGrid.setVisible(priority != null);
        physicalSkillGrid.setItems(binder.getBean().getPhysicalSkills());

        physicalSkillComboBox.setVisible(priority != null);
        physicalSkillComboBox.setEnabled(canLearnMorePhysicalSkills(binder));
        physicalSkillComboBox.setItems(EnumSet.allOf(PhysicalSkill.class));

        knowledgeSkillGrid.setVisible(priority != null);
        knowledgeSkillGrid.setItems(binder.getBean().getKnowledgeSkills());

        knowledgeSkillTextField.setVisible(priority != null);
        knowledgeSkillTextField.setEnabled(canLearnMoreKnowledgeSkills(binder));
    }

    private void build(Binder<PlayerCharacter> binder) {
        addPhysicalSkillSelector(binder);
        addKnowledgeSkillSelector(binder);
    }

    private void addPhysicalSkillSelector(Binder<PlayerCharacter> binder) {
        physicalSkillGrid = new Grid<>();
        physicalSkillComboBox = new ComboBox<>();
        physicalSkillComboBox.setWidth("40%");
        physicalSkillComboBox.setLabel("Aktionsfertigkeiten (S. 130)");

        physicalSkillGrid.setSelectionMode(Grid.SelectionMode.NONE);
        physicalSkillGrid.addColumn(PhysicalSkill::toString).setHeader("Fähigkeit");
        physicalSkillGrid.addComponentColumn(skill -> {
            ComboBox<Integer> comboBox = new ComboBox<>();
            comboBox.setItems(binder.getBean().getSkillLevel(skill));
            binder.bind(comboBox, player -> {
                return player.getSkillLevel(skill);
            }, (player, value) -> {
                player.learnSkill(skill, value);
            });
            comboBox.setItems(Collections.singletonList(-1));
            comboBox.setValue(-1);
            return comboBox;
        }).setHeader("Stufe");
        physicalSkillGrid.addComponentColumn(skill -> {
            Button removeButton = new Button("Entfernen");
            removeButton.addClickListener(event -> {
                binder.getBean().forgetSkill(skill);
                List<PhysicalSkill> physicalSkills = binder.getBean().getPhysicalSkills();
                physicalSkillGrid.setItems(physicalSkills);
                physicalSkillComboBox.setItems(EnumSet.allOf(PhysicalSkill.class).stream().filter(physicalSkills::contains).collect(Collectors.toList()));
                physicalSkillComboBox.setEnabled(canLearnMorePhysicalSkills(binder));
            });
            return removeButton;
        });

        physicalSkillComboBox.addValueChangeListener(event -> {
            if (event.getValue() != null) {
                binder.getBean().learnSkill(event.getValue(), -1);
                List<PhysicalSkill> learnedSkills = binder.getBean().getPhysicalSkills();
                physicalSkillGrid.setItems(learnedSkills);
                physicalSkillComboBox.setItems(EnumSet.allOf(PhysicalSkill.class).stream().filter(element -> !learnedSkills.contains(element)));
                physicalSkillComboBox.setEnabled(canLearnMorePhysicalSkills(binder));
            }
        });

        add(physicalSkillComboBox);
        add(physicalSkillGrid);
    }

    private void addKnowledgeSkillSelector(Binder<PlayerCharacter> binder) {
        knowledgeSkillGrid = new Grid<>();
        knowledgeSkillTextField = new TextField();
        knowledgeSkillTextField.setWidth("40%");
        knowledgeSkillTextField.setLabel("Wissensfertigkeiten (S. 148)");

        knowledgeSkillGrid.setSelectionMode(Grid.SelectionMode.NONE);
        knowledgeSkillGrid.addColumn(Skill::toString).setHeader("Zauber, Ritual, Alchemischer Zauber, Komplexe Form");
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
                binder.getBean().getKnowledgeSkills().add(new KnowledgeSkill(knowledgeSkillTextField.getValue()));
                List<KnowledgeSkill> learnedKnowledgeSkills = binder.getBean().getKnowledgeSkills();
                knowledgeSkillGrid.setItems(learnedKnowledgeSkills);
                knowledgeSkillTextField.setEnabled(canLearnMoreKnowledgeSkills(binder));
            }
        });

        add(knowledgeSkillTextField);
        add(knowledgeSkillGrid);
    }

   private boolean canLearnMoreKnowledgeSkills(Binder<PlayerCharacter> binder) {
        if (binder.getBean().getAwokenType() == null) {
            return false;
        }

        Priority priority = binder.getBean().getPriority(Prioritizable.ABILITY_POINTS);
        if (priority == null) {
            return false;
        }
        int learnedKnowledgeSkills = binder.getBean().getKnowledgeSkills().size();
        return learnedKnowledgeSkills < priority.getStartingAbilityPoints().getSingleAbilities();
    }

    private boolean canLearnMorePhysicalSkills(Binder<PlayerCharacter> binder) {
        if (binder.getBean().getAwokenType() == null) {
            return false;
        }

        Priority priority = binder.getBean().getPriority(Prioritizable.ABILITY_POINTS);
        if (priority == null) {
            return false;
        }
        int learnedKnowledgeSkills = binder.getBean().getPhysicalSkills().size();
        return learnedKnowledgeSkills < priority.getStartingAbilityPoints().getSingleAbilities();
    }
}
