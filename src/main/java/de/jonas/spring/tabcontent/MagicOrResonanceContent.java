package de.jonas.spring.tabcontent;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import de.jonas.spring.components.ListSelection;
import de.jonas.spring.model.*;
import de.jonas.spring.model.skills.MagicalOrResonanceSkill;
import de.jonas.spring.model.skills.MagicalSkillGroup;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public class MagicOrResonanceContent extends VerticalLayout {
    private ComboBox<MagicalSkillGroup> magicalSkillGroupComboBox;
    private ListSelection<MagicalOrResonanceSkill> magicalSkillSelection;
    private Label noMagicOrResonanceHint = new Label("Dein Charakter kann keine Magie oder Resonanz.");
    private ListSelection<Castable> castableSelection;

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

        magicalSkillSelection.setVisible(awokenType != null && awokenType != AwokenType.ASPECT_WIZARD);
        magicalSkillSelection.setEnabled(canLearnMoreSkills(binder));
        magicalSkillSelection.setItems(EnumSet.allOf(MagicalOrResonanceSkill.class).stream().filter(skill -> skill.canPlayerLearn(binder.getBean())));
        magicalSkillSelection.setSelectedItems(binder.getBean().getMagicalOrResonanceSkills());
        magicalSkillSelection.setLabel(awokenType == AwokenType.TECHNOMANCER ? "Resonanzfähigkeiten (S. 143)" : "Magische Fähigkeiten (S. 142)");

        castableSelection.setVisible(awokenType != null && awokenType != AwokenType.ASPECT_WIZARD);
        castableSelection.setEnabled(canLearnMoreCastables(binder));
        castableSelection.setItems(EnumSet.allOf(Castable.class).stream().filter(castable -> castable.canPlayerLearn(binder.getBean())));
        castableSelection.setSelectedItems(binder.getBean().getCastables());
        castableSelection.setLabel(awokenType == AwokenType.TECHNOMANCER ? "Komplexe Formen (S. 250)" : "Zauber, Rituale, Alchemische Zauber (S. 280)");
    }

    private void build(Binder<PlayerCharacter> binder) {
        add(noMagicOrResonanceHint);

        magicalSkillGroupComboBox = new ComboBox<>();
        magicalSkillGroupComboBox.setLabel("Fertigkeitsgruppe");
        magicalSkillGroupComboBox.setItems(EnumSet.allOf(MagicalSkillGroup.class));
        binder.bind(magicalSkillGroupComboBox, PlayerCharacter::getMagicalSkillGroup, (player, value) -> {
            MagicalSkillGroup currentSkillGroup = player.getMagicalSkillGroup();
            if (currentSkillGroup != null) {
                player.forgetSkill(currentSkillGroup);
            }
            player.learnSkill(value, getBaseSkillLevel(binder));
        });
        add(magicalSkillGroupComboBox);

        addMagicalSkillSelector(binder);
        addCastableSelector(binder);
    }

    private int getBaseSkillLevel(Binder<PlayerCharacter> binder) {
        MagicOrResonance magicOrResonance = binder.getBean().getPriority(Prioritizable.MAGIC_OR_RESONANZ).getMagicOrResonance(binder.getBean().getAwokenType());
        return binder.getBean().getAwokenType() == AwokenType.TECHNOMANCER ? magicOrResonance.getResonanceAbilityLevels() : magicOrResonance.getMagicalAbilityLevels();
    }

    private void addMagicalSkillSelector(Binder<PlayerCharacter> binder) {
        magicalSkillSelection = new ListSelection<>("Fähigkeit");
        magicalSkillSelection.addComponentColumn(magicalSkill -> {
            ComboBox<Integer> comboBox = new ComboBox<>();
            comboBox.setItems(Collections.singletonList(getBaseSkillLevel(binder)));
            comboBox.setValue(getBaseSkillLevel(binder));
            return comboBox;
        }).setHeader("Stufe");
        magicalSkillSelection.addComponentColumn(magicalSkill -> {
            Button removeButton = new Button("Entfernen");
            removeButton.addClickListener(event -> {
                binder.getBean().forgetSkill(magicalSkill);
                List<MagicalOrResonanceSkill> magicalSkills = binder.getBean().getMagicalOrResonanceSkills();
                magicalSkillSelection.setSelectedItems(magicalSkills);
                magicalSkillSelection.setItems(EnumSet.allOf(MagicalOrResonanceSkill.class).stream().filter(e -> !magicalSkills.contains(e)).collect(Collectors.toList()));
                magicalSkillSelection.setEnabled(canLearnMoreSkills(binder));
            });
            return removeButton;
        });

        magicalSkillSelection.onSelect(event -> {
            if (event.getValue() != null) {
                binder.getBean().learnSkill(event.getValue(), getBaseSkillLevel(binder));
                List<MagicalOrResonanceSkill> learnedSkills = binder.getBean().getMagicalOrResonanceSkills();
                magicalSkillSelection.setSelectedItems(learnedSkills);
                magicalSkillSelection.setItems(EnumSet.allOf(MagicalOrResonanceSkill.class).stream().filter(skill -> skill.canPlayerLearn(binder.getBean())).filter(element -> !learnedSkills.contains(element)));
                magicalSkillSelection.setEnabled(canLearnMoreSkills(binder));
            }
        });

        add(magicalSkillSelection);
    }

    private void addCastableSelector(Binder<PlayerCharacter> binder) {
        castableSelection = new ListSelection<>("Zauber, Ritual, Alchemischer Zauber, Komplexe Form");
        castableSelection.addComponentColumn(castable -> {
            Button removeButton = new Button("Entfernen");
            removeButton.addClickListener(event -> {
                binder.getBean().getAllCastables().remove(castable);
                List<Castable> learnedCastables = binder.getBean().getCastables();
                castableSelection.setItems(learnedCastables);
                castableSelection.setItems(EnumSet.allOf(Castable.class).stream().filter(castable1 -> castable1.canPlayerLearn(binder.getBean())).filter(element -> !learnedCastables.contains(element)).collect(Collectors.toList()));
                castableSelection.setEnabled(canLearnMoreCastables(binder));
            });
            return removeButton;
        });

        castableSelection.onSelect(event -> {
            if (event.getValue() != null) {
                binder.getBean().getAllCastables().add(event.getValue());
                List<Castable> learnedCastables = binder.getBean().getCastables();
                castableSelection.setSelectedItems(learnedCastables);
                castableSelection.setItems(EnumSet.allOf(Castable.class).stream().filter(castable1 -> castable1.canPlayerLearn(binder.getBean())).filter(element -> !learnedCastables.contains(element)).collect(Collectors.toList()));
                castableSelection.setEnabled(canLearnMoreCastables(binder));
            }
        });

        add(castableSelection);
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
        int learnedCastables = binder.getBean().getCastables().size();
        MagicOrResonance magicOrResonance = priority.getMagicOrResonance(binder.getBean().getAwokenType());
        if (binder.getBean().getAwokenType() == AwokenType.TECHNOMANCER) {
            return learnedCastables < magicOrResonance.getComplexForms();
        } else {
            return learnedCastables < magicOrResonance.getSpellsRitualsAlchemy();
        }
    }
}
