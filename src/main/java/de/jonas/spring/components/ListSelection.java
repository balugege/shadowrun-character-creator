package de.jonas.spring.components;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ComponentDataGenerator;
import com.vaadin.flow.function.ValueProvider;

import java.util.Collection;
import java.util.stream.Stream;

public class ListSelection<T> extends VerticalLayout {
    private ComboBox<T> comboBox = new ComboBox<>();
    private Grid<T> grid = new Grid<>();

    public ListSelection(String selectedName) {
        add(comboBox);
        add(grid);
        setPadding(false);

        comboBox.setWidth("40%");
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.addColumn(Object::toString).setHeader(selectedName);
    }

    public void onSelect(HasValue.ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<ComboBox<T>, T>> consumer) {
        comboBox.addValueChangeListener(consumer);
    }

    public void setLabel(String label) {
        comboBox.setLabel(label);
    }

    public void setItems(Collection<T> items) {
        comboBox.setItems(items);
    }

    public void setItems(Stream<T> items) {
        comboBox.setItems(items);
    }

    public <V extends Component> Grid.Column<T> addComponentColumn(ValueProvider<T, V> componentProvider) {
        return grid.addComponentColumn(componentProvider);
    }

    public void setSelectedItems(Collection<T> items) {
        grid.setItems(items);
    }

    public void setSelectedItems(Stream<T> items) {
        grid.setItems(items);
    }

    @Override
    public void setEnabled(boolean enabled) {
        comboBox.setEnabled(enabled);
    }
}
