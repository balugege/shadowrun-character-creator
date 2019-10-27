package de.jonas.spring.tabcontent;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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
import java.util.stream.Stream;

public class AdvantagesContent extends VerticalLayout {
    private ListSelection<PhysicalSkill> advantagesSelection;
    private ListSelection<PhysicalSkill> disAdvantagesSelection;

    public AdvantagesContent(Binder<PlayerCharacter> binder) {
        setPadding(false);

        build(binder);
        updateVisibility(binder);
        binder.addValueChangeListener(event -> updateVisibility(binder));
    }

    private void updateVisibility(Binder<PlayerCharacter> binder) {
        boolean canLearnMorePhysicalSkills = canLearnMoreAdvantagesSkills(binder);
        advantagesSelection.setEnabled(canLearnMorePhysicalSkills);
        advantagesSelection.setItems(EnumSet.allOf(PhysicalSkill.class));
        advantagesSelection.setSelectedItems(binder.getBean().getPhysicalSkills());

        disAdvantagesSelection.setVisible(true);
    }

    private void build(Binder<PlayerCharacter> binder) {
        addAdvantagesSelector(binder);
        addDisadvantagesSelector(binder);
    }

    private void addAdvantagesSelector(Binder<PlayerCharacter> binder) {
        advantagesSelection = new ListSelection<>("Vorteil");
        advantagesSelection.setLabel("Vorteile (S. 83 GRW)");

        advantagesSelection.addComponentColumn(skill -> new SkillLevelComboBox(skill, binder)).setHeader("Stufe");

        advantagesSelection.addComponentColumn(skill -> {
            Button removeButton = new Button("Entfernen");
            removeButton.addClickListener(event -> {
                binder.getBean().forgetSkill(skill);
                List<PhysicalSkill> physicalSkills = binder.getBean().getPhysicalSkills();
                advantagesSelection.setSelectedItems(physicalSkills);
                advantagesSelection.setItems(EnumSet.allOf(PhysicalSkill.class).stream().filter(element -> !physicalSkills.contains(element)));
                advantagesSelection.setEnabled(canLearnMoreAdvantagesSkills(binder));
            });
            return removeButton;
        }).setHeader("");

        advantagesSelection.onSelect(event -> {
            if (event.getValue() != null) {
                binder.getBean().learnSkill(event.getValue(), 1);
                List<PhysicalSkill> learnedSkills = binder.getBean().getPhysicalSkills();
                advantagesSelection.setSelectedItems(learnedSkills);
                advantagesSelection.setItems(EnumSet.allOf(PhysicalSkill.class).stream().filter(element -> !learnedSkills.contains(element)));
                advantagesSelection.setEnabled(canLearnMoreAdvantagesSkills(binder));
            }
        });

        add(advantagesSelection);
    }

    private void addDisadvantagesSelector(Binder<PlayerCharacter> binder) {
        disAdvantagesSelection = new ListSelection<>("Vorteil");
        disAdvantagesSelection.setLabel("Vorteile (S. 83 GRW)");

        disAdvantagesSelection.addComponentColumn(skill -> new SkillLevelComboBox(skill, binder)).setHeader("Stufe");

        disAdvantagesSelection.addComponentColumn(skill -> {
            Button removeButton = new Button("Entfernen");
            removeButton.addClickListener(event -> {
                binder.getBean().forgetSkill(skill);
                List<PhysicalSkill> physicalSkills = binder.getBean().getPhysicalSkills();
                advantagesSelection.setSelectedItems(physicalSkills);
                advantagesSelection.setItems(EnumSet.allOf(PhysicalSkill.class).stream().filter(element -> !physicalSkills.contains(element)));
                advantagesSelection.setEnabled(canLearnMoreAdvantagesSkills(binder));
            });
            return removeButton;
        }).setHeader("");

        disAdvantagesSelection.onSelect(event -> {
            if (event.getValue() != null) {
                if (canLearnMoreAdvantagesSkills(binder)) {
                    binder.getBean().learnSkill(event.getValue(), 1);
                    List<PhysicalSkill> learnedSkills = binder.getBean().getPhysicalSkills();
                    disAdvantagesSelection.setSelectedItems(learnedSkills);
                    disAdvantagesSelection.setItems(EnumSet.allOf(PhysicalSkill.class).stream().filter(element -> !learnedSkills.contains(element)));
                }
            }
        });

        add(disAdvantagesSelection);
    }

    private boolean canLearnMoreAdvantagesSkills(Binder<PlayerCharacter> binder) {
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
