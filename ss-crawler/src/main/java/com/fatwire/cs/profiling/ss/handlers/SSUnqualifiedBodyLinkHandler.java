package com.fatwire.cs.profiling.ss.handlers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;

import com.fatwire.cs.profiling.ss.Link;
import com.fatwire.cs.profiling.ss.QueryString;
import com.fatwire.cs.profiling.ss.ResultPage;
import com.fatwire.cs.profiling.ss.util.SSUriHelper;

public class SSUnqualifiedBodyLinkHandler extends AbstractBodyHandler {

    //ssUnqualifiedLink ended by quote, double quote or whitespace
    private final Pattern linkPattern = Pattern
            .compile("ssUnqualifiedLink\\?.*?['\\\" ]");

    /**
     * @param uriHelper
     */
    public SSUnqualifiedBodyLinkHandler(final SSUriHelper uriHelper) {
        super(uriHelper);
    }

    public void visit(ResultPage page) {
        final Matcher m = linkPattern.matcher(page.getBody());

        while (m.find()) {
            log.debug(m.group());
            final String link = m.group();
            if (link.length() > 1) {
                doLink(link.substring(0, link.length() - 1), page);
            }
        }

    }

    void doLink(final String link, ResultPage page) {
        log.trace(link);
        try {
            //<a href='ssUnqualifiedLink?op=CM_Actualidad_FA&c=Page&op2=1142352029508&paginaActual=0&pagename=ComunidadMadrid%2FEstructura&subMenuP=subMenuPresidenta&language=es&cid=1109266752498'
            final URI uri = new URI(StringEscapeUtils.unescapeXml(link));
            //final URI uri = new URI(link);
            log.trace(uri.getQuery());
            final String[] val = uri.getQuery().split("&");
            final Link map = new Link();
            for (final String v : val) {
                if (v.startsWith("blobcol=")) {
                    map.clear();
                    break;
                } else {
                    final int t = v.indexOf('=');
                    map.addParameter(v.substring(0, t), v.substring(t + 1, v
                            .length()));

                }
            }
            if (!map.isOK()) {
                page.addLink(map);
            }
        } catch (final URISyntaxException e) {
            log.error(e);
        }
    }

}
