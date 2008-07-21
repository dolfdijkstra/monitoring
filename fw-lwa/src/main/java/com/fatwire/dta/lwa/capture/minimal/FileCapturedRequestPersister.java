package com.fatwire.dta.lwa.capture.minimal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fatwire.dta.lwa.capture.CapturedRequestListener;
import com.fatwire.dta.lwa.capture.PersistanceException;
import com.fatwire.dta.lwa.capture.RequestEncoder;

public class FileCapturedRequestPersister implements
        CapturedRequestListener<MinimalCapturedRequest> {
    static final Log log = LogFactory
            .getLog(FileCapturedRequestPersister.class);

    private final RequestEncoder<MinimalCapturedRequest, String> encoder = new MinimalCapturedRequestToMulitLineStringEncoder();

    private File file;

    private Writer out;

    /**
     * @param file
     */
    public FileCapturedRequestPersister(final File file) {
        super();
        this.file = file;
    }

    public void capturedRequestReceived(final MinimalCapturedRequest capturedRequest)
            throws PersistanceException {

        try {
            out.write(encoder.encode(capturedRequest));

        } catch (final IOException e) {
            FileCapturedRequestPersister.log.error(e + ". Switching off output for now.");
            out = new PrintWriter(new NullWriter());
        }
    }

    public void start() {
        try {
            out = new OutputStreamWriter(new FileOutputStream(file, true),
                    "UTF-8");
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void stop() {
        try {
            out.close();
        } catch (final IOException e) {
            //nothing

        }
    }

    private static class NullWriter extends Writer {

        @Override
        public void close() throws IOException {
            //          nothing

        }

        @Override
        public void flush() throws IOException {
            //          nothing

        }

        @Override
        public void write(final char[] cbuf, final int off, final int len) throws IOException {
            //          nothing

        }

    }
}
