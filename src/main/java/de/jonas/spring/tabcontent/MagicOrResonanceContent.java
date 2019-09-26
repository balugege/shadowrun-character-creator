package de.jonas.spring.tabcontent;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import de.jonas.spring.model.*;
import de.jonas.spring.model.skills.MagicalOrResonanceSkill;
import de.jonas.spring.model.skills.MagicalSkillGroup;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public class MagicOrResonanceContent extends VerticalLayout {
    private Grid<MagicalOrResonanceSkill> magicalSkillGrid;
    private Label noMagicOrResonanceHint = new Label("Dein Charakter kann keine Magie oder Resonanz.");
    private ComboBox<MagicalSkillGroup> magicalSkillGroupComboBox;
    private ComboBox<MagicalOrResonanceSkill> skillComboBox;
    private Grid<Castable> castableGrid;
    private ComboBox<Castable> castableComboBox;

    public MagicOrResonanceContent(Binder<PlayerCharacter> binder) {
        setPadding(false);

        build(binder);
        updateVisibility(binder);
        binder.addValueChangeListener(event -> updateVisibility(binder));
    }

    private void updateVisibility(Binder<PlayerCharacter> binder) {
        AwokenType awokenType = binder.getBean().getAwokenType();

        noMagicOrResonanceHint.setVisible(awokenType == null);

        magicalSkillGroupComboBox.setVisible(awokenType == AwokenType.ASPECT_WIZARD);

        magicalSkillGrid.setVisible(awokenType != null && awokenType != AwokenType.ASPECT_WIZARD);
        magicalSkillGrid.setItems(binder.getBean().getMagicalOrResonanceSkills());

        skillComboBox.setVisible(awokenType != null && awokenType != AwokenType.ASPECT_WIZARD);
        skillComboBox.setEnabled(canLearnMoreSkills(binder));
        skillComboBox.setItems(EnumSet.allOf(MagicalOrResonanceSkill.class).stream().filter(skill -> skill.canPlayerLearn(binder.getBean())));
        skillComboBox.setLabel(awokenType == AwokenType.TECHNOMANCER ? "Resonanzfähigkeiten (S. 143)" : "Magische Fähigkeiten (S. 142)");

        castableGrid.setVisible(awokenType != null);
        castableGrid.setItems(binder.getBean().getCastables());

        castableComboBox.setVisible(awokenType != null);
        castableComboBox.setEnabled(canLearnMoreCastables(binder));
        castableComboBox.setItems(EnumSet.allOf(Castable.class).stream().filter(castable -> castable.canPlayerLearn(binder.getBean())));
        castableComboBox.setLabel(awokenType == AwokenType.TECHNOMANCER ? "Komplexe Formen (S. 250)" : "Zauber, Rituale, Alchemische Zauber (S. 280)");
    }

    private void build(Binder<PlayerCharacter> binder) {
        add(noMagicOrResonanceHint);

        magicalSkillGroupComboBox = new ComboBox<>();
        magicalSkillGroupComboBox.setLabel("Fertigkeitsgruppe");
        magicalSkillGroupComboBox.setItems(EnumSet.allOf(MagicalSkillGroup.class));
        binder.bind(magicalSkillGroupComboBox, PlayerCharacter::getMagicalSkillGroup, PlayerCharacter::setMagicalSkillGroup);
        add(magicalSkillGroupComboBox);

        addMagicalSkillSelector(binder);
        addCastableSelector(binder);
    }

    private void addMagicalSkillSelector(Binder<PlayerCharacter> binder) {
        magicalSkillGrid = new Grid<>();
        skillComboBox = new ComboBox<>();
        skillComboBox.setWidth("40%");

        magicalSkillGrid.setSelectionMode(Grid.SelectionMode.NONE);
        magicalSkillGrid.addColumn(MagicalOrResonanceSkill::toString).setHeader("Fähigkeit");
        magicalSkillGrid.addComponentColumn(magicalSkill -> {
            ComboBox<Integer> comboBox = new ComboBox<>();
            MagicOrResonance magicOrResonance = binder.getBean().getPriority(Prioritizable.MAGIC_OR_RESONANZ).getMagicOrResonance(binder.getBean().getAwokenType());
            int baseValue = binder.getBean().getAwokenType() == AwokenType.TECHNOMANCER ? magicOrResonance.getResonanceAbilityLevels() : magicOrResonance.getMagicalAbilityLevels();
            comboBox.setItems(Collections.singletonList(baseValue));
            comboBox.setValue(baseValue);
            return comboBox;
        }).setHeader("Stufe");
        magicalSkillGrid.addComponentColumn(magicalSkill -> {
            Button removeButton = new Button("Entfernen");
            removeButton.addClickListener(event -> {
                binder.getBean().getAllMagicalOrResonanceSkills().remove(magicalSkill);
                List<MagicalOrResonanceSkill> magicalSkills = binder.getBean().getMagicalOrResonanceSkills();
                magicalSkillGrid.setItems(magicalSkills);
                skillComboBox.setItems(EnumSet.allOf(MagicalOrResonanceSkill.class).stream().filter(magicalSkills::contains).collect(Collectors.toList()));
                skillComboBox.setEnabled(canLearnMoreSkills(binder));
            });
            return removeButton;
        });

        skillComboBox.addValueChangeListener(event -> {
            if (event.getValue() != null) {
                binder.getBean().getAllMagicalOrResonanceSkills().add(event.getValue());
                List<MagicalOrResonanceSkill> learnedSkills = binder.getBean().getMagicalOrResonanceSkills();
                magicalSkillGrid.setItems(learnedSkills);
                skillComboBox.setItems(EnumSet.allOf(MagicalOrResonanceSkill.class).stream().filter(skill -> skill.canPlayerLearn(binder.getBean())).filter(element -> !learnedSkills.contains(element)));
                skillComboBox.setEnabled(canLearnMoreSkills(binder));
            }
        });

        add(skillComboBox);
        add(magicalSkillGrid);
    }

    private void addCastableSelector(Binder<PlayerCharacter> binder) {
        castableGrid = new Grid<>();
        castableComboBox = new ComboBox<>();
        castableComboBox.setWidth("40%");

        castableGrid.setSelectionMode(Grid.SelectionMode.NONE);
        castableGrid.addColumn(Castable::toString).setHeader("Zauber, Ritual, Alchemischer Zauber, Komplexe Form");
        castableGrid.addComponentColumn(castable -> {
            Button removeButton = new Button("Entfernen");
            removeButton.addClickListener(event -> {
                binder.getBean().getAllCastables().remove(castable);
                List<Castable> learnedCastables = binder.getBean().getCastables();
                castableGrid.setItems(learnedCastables);
                castableComboBox.setItems(EnumSet.allOf(Castable.class).stream().filter(castable1 -> castable1.canPlayerLearn(binder.getBean())).filter(element -> !learnedCastables.contains(element)).collect(Collectors.toList()));
                castableComboBox.setEnabled(canLearnMoreCastables(binder));
            });
            return removeButton;
        });

        castableComboBox.addValueChangeListener(event -> {
            if (event.getValue() != null) {
                binder.getBean().getAllCastables().add(event.getValue());
                List<Castable> learnedCastables = binder.getBean().getCastables();
                castableGrid.setItems(learnedCastables);
                castableComboBox.setItems(EnumSet.allOf(Castable.class).stream().filter(castable1 -> castable1.canPlayerLearn(binder.getBean())).filter(element -> !learnedCastables.contains(element)).collect(Collectors.toList()));
                castableComboBox.setEnabled(canLearnMoreCastables(binder));
            }
        });

        add(castableComboBox);
        add(castableGrid);
    }

    private boolean canLearnMoreSkills(Binder<PlayerCharacter> binder) {
        if (binder.getBean().getAwokenType() == null) {
            return false;
        }

        Priority priority = binder.getBean().getPriority(Prioritizable.MAGIC_OR_RESONANZ);
        if (priority == null) {
            return false;
        }
        int selectedSkills = binder.getBean().getMagicalOrResonanceSkills().size();
        MagicOrResonance magicOrResonance = priority.getMagicOrResonance(binder.getBean().getAwokenType());
        if (binder.getBean().getAwokenType() == AwokenType.TECHNOMANCER) {
            return selectedSkills < magicOrResonance.getResonanceAbilities();
        } else {
            return selectedSkills < magicOrResonance.getMagicalAbilities();
        }
    }

    private boolean canLearnMoreCastables(Binder<PlayerCharacter> binder) {
        if (binder.getBean().getAwokenType() == null) {
            return false;
        }

        Priority priority = binder.getBean().getPriority(Prioritizable.MAGIC_OR_RESONANZ);
        if (priority == null) {
            return false;
        }
        int selectedCastables = binder.getBean().getCastables().size();
        MagicOrResonance magicOrResonance = priority.getMagicOrResonance(binder.getBean().getAwokenType());
        if (binder.getBean().getAwokenType() == AwokenType.TECHNOMANCER) {
            return selectedCastables < magicOrResonance.getComplexForms();
        } else {
            return selectedCastables < magicOrResonance.getSpellsRitualsAlchemy();
        }
    }
}
