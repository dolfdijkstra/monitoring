package com.fatwire.cs.page;

/**
 * Renders a part of a page (or a complete page if this is the top-level part)
 * 
 * @author Dolf.Dijkstra
 * @since Nov 25, 2007
 * @param <E>
 */
public interface HtmlBodyRenderer<E> extends PartRenderer<E>{

    public <T extends HtmlPage<E>> void renderPart(T page, E model) throws Exception;

}