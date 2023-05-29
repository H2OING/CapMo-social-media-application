package com.example.application.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HtmlContainer;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;

@Tag("div")
@CssImport("./styles/components/card.css")
public class Card extends HtmlContainer {

    private final Div header;
    private final Div content;
    private final Div footer;

    public Card(Component... components) {
        addClassName("card");
        header = new Div();
        header.addClassName("card-header");
        content = new Div();
        content.addClassName("card-content");
        footer = new Div();
        footer.addClassName("card-footer");
        add(header, content, footer);
        setContent(components);
    }

    public void setHeader(Component... components) {
        header.removeAll();
        header.add(components);
    }

    public void setContent(Component... components) {
        content.removeAll();
        content.add(components);
    }

    public void setFooter(Component... components) {
        footer.removeAll();
        footer.add(components);
    }

}