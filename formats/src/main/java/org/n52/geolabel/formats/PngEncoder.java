/**
 * Copyright 2013 52°North Initiative for Geospatial Open Source Software GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.n52.geolabel.formats;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.n52.geolabel.commons.Label;

public class PngEncoder implements LabelEncoder {

    private static final int DEFAULT_SIZE = 256;

    private static final String MIME_TYPE = "image/png";

    @Override
    public InputStream encode(Label label, int size) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        label.toSVG(new OutputStreamWriter(out, Charset.forName("utf-8")), "png-transform-base", size);

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        return encode(in);
    }

    public InputStream encode(final InputStream in) throws IOException {
        if (in == null)
            throw new IOException("Input stream is null!");

        TranscoderInput input = new TranscoderInput(in);
        PNGTranscoder t = new PNGTranscoder();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TranscoderOutput output = new TranscoderOutput(outputStream);
        try {
            t.transcode(input, output);
        }
        catch (TranscoderException e) {
            throw new IOException(e);
        }

        outputStream.flush();

        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    @Override
    public InputStream encode(Label label) throws IOException {
        return encode(label, DEFAULT_SIZE);
    }

    @Override
    public String getMimeType() {
        return MIME_TYPE;
    }

}
