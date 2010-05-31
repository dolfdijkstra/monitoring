package com.fatwire.cs.page;

/**
 * Renders a part of a page (or a complete page if this is the top-level part)
 * 
 * @author Dolf.Dijkstra
 * @since Nov 25, 2007
 * @param <E> the model to render
 */
public interface PartRenderer<E> {

    public void renderPart(HtmlPage<E> page, E model) throws Exception;

}