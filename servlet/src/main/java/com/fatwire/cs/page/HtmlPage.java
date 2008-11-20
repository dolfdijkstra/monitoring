/**
 * 
 */
package com.fatwire.cs.page;

import java.io.IOException;
import java.io.PrintWriter;


public class HtmlPage<T> implements Page<T> {

    private final PartRenderer<T> renderer;

    private final PrintWriter out;

    /**
     * @param out
     * @throws IOException 
     */
    public HtmlPage(final PartRenderer<T> renderer, final PrintWriter out)
            throws IOException {
        super();
        this.renderer = renderer;
        this.out = out;
    }

    public void doDTD() {
        out.print("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        out
                .println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");

    }

    public void doHeader() {
        out
                .print("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-US\" lang=\"en-US\">");
        out.print("<header><title>Info</title></header>");
        out.print("<body>");
    }

    public void doBody(final T model) throws Exception {
        renderer.renderPart(this, model);
    }

    public void doEnd() {
        out.print("</body></html>");
    }

    /* (non-Javadoc)
     * @see com.fatwire.cs.spring.controllers.Page#doPage(javax.servlet.http.HttpServletRequest)
     */
    public void renderPage(final T model) throws Exception {
        doDTD();
        doHeader();
        doBody(model);
        doEnd();
    }

    public void printTableOpen() {
        out.print("<table>");
    }

    public void printTableClose() {
        out.print("</table>");
    }

    public void printTableSectionTitle(final String title) {
        printTableSectionTitle(title, 2);
    }

    public void printTableSectionTitle(final String title, final int span) {
        out.print("<tr>");
        out.print("<th colspan=\"" + span + "\">");
        out.print(title);
        out.print("</th>");
        out.print("</tr>");
    }

    public void printTableRow(final String... cellValues) {
        out.print("<tr>");
        for (final String cell : cellValues) {
            out.print("<td>");
            out.print(cell);
            out.print("</td>");
        }
        out.print("</tr>");
    }

}